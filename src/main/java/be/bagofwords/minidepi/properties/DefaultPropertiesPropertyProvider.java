/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DefaultPropertiesPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "read-default-properties";
    }

    @Override
    public void addProperties(Properties properties, Logger logger) throws IOException {
        InputStream defaultPropertiesInputStream = this.getClass().getResourceAsStream("/default.properties");
        if (defaultPropertiesInputStream == null) {
            throw new IOException("Could not read resource /default.properties");
        } else {
            properties.load(defaultPropertiesInputStream);
        }
        logger.info("Read properties from resource default.properties");
    }
}
