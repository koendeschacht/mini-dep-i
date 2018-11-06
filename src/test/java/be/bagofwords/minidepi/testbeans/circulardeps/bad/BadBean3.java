/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2018-11-6. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans.circulardeps.bad;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;

public class BadBean3 implements LifeCycleBean {

    @Inject
    private BadBean1 badBean1;

    @Override
    public void startBean() {

    }

    @Override
    public void stopBean() {

    }

    @Override
    public String toString() {
        return "bean3";
    }
}
