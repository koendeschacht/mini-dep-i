/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.Application;
import be.bagofwords.minidepi.testbeans.Plugin1;
import org.junit.Assert;
import org.junit.Test;

public class WirePluginsTest {

    @Test
    public void wirePluginsTestEmpty() {
        ApplicationContext applicationContext = new ApplicationContext();
        Application application = applicationContext.getBean(Application.class);
        Assert.assertNotNull(application.getPlugins());
        Assert.assertEquals(0, application.getPlugins().size());
    }

    @Test
    public void wirePluginsTestNonEmpty_DeclareClass() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.registerBean(Plugin1.class);
        Application application = applicationContext.getBean(Application.class);
        Assert.assertNotNull(application.getPlugins());
        Assert.assertEquals(1, application.getPlugins().size());
        Assert.assertEquals(Plugin1.class, application.getPlugins().get(0).getClass());
    }

    @Test
    public void wirePluginsTestNonEmpty_DeclareObject() {
        ApplicationContext applicationContext = new ApplicationContext();
        Plugin1 plugin1 = new Plugin1();
        applicationContext.registerBean(plugin1);
        Application application = applicationContext.getBean(Application.class);
        Assert.assertNotNull(application.getPlugins());
        Assert.assertEquals(1, application.getPlugins().size());
        Assert.assertEquals(plugin1, application.getPlugins().get(0));
    }

}
