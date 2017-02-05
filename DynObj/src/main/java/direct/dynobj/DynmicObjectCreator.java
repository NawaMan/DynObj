package direct.dynobj;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.Optional;

/**
 * Creator for a dynamic object.
 * 
 * @author dssb
 **/
public class DynmicObjectCreator {
    
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
        
        Class<?>[] interfaces = prepareInterfaces(mainInterface, moreInterface);
        InvocationHandler invocationHandler = createInvocationHandler(fieldHolder);
        ClassLoader classLoader = mainInterface.getClassLoader();
        
        @SuppressWarnings("unchecked")
        T dynObj = (T) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        
        return dynObj;
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
    
    private InvocationHandler createInvocationHandler(FieldsHolder getters) {
        InvocationHandler invocationHandler = (Object proxy, Method method, Object[] args) -> {
            String fieldName = method.getName();
            Optional<Object> returned = get(getters, method, fieldName);
            handleMissingField(method, returned, fieldName);
            
            Object rawValue = returned.orElse(null);
            Object retValue = handlePrimitiveReturnType(method, rawValue);
            return retValue;
        };
        return invocationHandler;
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
    
    private void handleMissingField(Method method, Optional<Object> returned, String fieldName) {
        if (returned != null) {
            return;
        }
        
        Class<?> declaringClass = method.getDeclaringClass();
        throw new MissingFieldException(fieldName, declaringClass);
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
