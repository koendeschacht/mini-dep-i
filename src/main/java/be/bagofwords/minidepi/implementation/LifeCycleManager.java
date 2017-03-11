/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.LifeCycleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LifeCycleManager {

    private static final Logger logger = LoggerFactory.getLogger(LifeCycleManager.class);

    private final Set<LifeCycleBean> stoppedBeans = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStopped = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStarted = new HashSet<>();
    private final Set<LifeCycleBean> startedBeans = new HashSet<>();
    private final ApplicationContext applicationContext;

    private boolean applicationWasTerminated = false;
    private boolean applicationWasStarted = false;

    public LifeCycleManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public synchronized void terminateApplication() {
        if (!applicationWasTerminated) {
            List<? extends LifeCycleBean> lifeCycleBeans = applicationContext.getBeans(LifeCycleBean.class);
            for (LifeCycleBean bean : lifeCycleBeans) {
                waitUntilBeanStopped(bean);
            }
            applicationWasTerminated = true;
            logger.info("Application has terminated. Bye!");
        } else {
            logger.warn("Application termination requested while applications was already terminated");
        }
    }

    public void startApplication() {
        if (!applicationWasStarted) {
            List<? extends LifeCycleBean> lifeCycleBeans = applicationContext.getBeans(LifeCycleBean.class);
            for (LifeCycleBean bean : lifeCycleBeans) {
                waitUntilBeanStarted(bean);
            }
            applicationWasStarted = true;
        } else {
            logger.info("Application was already started...");
        }
    }

    public synchronized void waitUntilBeanStopped(LifeCycleBean bean) {
        if (beansBeingStopped.contains(bean)) {
            throw new ApplicationContextException("The stop() method of bean " + bean + " was already called. Possible cycle?");
        }
        if (stoppedBeans.contains(bean)) {
            return;
        }
        beansBeingStopped.add(bean);
        logger.info("Stopping bean " + bean);
        bean.stopBean();
        beansBeingStopped.remove(bean);
        stoppedBeans.add(bean);
    }

    public synchronized void waitUntilBeanStarted(LifeCycleBean bean) {
        if (beansBeingStarted.contains(bean)) {
            throw new ApplicationContextException("The stop() method of bean " + bean + " was already called. Possible cycle?");
        }
        if (startedBeans.contains(bean)) {
            return;
        }
        beansBeingStarted.add(bean);
        logger.info("Starting bean " + bean);
        bean.startBean();
        beansBeingStarted.remove(bean);
        startedBeans.add(bean);
    }

    public void waitUntilTerminated() throws InterruptedException {
        while (!applicationWasTerminated) {
            Thread.sleep(100);
        }
    }

    public boolean applicationWasTerminated() {
        return applicationWasTerminated;
    }

    public boolean applicationWasStarted() {
        return applicationWasStarted;
    }

    public void ensureBeanCorrectState(LifeCycleBean bean) {
        if (applicationWasStarted() && !applicationWasTerminated()) {
            waitUntilBeanStarted(bean);
        }
        if (applicationWasTerminated()) {
            waitUntilBeanStopped(bean);
        }
    }
}
