/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-17. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Property;

public class BeanWithIntegerProperty {

    @Property(value = "int_property", defaultValue = "42")
    private int property;

    public int getProperty() {
        return property;
    }
}
