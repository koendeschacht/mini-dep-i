/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import java.io.IOException;
import java.util.Properties;

public interface PropertyProvider {

    String triggerProperty();

    void addProperties(Properties properties) throws IOException;

}
