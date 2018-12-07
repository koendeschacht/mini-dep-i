/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-12-7. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;
import org.junit.Assert;

import static be.bagofwords.minidepi.testbeans.BeanState.STARTED;
import static be.bagofwords.minidepi.testbeans.BeanState.STOPPED;

public class ParentBeanWithStateTest extends TestBean implements Runnable, LifeCycleBean {

    @Inject
    private ChildBeanWithStateTests childBeanWithStateTests;

    @Override
    public void run() {
        assert childBeanWithStateTests != null;
    }

    @Override
    public void startBean() {
        Assert.assertEquals(STARTED, childBeanWithStateTests.beanState);
        beanState = STARTED;
    }

    @Override
    public void stopBean() {
        Assert.assertEquals(STARTED, childBeanWithStateTests.beanState);
        beanState = STOPPED;
    }
}
