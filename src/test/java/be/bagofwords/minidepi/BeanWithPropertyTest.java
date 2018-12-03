/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.BeanWithIntegerProperty;
import be.bagofwords.minidepi.testbeans.BeanWithPropertiesFromApplicationContext;
import be.bagofwords.minidepi.testbeans.BeanWithTwoProperties;
import be.bagofwords.minidepi.testbeans.BeanWithWiredProperties;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanWithPropertyTest {

    @Test
    public void testBeanWithPropertyDefaultValue() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithPropertiesFromApplicationContext bean1 = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("default_from_library", bean1.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("default_from_library", bean2.getProperty());
    }

    @Test
    public void testBeanWithPropertyConfiguredValue() {
        Map<String, String> config = new HashMap<>();
        config.put("my_property", "some_value");
        ApplicationContext applicationContext = new ApplicationContext(config);
        BeanWithPropertiesFromApplicationContext bean = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("some_value", bean.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("some_value", bean2.getProperty());
    }

    @Test
    public void testBeanWithPropertyFromFile() {
        System.setProperty("property.file", "src/test/resources/other.properties");
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithPropertiesFromApplicationContext bean1 = applicationContext.getBean(BeanWithPropertiesFromApplicationContext.class);
        Assert.assertEquals("value_from_other.properties", bean1.getProperty());
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("value_from_other.properties", bean2.getProperty());
        System.clearProperty("property.file");
    }

    @Test
    public void testBeanWithTransitiveProperties() {
        System.setProperty("property.file", "src/test/resources/file_with_transitive_properties.properties");
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithTwoProperties bean2 = applicationContext.getBean(BeanWithTwoProperties.class);
        Assert.assertEquals("value_from_other.properties", bean2.getProperty1());
        Assert.assertEquals("value_for_other_property", bean2.getProperty2());
        System.clearProperty("property.file");
    }

    @Test
    public void testBeanWithOverwrittenTransitiveProperties() {
        System.setProperty("property.files", "src/test/resources/overwritten.properties, src/test/resources/not_overwritten.properties");
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithWiredProperties bean2 = applicationContext.getBean(BeanWithWiredProperties.class);
        Assert.assertEquals("my_value", bean2.getProperty());
        System.clearProperty("property.files");
    }

    @Test
    public void testBeanWithPropertiesFromMultipleFiles() {
        System.setProperty("property.files", "src/test/resources/other.properties, src/test/resources/yet_other.properties");
        ApplicationContext applicationContext = new ApplicationContext();
        BeanWithTwoProperties bean2 = applicationContext.getBean(BeanWithTwoProperties.class);
        Assert.assertEquals("value_from_other.properties", bean2.getProperty1());
        Assert.assertEquals("value_for_other_property", bean2.getProperty2());
        System.clearProperty("property.files");
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
        ApplicationContext context = new ApplicationContext(config);
        Assert.assertEquals("some_value", context.getProperty("my_property"));
    }

}
