/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.logging.Log;
import be.bagofwords.minidepi.PropertyException;
import be.bagofwords.minidepi.properties.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertyManager {

    private Properties properties;
    private Map<String, Properties> readPropertyResources = new HashMap<>();

    public PropertyManager(Properties properties) {
        this.properties = properties;
        init();
    }

    public void init() {
        Map<String, String> lastTriggers = new HashMap<>();
        List<PropertyProvider> propertyProviders = Arrays.asList(new PropertyFilePropertyProvider(),
                new PropertyFilesPropertyProvider(), new SocketPropertyProvider(), new SystemPropertiesPropertyProvider());

        boolean finished = false;
        while (!finished) {
            finished = true;
            for (PropertyProvider propertyProvider : propertyProviders) {
                String trigger = propertyProvider.triggerProperty();
                if (trigger == null || properties.getProperty(trigger) != null) {
                    String currValue = trigger == null ? "run-once" : properties.getProperty(trigger);
                    if (!lastTriggers.containsKey(trigger) || !Objects.equals(lastTriggers.get(trigger), currValue)) {
                        try {
                            propertyProvider.addProperties(properties);
                        } catch (IOException exp) {
                            throw new RuntimeException("Failed to initialize properties. Error by " + propertyProvider, exp);
                        }
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
                throw new PropertyException("Could not read resource /" + propertyResource);
            } else {
                try {
                    Properties properties = new Properties();
                    properties.load(defaultPropertiesInputStream);
                    readPropertyResources.put(propertyResource, properties);
                    Log.i("Read properties from resource " + propertyResource);
                } catch (IOException e) {
                    throw new PropertyException("Could not load properties from resource /" + propertyResource, e);
                }
            }
        }
        Properties properties = readPropertyResources.get(propertyResource);
        String value = properties.getProperty(name);
        if (value == null) {
            throw new PropertyException("The configuration option " + name + " was not found in default properties " + propertyResource);
        }
        return value;
    }

    public String getProperty(String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            throw new PropertyException("The configuration option " + name + " was not found");
        }
        return value;
    }

    public static Properties propertiesFromConfig(Map<String, String> config) {
        Properties properties = new Properties();
        properties.putAll(config);
        return properties;
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }
}
