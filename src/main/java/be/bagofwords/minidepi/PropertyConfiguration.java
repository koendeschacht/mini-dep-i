/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-11. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi;

import java.util.HashMap;
import java.util.Map;

public class PropertyConfiguration {

    public boolean readDefaultProperties;
    public boolean useEnvironmentVariable;
    public boolean useEnvironmentVariableIfPresent;
    public String propertiesPath;
    public Map<String, String> properties = new HashMap<>();

    public static PropertyConfiguration create() {
        return new PropertyConfiguration();
    }

    public PropertyConfiguration readDefaultProperties() {
        readDefaultProperties = true;
        return this;
    }

    public PropertyConfiguration useEnvironmentVariable() {
        useEnvironmentVariable = true;
        return this;
    }

    public PropertyConfiguration useEnvironmentVariableIfPresent() {
        useEnvironmentVariableIfPresent = true;
        return this;
    }

    public PropertyConfiguration readFrom(String pathOfPropertiesFile) {
        propertiesPath = pathOfPropertiesFile;
        return this;
    }

    public PropertyConfiguration use(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

}
