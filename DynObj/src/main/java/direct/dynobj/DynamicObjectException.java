package direct.dynobj;

/**
 * This exception is a base exception for this package.
 * 
 * @author dssb
 **/
public class DynamicObjectException extends RuntimeException {
    
    private static final long serialVersionUID = 2094997913843232153L;
    
    private final String fieldName;
    
    private final String interfaceClassName;
    
    private final Class<?> interfaceClass;
    
    /**
     * Constructors which requires useful information.
     * 
     * @param fieldName       the name of the field the dynamic object was asked for.
     * @param interfaceClass  the interface of which the method is declared.
     * @param cause           the root cause of the exception.
     **/
    protected DynamicObjectException(
            String    fieldName,
            Class<?>  interfaceClass,
            Throwable cause) {
        super(prepareErrorMessage(fieldName, interfaceClass), cause);
        this.fieldName          = fieldName;
        this.interfaceClass     = interfaceClass;
        this.interfaceClassName = interfaceClass.getCanonicalName();
    }
    
    private static String regex = "^(.*)::([^.]+)$";
    
    /**
     * This method provide a standard text for the error message.
     * 
     * @param fieldName       the name of the field the dynamic object was asked for.
     * @param interfaceClass  the interface of which the method is declared.
     * @return  the error message.
     **/
    public static String prepareErrorMessage(
            String   fieldName,
            Class<?> interfaceClass) {
        return interfaceClass.getCanonicalName() + "::" + fieldName;
    }
    
    /**
     * Extract the field name form the exception error message.
     * 
     * @param errorMessage  the error message.
     * @return  the field name.
     **/
    public static String getFieldNameFromErrorMessage(String errorMessage) {
        return errorMessage.replaceAll(regex, "$2");
    }

    /**
     * Extract the interface name form the exception error message.
     * 
     * @param errorMessage  the error message.
     * @return  the interface name.
     **/
    public static String getInterfaceNameFromErrorMessage(String errorMessage) {
        return errorMessage.replaceAll(regex, "$1");
    }
    
    /**
     * @return  the field name.
     **/
    public String getFieldName() {
        return this.fieldName;
    }
    
    /**
     * @return  the interface class or {@code null} if accessing to the class is not possible.
     */
    public Class<?> getInterfaceClass() {
        return this.interfaceClass;
    }
    
    /**
     * @return  the interface class
     */
    public String getInterfaceClassName() {
        return this.interfaceClassName;
    }
    
    // TODO - add a proper serialization read/write that we not need to worry about the inability to access to the actual class.
    
}
