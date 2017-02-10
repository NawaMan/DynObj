package direct.dynobj;

import java.util.Map;
import java.util.TreeMap;

import static direct.dynobj.FieldsHolder.*;

/**
 * Shamelessly inspired by Groovy Expandp.
 * 
 * @author dssb
 */
public class Expando {
    
    private final Map<String, Object> data = new TreeMap<>();
    
    private final FieldsHolder fieldHolder;
    
    public Expando(boolean isJavaBean, boolean returnNullAsValue) {
        FieldsHolder dataHolder = fromMap(data, returnNullAsValue);
        this.fieldHolder = isJavaBean ? javaBean(dataHolder) : dataHolder;
    }
    
    public <T> T as(Class<T> mainInterface, Class<?> ... moreInterfaces) {
        return DynamicObject.of(this.fieldHolder, mainInterface, moreInterfaces);
    }
    
    public Object get(String name) {
        return this.data.get(name);
    }
    
    public Expando put(String name, Object value) {
        this.data.put(name, value);
        return this;
    }
    
}
