/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import be.bagofwords.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFilePropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "property.file";
    }

    @Override
    public void addProperties(Properties properties) throws IOException {
        String path = properties.getProperty("property.file");
        if (path == null) {
            throw new RuntimeException("No \"property.file\" property specified");
        }
        File propertiesFile = new File(path);
        if (!propertiesFile.exists()) {
            throw new RuntimeException("Could not find file " + propertiesFile.getAbsolutePath());
        }
        properties.load(new FileInputStream(propertiesFile));
        Log.i("Read properties from " + path);
    }
}
