package direct.dynobj;

public interface _NormalInterface {
    
    public String value();
    
    
    public default String concate(String suffix) {
        String value = value();
        return value + suffix;
    }
    
    @NonGetter
    public default int lengthOfValue() {
        String value = value();
        return (value == null) ? 0 : value.length();
    }
    
}
