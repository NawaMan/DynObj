package direct.dynobj;

import java.util.Map;

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
     * @param fieldHolder     the field holder for this object.
     * @param mainInterface   the main interface that the returning object will implement.
     * @param moreInterfaces  the additional interfaces that the returning object will implement.
     **/
    public static <T> T of(
            FieldsHolder fieldHolder,
            Class<T>     mainInterface,
            Class<?>...  moreInterfaces) {
        T dynObj = defaultCreator.create(fieldHolder, mainInterface, moreInterfaces);
        return dynObj;
    }
    
    /**
     * Create the object.
     * 
     * @param map               the map that hold the data.
     * @param mainInterface   the main interface that the returning object will implement.
     * @param moreInterfaces  the additional interfaces that the returning object will implement.
     **/
    public static <T> T fromMap(
            Map<String, ?> map,
            Class<T>       mainInterface,
            Class<?>...    moreInterfaces) {
        FieldsHolder holder = FieldsHolder.fromMap(map);
        T            dynObj = defaultCreator.create(holder, mainInterface, moreInterfaces);
        return dynObj;
    }
    
    /**
     * Create the object.
     * 
     * @param map                the map that hold the data.
     * @param returnNullAsValue  this flag make the holder return value null as oppose to 'missing field'.
     * @param mainInterface      the main interface that the returning object will implement.
     * @param moreInterfaces     the additional interfaces that the returning object will implement.
     **/
    public static <T> T fromMap(
            Map<String, ?> map,
            boolean        returnNullAsValue,
            Class<T>       mainInterface,
            Class<?>...    moreInterfaces) {
        FieldsHolder holder = FieldsHolder.fromMap(map, returnNullAsValue);
        T            dynObj = defaultCreator.create(holder, mainInterface, moreInterfaces);
        return dynObj;
    }
    
}
