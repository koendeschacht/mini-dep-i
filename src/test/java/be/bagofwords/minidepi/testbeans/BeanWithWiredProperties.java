/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-17. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Property;

public class BeanWithWiredProperties {

    @Property(value = "my_property", defaultValue = "default_value")
    private String property;

    public String getProperty() {
        return property;
    }
}
