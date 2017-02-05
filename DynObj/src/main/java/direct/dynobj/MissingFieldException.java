package direct.dynobj;

/**
 * This exception is thrown when the field was needed is not found in the holder.
 * 
 * @author dssb
 **/
public class MissingFieldException extends DynamicObjectException {
    
    private static final long serialVersionUID = -1148199964594393715L;
    
    /**
     * Constructor.
     * 
     * @param  fieldName       the field name.
     * @param  interfaceClass  the interface class.
     **/
    public MissingFieldException(
            String   fieldName,
            Class<?> interfaceClass) {
        super(fieldName, interfaceClass, null);
    }
    
}
