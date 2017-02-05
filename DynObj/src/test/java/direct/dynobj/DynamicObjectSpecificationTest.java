package direct.dynobj;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class DynamicObjectSpecificationTest {
    
    private final FieldsHolder primitiveGetter = name -> {
        if (name.equals("getByte")) {
            return Optional.of((Byte) (byte) 42);
        }
        if (name.equals("getShort")) {
            return Optional.of((Short) (short) 42);
        }
        if (name.equals("getInt")) {
            return Optional.of((Integer) (int) 42);
        }
        if (name.equals("getLong")) {
            return Optional.of((Long) (long) 42);
        }
        if (name.equals("getFloat")) {
            return Optional.of((Float) (float) 42);
        }
        if (name.equals("getDouble")) {
            return Optional.of((Double) (double) 42);
        }
        if (name.equals("getCharacter")) {
            return Optional.of((Character) '4');
        }
        if (name.equals("getBoolean")) {
            return Optional.of((Boolean) true);
        }
        return Optional.of(name);
    };
    
    @Test
    public void testDynamicObject() {
        FieldsHolder     getters = name -> Optional.ofNullable(name);
        _NormalInterface dynObj  = DynamicObject.of(getters, _NormalInterface.class);
        
        Assert.assertTrue(dynObj instanceof DynamicObject);
    }
    
    @Test
    public void testReturnNormalValue() {
        FieldsHolder     getters = name -> Optional.ofNullable(name);
        _NormalInterface dynObj  = DynamicObject.of(getters, _NormalInterface.class);
        
        Assert.assertEquals("value", dynObj.value());
    }
    
    @Test
    public void testNullValue() {
        FieldsHolder     getters = name -> Optional.empty();
        _NormalInterface dynObj  = DynamicObject.of(getters, _NormalInterface.class);
        
        Assert.assertEquals(null, dynObj.value());
    }
    
    @Test
    public void testNoValue() {
        FieldsHolder     getters = name -> null;
        _NormalInterface dynObj  = DynamicObject.of(getters, _NormalInterface.class);
        
        try {
            dynObj.value();
            Assert.fail();
        } catch (MissingFieldException e) {
            Assert.assertEquals(_NormalInterface.class, e.getInterfaceClass());
            Assert.assertEquals("value", e.getFieldName());
            
            String msg = DynamicObjectException.prepareErrorMessage("value", _NormalInterface.class);
            Assert.assertEquals(msg, e.getMessage());
        }
    }
    
    @Test
    public void testErrorMessage() {
        String msg = DynamicObjectException.prepareErrorMessage("value", _NormalInterface.class);
        Assert.assertEquals("direct.dynobj._NormalInterface::value", msg);
    }
    
    @Test
    public void testPrimitiveTypes() {
        FieldsHolder        getters = primitiveGetter;
        _PrimitiveInterface dynObj  = DynamicObject.of(getters, _PrimitiveInterface.class);
        
        Assert.assertEquals((byte)  42, dynObj.getByte());
        Assert.assertEquals((short) 42, dynObj.getShort());
        Assert.assertEquals((int)   42, dynObj.getInt());
        Assert.assertEquals((long)  42, dynObj.getLong());
        Assert.assertEquals((float) 42, dynObj.getFloat(), 0.0);
        Assert.assertEquals((double)42, dynObj.getDouble(), 0.0f);
        Assert.assertEquals(       '4', dynObj.getCharacter());
        Assert.assertEquals(      true, dynObj.getBoolean());
    }
    
    @Test
    public void testMultipleInterfaces() {
        FieldsHolder        getters   = primitiveGetter;
        _NormalInterface    theDynObj = DynamicObject.of(getters, _NormalInterface.class, _PrimitiveInterface.class);
        _PrimitiveInterface antDynObj = (_PrimitiveInterface) theDynObj;
        
        Assert.assertEquals("value", theDynObj.value());
        Assert.assertEquals(42, antDynObj.getInt());
    }
    
    @Test
    public void testMethodWithParameter() {
        FieldsHolder     getters   = name -> Optional.ofNullable(name);
        _NormalInterface theDynObj = DynamicObject.of(getters, _NormalInterface.class);
        Assert.assertEquals("value is the value", theDynObj.concate(" is the value"));
    }
    
    @Test
    public void testNotGetter() {
        FieldsHolder     getters   = name -> Optional.ofNullable(name);
        _NormalInterface theDynObj = DynamicObject.of(getters, _NormalInterface.class);
        Assert.assertEquals(5, theDynObj.lengthOfValue());
    }
    
    @Test
    public void testDefaultImplementation() {
        FieldsHolder               getters   = name -> null;
        _WithDefaultValueInterface theDynObj = DynamicObject.of(getters, _WithDefaultValueInterface.class);
        Assert.assertEquals("whatever", theDynObj.value());
    }
    
}
