/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;

public class DatabaseService extends TestBean implements LifeCycleBean {

    public void executeStatement() {
        if (beanState != BeanState.STARTED) {
            throw new RuntimeException("Can not execute statement, state is currently " + beanState);
        }
    }

    @Override
    public void startBean() {
        beanState = BeanState.STARTED;
    }

    @Override
    public void stopBean() {
        beanState = BeanState.STOPPED;
    }
}
