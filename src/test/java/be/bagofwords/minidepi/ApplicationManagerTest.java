/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.annotations.Inject;
import be.bagofwords.minidepi.testbeans.BeanState;
import be.bagofwords.minidepi.testbeans.Dao1;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationManagerTest {

    static boolean runnableRan;
    static boolean runnableStopped;

    @Test
    public void testApplicationManagerWithClass() {
        runnableRan = false;
        runnableStopped = false;
        ApplicationManager.run(MyRunnable.class);
        Assert.assertTrue(runnableRan);
        Assert.assertTrue(runnableStopped);
    }

    @Test
    public void testApplicationManagerWithObject() {
        runnableRan = false;
        runnableStopped = false;
        MyRunnable myRunnable = new MyRunnable();
        ApplicationManager.run(myRunnable);
        Assert.assertTrue(runnableRan);
        Assert.assertTrue(runnableStopped);
    }

    public static class MyRunnable implements LifeCycleBean, Runnable {

        private BeanState beanState;

        @Inject
        private Dao1 dao1;

        public MyRunnable() {
            beanState = BeanState.INITIALIZED;
        }

        public void run() {
            try {
                assert beanState == BeanState.STARTED;
                assert dao1 != null;
                runnableRan = true;
            } catch (AssertionError error) {
                System.out.println("Test failed!");
                error.printStackTrace();
            }
        }

        @Override
        public void startBean() {
            beanState = BeanState.STARTED;
        }

        @Override
        public void stopBean() {
            beanState = BeanState.STOPPED;
            runnableStopped = true;
        }
    }

}
