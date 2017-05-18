/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.Application;
import be.bagofwords.minidepi.testbeans.BeanState;
import be.bagofwords.minidepi.testbeans.DatabaseService;
import be.bagofwords.minidepi.testbeans.SlowBean;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LifeCycleTest {

    @Test
    public void testLifeCycle_AutoStart() {
        Map<String, String> config = new HashMap<>();
        config.put("autostart.application", "true");
        ApplicationContext applicationContext = new ApplicationContext();
        Application application = applicationContext.getBean(Application.class);
        DatabaseService databaseService = applicationContext.getBean(DatabaseService.class);
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        Assert.assertEquals(BeanState.STARTED, databaseService.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
    }

    @Test
    public void testLifeCycle_ReStart() {
        ApplicationContext applicationContext = new ApplicationContext();
        Application application = applicationContext.getBean(Application.class);
        DatabaseService databaseService = applicationContext.getBean(DatabaseService.class);
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        Assert.assertEquals(BeanState.STARTED, databaseService.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
        applicationContext.restart();
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        Assert.assertEquals(BeanState.STARTED, databaseService.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
    }

    @Test
    public void testLifeCycle_NoAutoStart() {
        Map<String, String> config = new HashMap<>();
        config.put("autostart.application", "false");
        ApplicationContext applicationContext = new ApplicationContext(config);
        Application application = applicationContext.getBean(Application.class);
        DatabaseService databaseService = applicationContext.getBean(DatabaseService.class);
        Assert.assertEquals(BeanState.INITIALIZED, application.beanState);
        Assert.assertEquals(BeanState.INITIALIZED, databaseService.beanState);
        applicationContext.start();
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        Assert.assertEquals(BeanState.STARTED, databaseService.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
    }

    @Test
    public void testLifeCycleAsync() throws InterruptedException {
        final ApplicationContext applicationContext = new ApplicationContext();
        final Application application = applicationContext.getBean(Application.class);
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                applicationContext.terminate();
            }
        }).start();
        applicationContext.waitUntilTerminated();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
    }

    @Test
    public void testSlowBean() {
        final ApplicationContext applicationContext = new ApplicationContext();
        final SlowBean slowBean = new SlowBean();
        applicationContext.registerBean(slowBean);
        Assert.assertEquals(BeanState.STARTED, slowBean.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, slowBean.beanState);
    }

}
