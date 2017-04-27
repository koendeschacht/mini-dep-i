/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import be.bagofwords.logging.Log;

import java.io.IOException;
import java.util.Properties;

public class SystemPropertiesPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return null; //Always triggered
    }

    @Override
    public void addProperties(Properties properties) throws IOException {
        properties.putAll(System.getProperties());
        Log.i("Read system properties");
    }

}
