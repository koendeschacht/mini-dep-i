/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.Plugin1;
import be.bagofwords.minidepi.testbeans.Plugin1Subclass;
import org.junit.Assert;
import org.junit.Test;

public class SimpleWiringTests {

    @Test
    public void wireCreatedObject() {
        ApplicationContext applicationContext = new ApplicationContext();
        Plugin1 myPlugin = new Plugin1();
        applicationContext.declareBean(myPlugin);
        Assert.assertNotNull(myPlugin.getDao1());
        Assert.assertEquals(3, applicationContext.getBeans(Object.class).size());
    }

    @Test
    public void wireCreatedObjectSubclass() {
        ApplicationContext applicationContext = new ApplicationContext();
        Plugin1Subclass myPlugin = new Plugin1Subclass();
        applicationContext.declareBean(myPlugin);
        Assert.assertNotNull(myPlugin.getDao1());
        Assert.assertNotNull(myPlugin.getDao2());
        Assert.assertEquals(4, applicationContext.getBeans(Object.class).size());
    }
}
