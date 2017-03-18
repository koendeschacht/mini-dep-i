/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.annotations.Inject;

public class BeanWithPropertiesFromApplicationContext {

    private String property ;

    public BeanWithPropertiesFromApplicationContext(ApplicationContext applicationContext) {
        property = applicationContext.getProperty("my_property", "default_value");
    }

    public String getProperty() {
        return property;
    }
}
