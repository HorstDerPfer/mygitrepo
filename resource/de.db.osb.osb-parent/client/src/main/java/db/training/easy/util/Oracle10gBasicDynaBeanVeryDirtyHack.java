/**
 * Copyright 2010, DB Systel GmbH
 */
package db.training.easy.util;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

/**
 * @author BurghardtBornschein
 *
 *  Disables the type check against the meta data of the resultset provided by the oracle jdbc driver.
 *  So e.g. it can be inserted a String derived from a CLOB instead the CLOB itself. 
 */
public class Oracle10gBasicDynaBeanVeryDirtyHack extends BasicDynaBean {

    public Oracle10gBasicDynaBeanVeryDirtyHack(DynaClass dynaClass) {
        super(dynaClass);
    }

    
    public void set(String name, Object value) {

        DynaProperty descriptor = getDynaProperty(name);
        if (value == null) {
            if (descriptor.getType().isPrimitive()) {
                throw new NullPointerException
                        ("Primitive value for '" + name + "'");
            }
        } 
        values.put(name, value);
    }
    
}
