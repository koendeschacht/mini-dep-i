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

public class LifeCycleTest {

    @Test
    public void testLifeCycle() {
        ApplicationContext applicationContext = new ApplicationContext();
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
        Assert.assertEquals(BeanState.INITIALIZED, application.beanState);
        applicationContext.start();
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

        SlowBean slowBean = applicationContext.getBean(SlowBean.class);
        Assert.assertEquals(BeanState.INITIALIZED, slowBean.beanState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                applicationContext.start();
            }
        }).start();
        applicationContext.waitUntilBeanStarted(slowBean);
        Assert.assertEquals(BeanState.STARTED, slowBean.beanState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                applicationContext.terminate();
            }
        }).start();
        applicationContext.waitUntilBeanStopped(slowBean);
        Assert.assertEquals(BeanState.STOPPED, slowBean.beanState);
    }

}
