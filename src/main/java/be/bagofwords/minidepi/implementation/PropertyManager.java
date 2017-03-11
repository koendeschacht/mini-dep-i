/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.minidepi.ApplicationContext;
import be.bagofwords.minidepi.ApplicationContextException;
import be.bagofwords.minidepi.PropertyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private Properties properties;
    private String applicationName;

    public PropertyManager(PropertyConfiguration config) throws IOException {
        properties = new Properties();
        if (config.readDefaultProperties) {
            InputStream defaultPropertiesInputStream = this.getClass().getResourceAsStream("/default.properties");
            if (defaultPropertiesInputStream == null) {
                logger.info("Could not read default.properties");
            } else {
                properties.load(defaultPropertiesInputStream);
            }
        }
        if (config.useEnvironmentVariable || config.useEnvironmentVariableIfPresent) {
            String propertyFile = System.getProperty("property-file");
            if (propertyFile == null) {
                if (!config.useEnvironmentVariable) {
                    logger.warn("No property file specified. You can specify one with -Dproperty-file=/some/path");
                } else {
                    throw new ApplicationContextException("No property file specified. You need to specify one with -Dproperty-file=/some/path");
                }
            } else {
                properties.load(new FileInputStream(propertyFile));
            }
        }
        if (config.propertiesPath != null) {
            properties.load(new FileInputStream(config.propertiesPath));
        }
        properties.putAll(config.properties);
        initializeApplicationName();
    }

    private void initializeApplicationName() {
        this.applicationName = properties.getProperty("application_name");
        if (this.applicationName == null) {
            this.applicationName = "some_application";
        }
    }

    public String getApplicationName() {
        return applicationName;
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
