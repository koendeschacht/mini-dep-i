/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import be.bagofwords.util.SocketConnection;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class SocketPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "socket-properties-host";
    }

    @Override
    public void addProperties(Properties properties, Logger logger) throws IOException {
        String host = properties.getProperty("socket-properties-host");
        int port = Integer.parseInt(properties.getProperty("socket-properties-port"));
        String applicationName = properties.getProperty("application-name");
        String applicationTag = properties.getProperty("application-tag");
        String environment = properties.getProperty("application-environment");
        SocketConnection socketConnection = new SocketConnection(host, port, "load-properties");
        socketConnection.writeString(applicationName);
        socketConnection.writeString(applicationTag);
        socketConnection.writeString(environment);
        int numberOfProperties = socketConnection.readInt();
        for (int i = 0; i < numberOfProperties; i++) {
            properties.setProperty(socketConnection.readString(), socketConnection.readString());
        }
        socketConnection.close();
        logger.info("Read properties from " + host + ":" + port);
    }
}
