package direct.dynobj;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This interface allows a way to make accessing to the field easy.
 * 
 * @author dssb
 **/
public interface Field<T, F> extends Function<T, F> {
    
    
    /**
     * Predicate to check if this value of this field equals to the given value.
     * 
     * @param expected  the expected value.
     * @return  the predicate.
     **/
    public default Predicate<T> equalsTo(F expected) {
        return instance->{
            F theValue = apply(instance);
            if (theValue == expected) {
                return true;
            }
            if (theValue == null) {
                return false;
            }
            if (expected == null) {
                return false;
            }
            boolean compareResult = theValue.equals(expected);
            return compareResult;
        };
    }
    
    /**
     * Predicate to check if this value of this field does not equal to the given value.
     * 
     * @param expected  the expected value.
     * @return  the predicate.
     **/
    public default Predicate<T> notEqualsTo(F expected) {
        return instance->{
            F theValue = apply(instance);
            if (theValue == expected) {
                return false;
            }
            if (theValue == null) {
                return true;
            }
            if (expected == null) {
                return true;
            }
            boolean compareResult = theValue.equals(expected);
            return !compareResult;
        };
    }
    
    /**
     * Predicate to check if this value of this field is in the given collection.
     * 
     * @param collection  the collection.
     * @return  the predicate.
     **/
    public default Predicate<T> in(Collection<T> collection) {
        return instance->{
            if (collection == null) {
                return false;
            }
            return collection.contains(apply(instance));
        };
    }
    
    /**
     * Predicate to check if this value of this field is not in the given collection.
     * 
     * @param collection  the collection.
     * @return  the predicate.
     **/
    public default Predicate<T> notIn(Collection<T> collection) {
        return instance->{
            if (collection == null) {
                return true;
            }
            return !collection.contains(apply(instance));
        };
    }
    
    /**
     * Predicate to check if this value of this field is in the given collection.
     * 
     * @param instances  the instance value.
     * @return  the predicate.
     **/
    public default Predicate<T> in(@SuppressWarnings("unchecked") T ... instances) {
        return instance->{
            if (instances == null) {
                return false;
            }
            for (int i = 0; i < instances.length; i++) {
                T each = instances[i];
                if (Objects.equals(each, instance)) {
                    return true;
                }
            }
            return false;
        };
    }
    
    /**
     * Predicate to check if this value of this field is not in the given collection.
     * 
     * @param instances  the instance value.
     * @return  the predicate.
     **/
    public default Predicate<T> notIn(@SuppressWarnings("unchecked") T ... instances) {
        return instance->{
            if (instances == null) {
                return false;
            }
            for (int i = 0; i < instances.length; i++) {
                T each = instances[i];
                if (Objects.equals(each, instance)) {
                    return false;
                }
            }
            return true;
        };
    }
    
}