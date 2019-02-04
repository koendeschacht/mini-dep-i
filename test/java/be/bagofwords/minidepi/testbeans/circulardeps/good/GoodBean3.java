/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-6. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans.circulardeps.good;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;

public class GoodBean3 implements LifeCycleBean {

    @Inject
    private GoodBean1 goodBean1;

    public boolean wasStarted = false;

    @Override
    public void startBean() {
        wasStarted = true;
    }

    @Override
    public void stopBean() {

    }

    @Override
    public String toString() {
        return "bean3";
    }
}
