package direct.dynobj;

import java.util.Map;
import java.util.Optional;

/**
 * This interface is provide the field value.
 * 
 * @author dssb
 **/
@FunctionalInterface
public interface FieldsHolder {
    
    /**
     * @return {@code null}                      if the field with the name does not exists.
     *         {@link Optional}.ofNullable(null) if the value is actually null.
     *         the value of the field of the given name.
     **/
    public Optional<Object> get(String name) throws Throwable;
    
    
    

    /**
     * This helpful method make creating FieldsHolder from map easy.
     * This holder will make the dynamic object that is return null for missing field as oppose to 'missing field'.
     * 
     * @param map  the map that contains the object.
     * @return  the fields holder.
     **/
    public static FieldsHolder fromMap(Map<String, ?> map) {
        return fromMap(map, true);
    }
    /**
     * This helpful method make creating FieldsHolder from map easy.
     * 
     * @param map                the map that contains the object.
     * @param returnNullAsValue  this flag make the holder return value null as oppose to 'missing field'.
     * @return  the fields holder.
     **/
    public static FieldsHolder fromMap(
            Map<String, ?> map,
            boolean        returnNullAsValue) {
        return fieldName->{
            if (!map.containsKey(fieldName)) {
                if (returnNullAsValue) {
                    return Optional.empty();
                } else {
                    return null;
                }
            }
            return Optional.ofNullable(map.get(fieldName));
        };
    }
    
}
