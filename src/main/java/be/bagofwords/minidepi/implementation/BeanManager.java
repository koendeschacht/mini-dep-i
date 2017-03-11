/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.LifeCycleBean;
import be.bagofwords.minidepi.annotations.Bean;
import be.bagofwords.minidepi.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanManager {

    private static final Logger logger = LoggerFactory.getLogger(BeanManager.class);

    private final ApplicationContext applicationContext;
    private final LifeCycleManager lifeCycleManager;
    private final List<QualifiedBean> beans = new ArrayList<>();
    private final Set<Class> beansBeingCreated = new HashSet<>();

    public BeanManager(ApplicationContext applicationContext, LifeCycleManager lifeCycleManager) {
        this.applicationContext = applicationContext;
        this.lifeCycleManager = lifeCycleManager;
        registerNewBean(applicationContext);
    }

    private <T> void registerNewBean(T bean) {
        Set<String> qualifiers = getQualifiers(bean);
        beans.add(new QualifiedBean(qualifiers, bean));
        if (bean instanceof LifeCycleBean) {
            lifeCycleManager.ensureBeanCorrectState((LifeCycleBean) bean);
        }
    }

    private <T> Set<String> getQualifiers(T bean) {
        Set<String> qualifiers = new HashSet<>();
        Annotation[] annotations = bean.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.getClass().equals(Bean.class)) {
                Bean beanAnnotation = (Bean) annotation;
                qualifiers.add(beanAnnotation.name());
            }
        }
        return qualifiers;
    }

    public <T> List<T> getBeans(Class<T> interfaceClass) {
        return getBeans(interfaceClass, null);
    }

    public <T> List<T> getBeans(Class<T> interfaceClass, String name) {
        List<T> result = new ArrayList<>();
        for (QualifiedBean qualifiedBean : beans) {
            if (interfaceClass.isAssignableFrom(qualifiedBean.bean.getClass())) {
                if (name == null || qualifiedBean.qualifiers.contains(name)) {
                    result.add(interfaceClass.cast(qualifiedBean.bean));
                }
            }
        }
        return result;
    }

    public <T> T getBeanIfPresent(Class<T> interfaceClass) {
        List<T> beans = getBeans(interfaceClass);
        return returnBeanOrNullOrError(interfaceClass, beans);
    }

    private <T> T returnBeanOrNullOrError(Class<T> interfaceClass, List<T> beans) {
        if (beans.size() == 1) {
            return beans.get(0);
        } else if (beans.size() == 0) {
            return null;
        } else {
            throw new ApplicationContextException("Found " + beans.size() + " beans of type " + interfaceClass);
        }
    }

    public <T> T getBeanIfPresent(Class<T> interfaceClass, String name) {
        List<T> beans = getBeans(interfaceClass, name);
        if (beans.size() == 1) {
            return beans.get(0);
        } else if (beans.size() == 0) {
            return null;
        } else {
            throw new ApplicationContextException("Found " + beans.size() + " beans of type " + interfaceClass + " and name " + name);
        }
    }

    public <T> T getBean(Class<T> interfaceClass, String name) {
        List<T> matchingBeans = getBeans(interfaceClass, name);
        if (matchingBeans.size() == 1) {
            return matchingBeans.get(0);
        } else {
            createBean(interfaceClass);
            matchingBeans = getBeans(interfaceClass, name);
            if (matchingBeans.size() == 1) {
                return matchingBeans.get(0);
            } else {
                throw new ApplicationContextException("Could not find bean with type " + interfaceClass + " and name " + name);
            }
        }
    }

    public <T> T getBean(Class<T> interfaceClass) {
        List<T> matchingBeans = getBeans(interfaceClass);
        if (matchingBeans.size() == 1) {
            return matchingBeans.get(0);
        } else {
            return createBean(interfaceClass);
        }
    }

    public <T> void declareBean(Class<T> beanClass) {
        getBean(beanClass);
    }

    private <T> T createBean(Class<T> beanClass) {
        if (beansBeingCreated.contains(beanClass)) {
            throw new ApplicationContextException("Dependency while creating bean " + beanClass);
        }
        beansBeingCreated.add(beanClass);
        try {
            T newBean = constructBean(beanClass);
            declareBean(newBean);
            logger.info("Created bean " + newBean);
            return newBean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationContextException("Failed to create bean " + beanClass, e);
        } finally {
            beansBeingCreated.remove(beanClass);
        }
    }

    private <T> T constructBean(Class<T> beanClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            //Constructor with a single argument, the application context?
            return beanClass.getConstructor(ApplicationContext.class).newInstance(applicationContext);
        } catch (NoSuchMethodException exp) {
            //OK, continue
        }
        //Constructor with @Inject annotation?
        for (Constructor<?> constructor : beanClass.getConstructors()) {
            if (hasInjectAnnotation(constructor.getAnnotations())) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];
                for (int i = 0; i < args.length; i++) {
                    args[i] = getBean(parameterTypes[i]);
                }
                return (T) constructor.newInstance(args);
            }
        }
        //Constructor without any arguments?
        try {
            return beanClass.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ApplicationContextException("Could not create bean of type " + beanClass.getCanonicalName() + ". " +
                    "Need at least one constructor that has either (1) no parameters, or (2) a single parameter of type ApplicationContext, or (3) the @Inject annotation");
        }
    }

    public void declareBean(Object bean) {
        try {
            registerNewBean(bean);
            Class<?> currClass = bean.getClass();
            while (!currClass.equals(Object.class)) {
                wireFields(bean, currClass);
                currClass = currClass.getSuperclass();
            }
        } catch (IllegalAccessException exp) {
            throw new ApplicationContextException("Failed to wire bean " + bean, exp);
        }
    }

    private void wireFields(Object bean, Class<?> beanClass) throws IllegalAccessException {
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            if (hasInjectAnnotation(field.getAnnotations())) {
                field.setAccessible(true);
                if (field.get(bean) == null) {
                    Class<?> fieldType = field.getType();
                    Object newValue;
                    if (fieldType == List.class) {
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            Type[] genericTypeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
                            if (genericTypeArgs.length == 1) {
                                newValue = getBeans((Class) genericTypeArgs[0]);
                            } else {
                                throw new ApplicationContextException("Received multiple types for List???");
                            }
                        } else {
                            throw new ApplicationContextException("Could not determine generic type of field " + field.getName() + " in " + bean);
                        }
                    } else {
                        newValue = getBean(fieldType);
                    }
                    field.set(bean, newValue);
                }
            }
        }
    }

    private boolean hasInjectAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Inject.class)) {
                return true;
            }
            String name = annotation.getClass().getCanonicalName();
            if (name != null && name.equals("javax.inject.Inject")) {
                return true;
            }
        }
        return false;
    }

}
