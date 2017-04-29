/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-4-27. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import be.bagofwords.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFilesPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "property.files";
    }

    @Override
    public void addProperties(Properties properties) throws IOException {
        String paths = properties.getProperty("property.files");
        if (paths == null) {
            throw new RuntimeException("No \"property.files\" property specified");
        }
        String[] splittedPaths = paths.split(",");
        for (String path : splittedPaths) {
            path = path.trim();
            File propertiesFile = new File(path);
            if (!propertiesFile.exists()) {
                throw new RuntimeException("Could not find file " + propertiesFile.getAbsolutePath());
            }
            properties.load(new FileInputStream(propertiesFile));
            Log.i("Read properties from " + path);
        }
    }
}
