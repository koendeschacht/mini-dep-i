/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.testbeans.*;
import be.bagofwords.minidepi.testbeans.circulardeps.bad.BadBean1;
import be.bagofwords.minidepi.testbeans.circulardeps.bad.BadBean2;
import be.bagofwords.minidepi.testbeans.circulardeps.bad.BadBean3;
import be.bagofwords.minidepi.testbeans.circulardeps.good.GoodBean1;
import be.bagofwords.minidepi.testbeans.circulardeps.good.GoodBean2;
import be.bagofwords.minidepi.testbeans.circulardeps.good.GoodBean3;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Assert.assertTrue(applicationContext.isStarted());
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
        Assert.assertFalse(applicationContext.isStarted());
    }

    @Test
    public void testCorrecOrderOfBeanLifecycleStates() {
        ApplicationContext applicationContext = new ApplicationContext();
        ParentBeanWithStateTest application = applicationContext.getBean(ParentBeanWithStateTest.class);
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
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
        Assert.assertFalse(applicationContext.isStarted());
        applicationContext.start();
        Assert.assertEquals(BeanState.STARTED, application.beanState);
        Assert.assertEquals(BeanState.STARTED, databaseService.beanState);
        Assert.assertTrue(applicationContext.isStarted());
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, application.beanState);
        Assert.assertEquals(BeanState.STOPPED, databaseService.beanState);
        Assert.assertFalse(applicationContext.isStarted());
    }

    @Test
    public void testLifeCycleAsync() {
        final ApplicationContext applicationContext = new ApplicationContext();
        Application application = applicationContext.getBean(Application.class);
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
        ApplicationContext applicationContext = new ApplicationContext();
        SlowBean slowBean = new SlowBean();
        applicationContext.registerBean(slowBean);
        Assert.assertEquals(BeanState.STARTED, slowBean.beanState);
        applicationContext.terminate();
        Assert.assertEquals(BeanState.STOPPED, slowBean.beanState);
    }

    @Test
    public void testCyclicDependencies_cyclic_dependencies_between_lifecycle_beans() {
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                new ApplicationContext().registerBean(new BadBean3());
            }
        }).hasMessage("Cyclic dependency detected between beans bean3 and bean1. Consider setting one of the inject annotations with ensureStarted=false");

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                new ApplicationContext().registerBean(new BadBean1());
            }
        }).hasMessage("Cyclic dependency detected between beans bean1 and bean2. Consider setting one of the inject annotations with ensureStarted=false");

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() {
                new ApplicationContext().registerBean(new BadBean2());
            }
        }).hasMessage("Cyclic dependency detected between beans bean2 and bean3. Consider setting one of the inject annotations with ensureStarted=false");
    }

    @Test
    public void testCyclicDependencies_cyclic_dependencies_between_non_lifecycle_beans() {
        ApplicationContext applicationContext1 = new ApplicationContext();
        applicationContext1.registerBean(new GoodBean1());
        assertThatApplicationContextWasStartedAndCanBeStopped(applicationContext1);

        ApplicationContext applicationContext2 = new ApplicationContext();
        applicationContext2.registerBean(new GoodBean2());
        assertThatApplicationContextWasStartedAndCanBeStopped(applicationContext2);

        ApplicationContext applicationContext3 = new ApplicationContext();
        applicationContext3.registerBean(new GoodBean3());
        assertThatApplicationContextWasStartedAndCanBeStopped(applicationContext3);
    }

    public void assertThatApplicationContextWasStartedAndCanBeStopped(ApplicationContext applicationContext) {
        assertThat(applicationContext.getBean(GoodBean3.class).wasStarted).isTrue();
        applicationContext.terminate();
    }

}
