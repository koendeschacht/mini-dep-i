/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanWithProperties;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanWithPropertyTest {

    @Test
    public void testBeanWithPropertyDefaultValue() {
        BeanWithProperties beanWithProperties = new ApplicationContext(PropertyConfiguration.create()).getBean(BeanWithProperties.class);
        Assert.assertEquals("default_value", beanWithProperties.getProperty());
    }

    @Test
    public void testBeanWithPropertyDefaultProperties() {
        BeanWithProperties beanWithProperties = new ApplicationContext(PropertyConfiguration.create().readDefaultProperties()).getBean(BeanWithProperties.class);
        Assert.assertEquals("value_from_default.properties", beanWithProperties.getProperty());
    }

    @Test
    public void testBeanWithPropertyConfiguredValue() {
        Map<String, String> config = new HashMap<>();
        config.put("my_property", "some_value");
        BeanWithProperties beanWithProperties = new ApplicationContext(PropertyConfiguration.create().use(config)).getBean(BeanWithProperties.class);
        Assert.assertEquals("some_value", beanWithProperties.getProperty());
    }

}
