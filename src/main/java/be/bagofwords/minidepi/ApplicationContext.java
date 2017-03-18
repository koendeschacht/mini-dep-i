/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import be.bagofwords.minidepi.implementation.BeanManager;
import be.bagofwords.minidepi.implementation.LifeCycleManager;
import be.bagofwords.minidepi.implementation.PropertyManager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final LifeCycleManager lifeCycleManager = new LifeCycleManager(this);
    private final BeanManager beanManager = new BeanManager(this, lifeCycleManager);
    private final PropertyManager propertyManager;

    public ApplicationContext() {
        this(Collections.<String, String>emptyMap());
    }

    public ApplicationContext(Map<String, String> config) {
        try {
            propertyManager = new PropertyManager(config);
        } catch (IOException exp) {
            throw new ApplicationContextException("Failed to read properties", exp);
        }
    }

    public void terminate() {
        lifeCycleManager.terminateApplication();
    }

    public synchronized void waitUntilBeanStopped(LifeCycleBean bean) {
        lifeCycleManager.waitUntilBeanStopped(bean);
    }

    public synchronized void waitUntilBeanStarted(LifeCycleBean bean) {
        lifeCycleManager.waitUntilBeanStarted(bean);
    }

    public void waitUntilTerminated() throws InterruptedException {
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

    public String getProperty(String name, String defaultValue) {
        return propertyManager.getProperty(name, defaultValue);
    }

    public String getApplicationName() {
        return propertyManager.getProperty("application_name", "some_application");
    }
}
