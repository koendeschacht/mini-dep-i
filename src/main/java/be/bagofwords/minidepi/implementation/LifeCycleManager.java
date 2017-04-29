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

public class LifeCycleManager {

    private final Set<Object> stoppedBeans = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStopped = new HashSet<>();
    private final Set<LifeCycleBean> beansBeingStarted = new HashSet<>();
    private final Set<LifeCycleBean> startedBeans = new HashSet<>();
    private final MappedLists<Object, Object> runTimeDependencies = new MappedLists<>();
    private final ApplicationContext applicationContext;

    private boolean applicationWasTerminated = false;
    private boolean terminatedRequested = false;

    public LifeCycleManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public synchronized void terminateApplication() {
        if (!applicationWasTerminated) {
            terminatedRequested = true;
            List<? extends LifeCycleBean> lifeCycleBeans = applicationContext.getBeans(LifeCycleBean.class);
            for (LifeCycleBean bean : lifeCycleBeans) {
                waitUntilBeanStopped(bean);
            }
            applicationWasTerminated = true;
            Log.i("Application has terminated. Bye!");
        } else {
            Log.w("Application termination requested while application was already terminated");
        }
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
            Log.i("Stopping bean " + bean);
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
        while (!applicationWasTerminated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                if (!terminatedRequested) {
                    throw new RuntimeException("Received InterruptedException while no termination was requested");
                }
            }
        }
    }

    public boolean applicationWasTerminated() {
        return applicationWasTerminated;
    }

    public void ensureBeanCorrectState(LifeCycleBean bean) {
        if (!applicationWasTerminated()) {
            waitUntilBeanStarted(bean);
        }
        if (applicationWasTerminated()) {
            waitUntilBeanStopped(bean);
        }
    }

    public boolean terminateWasRequested() {
        return terminatedRequested;
    }

    public void registerRuntimeDependency(Object bean, Object dependencyBean) {
        if (terminatedRequested) {
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
