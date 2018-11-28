/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-28. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.annotations.Property;

public class BeanWithTwoProperties {

    @Property(value = "my_property", orFrom = "library.properties")
    private String property1;

    @Property(value = "my.other.property")
    private String property2;

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }
}
