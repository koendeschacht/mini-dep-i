/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFilePropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "property-file";
    }

    @Override
    public void addProperties(Properties properties, Logger logger) throws IOException {
        String path = properties.getProperty("property-file");
        if (path == null) {
            throw new RuntimeException("No \"property-file\" property specified");
        }
        properties.load(new FileInputStream(path));
        logger.info("Read properties from " + path);
    }
}
