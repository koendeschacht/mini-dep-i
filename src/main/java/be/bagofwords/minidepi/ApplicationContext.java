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
        this(PropertyConfiguration.create().readDefaultProperties().useEnvironmentVariableIfPresent().use(config));
    }

    public ApplicationContext(PropertyConfiguration propertyConfiguration) {
        try {
            propertyManager = new PropertyManager(propertyConfiguration);
        } catch (IOException exp) {
            throw new ApplicationContextException("Failed to read properties", exp);
        }
    }

    public void start() {
        lifeCycleManager.startApplication();
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

    public void declareBean(Object bean, String... names) {
        beanManager.declareBean(bean, names);
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

    public void declareBean(Class beanClass, String... names) {
        beanManager.declareBean(beanClass, names);
    }

    public String getProperty(String name) {
        return propertyManager.getProperty(name);
    }

    public String getProperty(String name, String defaultValue) {
        return propertyManager.getProperty(name, defaultValue);
    }

    public String getApplicationName() {
        return propertyManager.getProperty("application_name");
    }
}
