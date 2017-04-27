package trieu.propertyguru.utils.teh.fields;


import trieu.propertyguru.utils.teh.reflect.TEHFieldsReflectionExtractor;

/**
 * TEH fields Factory
 * 
 */
public class TEHFieldsFactory {

    /**
     * singleton
     * 
     * @return
     */
    public static synchronized TEHFields get() {
	    return TEHFieldsReflectionExtractor.getInstance();
    }

}
