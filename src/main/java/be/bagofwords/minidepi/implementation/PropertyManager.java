/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.implementation;

import be.bagofwords.logging.Log;
import be.bagofwords.minidepi.PropertyException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertyManager {

    private Properties properties;
    private Map<String, Properties> readPropertyResources = new HashMap<>();
    private List<File> readPropertyFiles = new ArrayList<>();
    private UserInputManager userInputManager = new UserInputManager();

    public PropertyManager(Properties properties) {
        this.properties = properties;
        init();
    }

    public void init() {
        properties.putAll(System.getProperties());
        readAllPropertiesFromPropertyFiles();
    }

    public void readAllPropertiesFromPropertyFiles() {
        //Keep reading from property files until no new file found
        String lastPropertyFile = null;
        String lastPropertyFiles = null;
        boolean finished = false;
        while (!finished) {
            finished = true;
            String propertyFile = properties.getProperty("property.file");
            if (propertyFile != null && !propertyFile.equals(lastPropertyFile)) {
                readPropertiesFromFile(properties, propertyFile);
                lastPropertyFile = propertyFile;
                finished = false;
            }
            String propertyFiles = properties.getProperty("property.files");
            if (propertyFiles != null && !propertyFiles.equals(lastPropertyFiles)) {
                String[] propertyFilesArr = propertyFiles.split(",");
                for (String file : propertyFilesArr) {
                    readPropertiesFromFile(properties, file);
                }
                lastPropertyFiles = propertyFiles;
                finished = false;
            }
        }
    }

    public void readPropertiesFromFile(Properties properties, String path) {
        path = path.trim();
        File propertiesFile = new File(path);
        if (!propertiesFile.exists()) {
            throw new RuntimeException("Could not find file " + propertiesFile.getAbsolutePath());
        }
        try {
            properties.load(new FileInputStream(propertiesFile));
            readPropertyFiles.add(propertiesFile);
        } catch (IOException e) {
            throw new PropertyException("Failed to read properties from file " + propertiesFile.getAbsolutePath(), e);
        }
        Log.i("Read properties from " + path);
    }


    public String getProperty(String name, String orFrom) {
        String value = properties.getProperty(name);
        if (value == null) {
            return readPropertyFromFallbackResource(name, orFrom); //Should always return a value, since a fallback file was specified
        } else {
            return value;
        }
    }

    public String getProperty(String name) {
        String value = properties.getProperty(name);
        if (value == null) {
            value = userInputManager.getPropertyFromUserInputIfPossible(name, readPropertyFiles);
        }
        if (value == null) {
            throw new PropertyException("The property " + name + " was not found");
        }
        return value;
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }

    private synchronized String readPropertyFromFallbackResource(String name, String propertyResource) {
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
            //Should not happen normally, since the developer of the specific library specified the propertyResource
            throw new PropertyException("The configuration option " + name + " was not found in default properties " + propertyResource);
        }
        return value;
    }
}
