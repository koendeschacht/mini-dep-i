/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-12-7. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;
import org.junit.Assert;

import static be.bagofwords.minidepi.testbeans.BeanState.INITIALIZED;
import static be.bagofwords.minidepi.testbeans.BeanState.STOPPED;

public class ChildBeanWithStateTests extends TestBean implements LifeCycleBean {

    @Inject(ensureStarted = false)
    private ParentBeanWithStateTest parentBeanWithStateTest;

    @Override
    public void startBean() {
        Assert.assertEquals(INITIALIZED, parentBeanWithStateTest.beanState);
        beanState = BeanState.STARTED;
    }

    @Override
    public void stopBean() {
        Assert.assertEquals(STOPPED, parentBeanWithStateTest.beanState);
        beanState = BeanState.STOPPED;
    }
}
