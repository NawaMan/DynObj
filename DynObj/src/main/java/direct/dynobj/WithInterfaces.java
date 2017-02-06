package direct.dynobj;

import java.util.Collection;

/**
 * This interface will allows access to the collection of all the interfaces a dynamic object implements.
 * 
 * @author dssb
 **/
public interface WithInterfaces {

    /**
     * @return the collections of the interfaces.
     * 
     * NOTE: the name of the method is intentionally made unconventional so that it will not conflict with whatever
     *         people want to use.
     **/
    public Collection<Class<?>> _interfaces();
    
}
