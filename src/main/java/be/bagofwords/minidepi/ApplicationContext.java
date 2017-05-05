/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.implementation.BeanManager;
import be.bagofwords.minidepi.implementation.LifeCycleManager;
import be.bagofwords.minidepi.implementation.PropertyManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApplicationContext {

    private final LifeCycleManager lifeCycleManager = new LifeCycleManager(this);
    private final BeanManager beanManager = new BeanManager(this, lifeCycleManager);
    private final PropertyManager propertyManager;

    public ApplicationContext() {
        this(Collections.<String, String>emptyMap());
    }

    public ApplicationContext(Map<String, String> config) {
        propertyManager = new PropertyManager(config);
    }

    public ApplicationContext(Properties properties) {
        propertyManager = new PropertyManager(properties);
    }

    public void terminate() {
        lifeCycleManager.terminateApplication();
    }

    public void terminateAsync() {
        new Thread("terminate-application-thread") {
            public void run() {
                terminate();
            }
        }.start();
    }

    public boolean hasWiredFields(Object object) {
        return beanManager.hasWiredFields(object);
    }

    public boolean terminateWasRequested() {
        return lifeCycleManager.terminateWasRequested();
    }

    public synchronized void registerRuntimeDependency(Object bean, Object dependency) {
        lifeCycleManager.registerRuntimeDependency(bean, dependency);
    }

    public void waitUntilTerminated() {
        lifeCycleManager.waitUntilTerminated();
    }

    public void registerBean(Object bean, String... names) {
        beanManager.registerBean(bean, names);
    }

    public void registerBean(Class beanClass, String... names) {
        beanManager.registerBean(beanClass, names);
    }

    public void wireBean(Object bean) {
        beanManager.wireBean(bean);
    }

    public <T> List<T> getBeans(Class<T> type, String... names) {
        return beanManager.getBeans(type, names);
    }

    public <T> T getBean(Class<T> beanType, String... names) {
        return beanManager.getBean(beanType, names);
    }

    public <T> T getBeanIfPresent(Class<T> beanType, String... names) {
        return beanManager.getBeanIfPresent(beanType, names);
    }

    public String getProperty(String name) {
        return propertyManager.getProperty(name);
    }

    public String getProperty(String name, String orFrom) {
        return propertyManager.getProperty(name, orFrom);
    }

    public String getApplicationName() {
        return propertyManager.getProperty("application.name", "mini-dep-i.properties");
    }
}
