/*******************************************************************************
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-3-18. For license
 * information see the LICENSE file in the root folder of this repository.
 ******************************************************************************/

package be.bagofwords.minidepi.properties;

import be.bagofwords.logging.Log;
import be.bagofwords.util.SocketConnection;

import java.io.IOException;
import java.util.Properties;

public class SocketPropertyProvider implements PropertyProvider {

    @Override
    public String triggerProperty() {
        return "socket.properties.host";
    }

    @Override
    public void addProperties(Properties properties) throws IOException {
        String host = properties.getProperty("socket.properties.host");
        int port = Integer.parseInt(properties.getProperty("socket.properties.port"));
        String applicationName = properties.getProperty("application.name");
        SocketConnection socketConnection = new SocketConnection(host, port, "load-properties");
        socketConnection.writeString(applicationName);
        int numberOfProperties = socketConnection.readInt();
        for (int i = 0; i < numberOfProperties; i++) {
            properties.setProperty(socketConnection.readString(), socketConnection.readString());
        }
        socketConnection.close();
        Log.i("Read properties from " + host + ":" + port);
    }
}
