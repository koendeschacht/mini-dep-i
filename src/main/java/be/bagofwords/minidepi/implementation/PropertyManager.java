/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private Properties properties;
    private List<PropertyProvider> propertyProviders = Arrays.asList(new DefaultPropertiesPropertyProvider(),
            new PropertyFilePropertyProvider(), new SocketPropertyProvider(), new SystemPropertiesPropertyProvider());

    public PropertyManager(Map<String, String> config) throws IOException {
        properties = new Properties();
        properties.putAll(config);
        Map<String, String> lastTriggers = new HashMap<>();
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

    public String getProperty(String name, String defaultValue) {
        String value = properties.getProperty(name, null);
        if (value == null) {
            if (defaultValue == null) {
                throw new ApplicationContextException("The configuration option " + name + " was not found");
            } else {
                logger.warn("No configuration found for " + name + ", using default value " + defaultValue);
                value = defaultValue;
            }
        }
        return value;
    }

    public String getProperty(String name) {
        return getProperty(name, null);
    }

}
