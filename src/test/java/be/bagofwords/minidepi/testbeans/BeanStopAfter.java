/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-4-9. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;
import org.junit.Assert;

public class BeanStopAfter implements LifeCycleBean {

    @Inject(runtimeDependency = false)
    private BeanStopBefore beanStopBefore;

    public boolean stopped;

    @Override
    public void startBean() {

    }

    @Override
    public void stopBean() {
        Assert.assertTrue(beanStopBefore.stopped);
        stopped = true;
    }
}
