/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.PropertyNotFoundException;
import be.bagofwords.minidepi.properties.PropertyFilePropertyProvider;
import be.bagofwords.minidepi.properties.PropertyProvider;
import be.bagofwords.minidepi.properties.SocketPropertyProvider;
import be.bagofwords.minidepi.properties.SystemPropertiesPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private Properties properties;
    private Map<String, Properties> readPropertyResources = new HashMap<>();

    public PropertyManager(Map<String, String> config) throws IOException {
        properties = new Properties();
        properties.putAll(config);
        Map<String, String> lastTriggers = new HashMap<>();
        List<PropertyProvider> propertyProviders = Arrays.asList(new PropertyFilePropertyProvider(),
                new SocketPropertyProvider(), new SystemPropertiesPropertyProvider());

        boolean finished = false;
        while (!finished) {
            finished = true;
            for (PropertyProvider propertyProvider : propertyProviders) {
                String trigger = propertyProvider.triggerProperty();
                if (trigger == null || properties.getProperty(trigger) != null) {
                    String currValue = trigger == null ? "run-once" : properties.getProperty(trigger);
                    if (!lastTriggers.containsKey(trigger) || !Objects.equals(lastTriggers.get(trigger), currValue)) {
                        propertyProvider.addProperties(properties, logger);
                        lastTriggers.put(trigger, currValue);
                        finished = false;
                    }
                }
            }
        }
    }

    public String getProperty(String name, String orFrom) {
        String value = properties.getProperty(name);
        if (value == null) {
            return getPropertyFromDefaults(orFrom, name);
        } else {
            return value;
        }
    }

    private synchronized String getPropertyFromDefaults(String propertyResource, String name) {
        if (!readPropertyResources.containsKey(propertyResource)) {
            InputStream defaultPropertiesInputStream = this.getClass().getResourceAsStream("/" + propertyResource);
            if (defaultPropertiesInputStream == null) {
                throw new ApplicationContextException("Could not read resource /" + propertyResource);
            } else {
                try {
                    Properties properties = new Properties();
                    properties.load(defaultPropertiesInputStream);
                    readPropertyResources.put(propertyResource, properties);
                    logger.info("Read properties from resource " + propertyResource);
                } catch (IOException e) {
                    throw new ApplicationContextException("Could not load properties from resource /" + propertyResource, e);
                }
            }
        }
        Properties properties = readPropertyResources.get(propertyResource);
        String value = properties.getProperty(name);
        if (value == null) {
            throw new PropertyNotFoundException("The configuration option " + name + " was not found in default properties " + propertyResource);
        }
        return value;
    }

    public String getProperty(String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            throw new PropertyNotFoundException("The configuration option " + name + " was not found");
        }
        return value;
    }

}
