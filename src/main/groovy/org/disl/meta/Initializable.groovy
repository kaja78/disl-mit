package org.disl.meta;

/**
 * Marks class for post construct initialisation. 
 * DISL model objects should be constructed by MetaFactory in runtime.
 * When the class implements Initializable interface, it's init() method is called after new instance is created.
 * */
public interface Initializable {
    /**
     * Implement post construct initialization.
     * */
    public void init();
}
