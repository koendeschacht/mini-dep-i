/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class SystemPropertiesPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return null; //Always trigger
    }

    @Override
    public void addProperties(Properties properties, Logger logger) throws IOException {
        properties.putAll(System.getProperties());
        logger.info("Read system properties");
    }

}
