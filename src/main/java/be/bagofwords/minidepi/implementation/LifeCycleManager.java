/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.logging.Log;
import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.util.MappedLists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static be.bagofwords.minidepi.implementation.ApplicationState.*;

public class LifeCycleManager {

    private final Set<Object> stoppedBeans = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStopped = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStarted = new HashSet<>();
    private final Set<LifeCycleBean> startedBeans = new HashSet<>();
    private final MappedLists<Object, Object> runTimeDependencies = new MappedLists<>();
    private final ApplicationContext applicationContext;

    private ApplicationState applicationState;

    public LifeCycleManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.resetState();
    }

    private void resetState() {
        applicationState = BEFORE_START;
        startedBeans.clear();
        stoppedBeans.clear();
    }

    public synchronized void startApplication() {
        if (applicationState != BEFORE_START) {
            throw new ApplicationContextException("Application can not be started because the current state is " + applicationState);
        }
        applicationState = STARTED;
        List<? extends LifeCycleBean> lifeCycleBeans = applicationContext.getBeans(LifeCycleBean.class);
        waitUntilBeansStarted(lifeCycleBeans);
    }

    public synchronized void terminateApplication() {
        if (!terminateWasRequested()) {
            applicationState = TERMINATE_REQUESTED;
            List<? extends LifeCycleBean> lifeCycleBeans = applicationContext.getBeans(LifeCycleBean.class);
            waitUntilBeansStopped(lifeCycleBeans);
            applicationState = TERMINATED;
            Log.i("Application has terminated. Bye!");
        } else {
            Log.w("Application termination requested while application was already terminated");
        }
    }

    public void restartApplication() {
        if (applicationState == STARTED) {
            terminateApplication();
        }
        resetState();
        startApplication();
    }

    public synchronized void waitUntilBeanStopped(Object bean) {
        if (runTimeDependencies.containsKey(bean)) {
            waitUntilBeansStopped(runTimeDependencies.get(bean));
        }
        if (stoppedBeans.contains(bean)) {
            return;
        }
        if (bean instanceof LifeCycleBean) {
            if (beansBeingStopped.contains(bean)) {
                throw new ApplicationContextException("The stop() method of bean " + bean + " was already called. Possible cycle?");
            }
            beansBeingStopped.add((LifeCycleBean) bean);
            // Log.i("Stopping bean " + bean);
            ((LifeCycleBean) bean).stopBean();
            beansBeingStopped.remove(bean);
        }
        stoppedBeans.add(bean);
    }

    public synchronized void waitUntilBeansStopped(List<? extends Object> beans) {
        for (Object bean : beans) {
            waitUntilBeanStopped(bean);
        }
    }

    public synchronized void waitUntilBeanStarted(LifeCycleBean bean) {
        if (applicationState != STARTED) {
            throw new ApplicationContextException("The application was not yet started");
        }
        if (beansBeingStarted.contains(bean)) {
            throw new ApplicationContextException("The stop() method of bean " + bean + " was already called. Possible cycle?");
        }
        if (startedBeans.contains(bean)) {
            return;
        }
        beansBeingStarted.add(bean);
        // Log.i("Starting bean " + bean);
        bean.startBean();
        beansBeingStarted.remove(bean);
        startedBeans.add(bean);
    }

    public <T extends LifeCycleBean> void waitUntilBeansStarted(List<T> beans) {
        for (T bean : beans) {
            waitUntilBeanStarted(bean);
        }
    }

    public void waitUntilTerminated() {
        while (applicationState != TERMINATED) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                if (applicationState != TERMINATE_REQUESTED) {
                    throw new RuntimeException("Received InterruptedException while no termination was requested");
                }
            }
        }
    }

    public boolean applicationIsStarted() {
        return applicationState == STARTED;
    }

    public void ensureBeanCorrectState(LifeCycleBean bean) {
        if (applicationState == STARTED) {
            waitUntilBeanStarted(bean);
        } else if (terminateWasRequested()) {
            waitUntilBeanStopped(bean);
        }
    }

    public boolean terminateWasRequested() {
        return applicationState == TERMINATE_REQUESTED || applicationState == TERMINATED;
    }

    public void registerRuntimeDependency(Object bean, Object dependencyBean) {
        if (terminateWasRequested()) {
            throw new RuntimeException("Terminate was already requested. Please call registerRuntimeDependency(..) on application startup");
        }
        if (isDependent(bean, dependencyBean)) {
            throw new RuntimeException("Cyclic dependency detected between beans " + bean + " and " + dependencyBean);
        }
        runTimeDependencies.get(dependencyBean).add(bean);
    }

    private boolean isDependent(Object bean, Object dependencyBean) {
        List<Object> dependentBeans = new ArrayList<>();
        dependentBeans.add(bean);
        while (!dependentBeans.isEmpty()) {
            Object currBean = dependentBeans.remove(0);
            if (currBean == dependencyBean) {
                return true;
            }
            if (runTimeDependencies.containsKey(currBean)) {
                dependentBeans.addAll(runTimeDependencies.get(currBean));
            }
        }
        return false;
    }

}
