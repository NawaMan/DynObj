package direct.dynobj;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Creator for a dynamic object.
 * 
 * @author dssb
 **/
public class DynmicObjectCreator {
    
    // TODO - Think about an ability to freeze the value (validate them before freezing).
    
    private static Method orgFieldsHolderMethod;
    static {
        try {
            Method mth = WithOriginalFieldsHolder.class.getMethod("_originalFieldHolder", new Class[0]);
            orgFieldsHolderMethod = mth;
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static Method interfacesMethod;
    static {
        try {
            Method mth = WithInterfaces.class.getMethod("_interfaces", new Class[0]);
            interfacesMethod = mth;
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    /**
     * Create the object.
     * 
     * @param fieldHolder    the field holder for this object.
     * @param mainInterface  the main interface that the returning object will implement.
     * @param moreInterface  the additional interfaces that the returning object will implement.
     **/
    public <T> T create(
            FieldsHolder fieldHolder,
            Class<T>     mainInterface,
            Class<?>...  moreInterface) {
        checkPrecondition(fieldHolder, mainInterface, moreInterface);
        
        // TODO - May be we should ensure that these are all interface
        Class<?>[]        interfaces       = prepareInterfaces(mainInterface, moreInterface);
        InvocationHandler invocationHandler = createInvocationHandler(fieldHolder, interfaces);
        ClassLoader       classLoader       = mainInterface.getClassLoader();
        
        @SuppressWarnings("unchecked")
        T dynObj = (T) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        
        return dynObj;
    }

    private boolean checkIfImplement(Class<?>[] interfaces, Class<?> clzz) {
        for (int i = 0; i < interfaces.length; i++) {
            Class<?> intf = interfaces[i];
            if (clzz.isAssignableFrom(intf)) {
                return true;
            }
        }
        return false;
    }
    
    private <T> void checkPrecondition(FieldsHolder getters, Class<T> inf, Class<?>... infs) {
        Objects.requireNonNull(getters);
        Objects.requireNonNull(inf);
        for (int i = 0; i < infs.length; i++) {
            if (infs[i] == null) {
                throw new NullPointerException("Interface at: " + i);
            }
        }
    }
    
    private <T> Class<?>[] prepareInterfaces(Class<T> inf, Class<?>... infs) {
        Class<?>[] interfaces = null;
        if ((infs == null) || (infs.length == 0)) {
            interfaces = new Class<?>[] { inf, DynamicObject.class };
        } else {
            interfaces = new Class<?>[infs.length + 2];
            interfaces[0] = inf;
            interfaces[1] = DynamicObject.class;
            System.arraycopy(infs, 0, interfaces, 2, infs.length);
        }
        return interfaces;
    }
    
    private InvocationHandler createInvocationHandler(FieldsHolder getters, Class<?>[] interfaces) {
        InvocationHandler invocationHandler = (Object proxy, Method method, Object[] args) -> {
            // Should we allow function here?
            if (method.getParameterTypes().length != 0) {
                return handleNonGetterMethod(proxy, method, args);
            }
            if (method.isAnnotationPresent(NonGetter.class)) {
                return handleNonGetterMethod(proxy, method, args);
            }
            

            boolean isWithOrgFH = checkIfImplement(interfaces, WithOriginalFieldsHolder.class);
            if (isWithOrgFH
             && method.getName().equals(orgFieldsHolderMethod.getName())
             && orgFieldsHolderMethod.getDeclaringClass().isAssignableFrom(method.getDeclaringClass())
             && method.getReturnType().equals(FieldsHolder.class)) {
                return getters;
            }
            boolean isWithInfs  = checkIfImplement(interfaces, WithInterfaces.class);
            if (isWithInfs
             && method.getName().equals(interfacesMethod.getName())
             && interfacesMethod.getDeclaringClass().isAssignableFrom(method.getDeclaringClass())
             && method.getReturnType().equals(Collection.class)) {
                return Arrays.asList(interfaces);
            }

            String fieldName = method.getName();
            Optional<Object> returned = get(getters, method, fieldName);
            Object rawValue = handleMissingField(proxy, method, returned, fieldName);
            Object retValue = handlePrimitiveReturnType(method, rawValue);
            return retValue;
        };
        return invocationHandler;
    }

    private Object handleNonGetterMethod(Object proxy, Method method, Object[] args) 
            throws Throwable, IllegalAccessException {
        String fieldName = method.getName();
        Class<?> declaringClass = method.getDeclaringClass();
        if (method.isDefault()) {
            Object defaultReturnValue = runDefaultImplementation(proxy, method, args, declaringClass);
            return defaultReturnValue;
        } else {
            throw new MethodNotFieldGetterException(fieldName, declaringClass);
        }
    }

    private Object runDefaultImplementation(Object proxy, Method method, Object[] args, Class<?> declaringClass)
            throws Throwable, IllegalAccessException {
        final Constructor<MethodHandles.Lookup> constructor
                = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
    }
    
    private Optional<Object> get(FieldsHolder getters, Method method, String fieldName) {
        try {
            Optional<Object> returnedValue = getters.get(fieldName);
            return returnedValue;
        } catch (Throwable cause) {
            Class<?> declaringClass = method.getDeclaringClass();
            throw new FieldGetExecutionException(fieldName, declaringClass, cause);
        }
    }
    
    private Object handleMissingField(Object proxy, Method method, Optional<Object> returned, String fieldName)
            throws IllegalAccessException, Throwable {
        if (returned != null) {
            return returned.orElse(null);
        }

        Class<?> declaringClass = method.getDeclaringClass();
        if (method.isDefault()) {
            Object defaultReturnValue = runDefaultImplementation(proxy, method, new Object[0], declaringClass);
            return defaultReturnValue;
        } else {
            throw new MissingFieldException(fieldName, declaringClass);
        }
    }
    
    private Object handlePrimitiveReturnType(Method method, Object value) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            if (returnType == int.class) {
                return ((Number) value).intValue();
            }
            if (returnType == boolean.class) {
                return ((Boolean) value).booleanValue();
            }
            if (returnType == double.class) {
                return ((Number) value).doubleValue();
            }
            if (returnType == char.class) {
                return ((Character) value).charValue();
            }
            
            if (returnType == byte.class) {
                return ((Number) value).byteValue();
            }
            if (returnType == long.class) {
                return ((Number) value).longValue();
            }
            
            if (returnType == short.class) {
                return ((Number) value).shortValue();
            }
            if (returnType == float.class) {
                return ((Number) value).floatValue();
            }
        }
        return value;
    }
    
}
