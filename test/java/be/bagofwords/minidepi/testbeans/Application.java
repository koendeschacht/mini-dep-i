/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.testbeans;

import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Inject;

import java.util.List;

public class Application extends TestBean implements Runnable, LifeCycleBean {

    @Inject
    private Dao1 dao1;
    @Inject
    private List<Plugin> plugins;

    @Override
    public void run() {
        assert dao1 != null;
        assert plugins != null;
    }

    public List<Plugin> getPlugins() {
        return plugins;
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
