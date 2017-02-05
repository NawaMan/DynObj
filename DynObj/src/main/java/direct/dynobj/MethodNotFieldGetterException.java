package direct.dynobj;

/**
 * This exception is thrown if the field getter method is not a getter (it has a parameter) and no default method. 
 * 
 * @author dssb
 **/
public class MethodNotFieldGetterException extends DynamicObjectException {
    
    private static final long serialVersionUID = -7925691569757349597L;

    /**
     * Constructor.
     * 
     * @param  methodName      the method name.
     * @param  interfaceClass  the interface class.
     **/
    public MethodNotFieldGetterException(
            String    methodName,
            Class<?>  interfaceClass) {
        super(methodName, interfaceClass, null);
    }
    
    /**
     * @return the method name.
     **/
    public String getMethodName() {
        return this.getFieldName();
    }
    
}
