/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-12. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;

public class SlowBean extends TestBean implements LifeCycleBean {

    @Override
    public void startBean() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        beanState = BeanState.STARTED;
    }

    @Override
    public void stopBean() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        beanState = BeanState.STOPPED;
    }
}
