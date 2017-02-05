package direct.dynobj;

/**
 * This exception is thrown when there is an exception thrown why attempting to get the value.
 * 
 * @author dssb
 **/
public class FieldGetExecutionException extends DynamicObjectException {
    
    private static final long serialVersionUID = 3884468406756535803L;

    /**
     * Constructor.
     * 
     * @param  fieldName       the field name.
     * @param  interfaceClass  the interface class.
     * @param  cause           the root cause of the exception.
     **/
    public FieldGetExecutionException(
            String    fieldName,
            Class<?>  interfaceClass,
            Throwable cause) {
        super(fieldName, interfaceClass, cause);
    }
    
}
