/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanWithIntegerProperty;
import be.bagofwords.minidepi.testbeans.BeanWithPropertiesFromApplicationContext;
import be.bagofwords.minidepi.testbeans.BeanWithWiredProperties;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanWithPropertyTest {

    @Test
    public void testBeanWithPropertyDefaultValue() {
        ApplicationContext applicationContext = new ApplicationContext(PropertyConfiguration.create());
        BeanWithPropertiesFromApplicationContext bean1 = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("default_value", bean1.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("default_value", bean2.getProperty());
    }

    @Test
    public void testBeanWithPropertyDefaultProperties() {
        ApplicationContext applicationContext = new ApplicationContext(PropertyConfiguration.create().readDefaultProperties());
        BeanWithPropertiesFromApplicationContext bean1 = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("value_from_default.properties", bean1.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("value_from_default.properties", bean2.getProperty());
    }

    @Test
    public void testBeanWithPropertyConfiguredValue() {
        Map<String, String> config = new HashMap<>();
        config.put("my_property", "some_value");
        ApplicationContext applicationContext = new ApplicationContext(PropertyConfiguration.create().use(config));
        BeanWithPropertiesFromApplicationContext bean = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("some_value", bean.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("some_value", bean2.getProperty());
    }

    @Test
    public void testBeanWithIntegerProperty() {
        BeanWithIntegerProperty bean = new ApplicationContext().getBean(BeanWithIntegerProperty.class);
        Assert.assertEquals(42, bean.getProperty());
    }

    @Test
    public void testGettingPropertyFromContext() {
        Map<String, String> config = new HashMap<>();
        config.put("my_property", "some_value");
        ApplicationContext context = new ApplicationContext(PropertyConfiguration.create().use(config));
        Assert.assertEquals("some_value", context.getProperty("my_property"));
    }

    @Test
    public void testGettingApplicationName() {
        Map<String, String> config = new HashMap<>();
        config.put("application_name", "cool_app");
        ApplicationContext context = new ApplicationContext(PropertyConfiguration.create().use(config));
        Assert.assertEquals("cool_app", context.getApplicationName());
    }

}
