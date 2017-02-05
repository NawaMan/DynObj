package direct.dynobj;

import static direct.dynobj.DynamicObjectUsecaseTest.Data.data;
import static java.util.Collections.singletonMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class DynamicObjectUsecaseTest {
    
    public static interface Data {
        
        public static final Field<Data, String> data = Data::data;
        
        public String data();
        
    }
    
    @Test
    public void testFromMaps() {
        List<Map<String, String>> rows = Arrays.asList(
                singletonMap("data", "row1"),
                singletonMap("data", "row2"),
                singletonMap("data", "row3"),
                singletonMap("data", "row4")
        );
        
        String result =
                rows.stream()
                    .map(row->DynamicObject.fromMap(row, Data.class))
                    .filter(data.notEqualsTo("row2"))
                    .map(data)
                    .collect(Collectors.joining(","));
        Assert.assertEquals("row1,row3,row4", result);
    }
    
}
