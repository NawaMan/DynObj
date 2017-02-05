package direct.dynobj;

/**
 * This interface marks that an object is a dynamic object.
 * It also provides a more natural way create the object.
 * 
 * @author dssb
 **/
public interface DynamicObject {
    
    // TODO - at some point, we may want to allow this to be overwrite easily -- like to support dependency injection.
    /**
     * This is the dynamic object creator used.
     **/
    public static final DynmicObjectCreator defaultCreator = new DynmicObjectCreator();

    /**
     * Create the object.
     * 
     * @param fieldHolder    the field holder for this object.
     * @param mainInterface  the main interface that the returning object will implement.
     * @param moreInterface  the additional interfaces that the returning object will implement.
     **/
    public static <T> T of(
            FieldsHolder fieldHolder,
            Class<T>     mainInterface,
            Class<?>...  moreInterface) {
        T dynObj = defaultCreator.create(fieldHolder, mainInterface, moreInterface);
        return dynObj;
    }
    
}
