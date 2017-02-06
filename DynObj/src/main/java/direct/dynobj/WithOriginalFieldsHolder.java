package direct.dynobj;

/**
 * This interface will allows access to the source fields holder of the dynamic object.
 * 
 * @author dssb
 **/
public interface WithOriginalFieldsHolder {
    
    /**
     * @return the original field holder.
     * 
     * NOTE: the name of the method is intentionally made unconventional so that it will not conflict with whatever
     *         people want to use.
     **/
    public FieldsHolder _originalFieldHolder();
    
}
