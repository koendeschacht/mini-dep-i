/*
 * Created by Koen Deschacht (koendeschacht@gmail.com) 2017-4-28. For license
 * information see the LICENSE file in the root folder of this repository.
 */

package be.bagofwords.minidepi.remote;

import be.bagofwords.exec.RemoteExecConfig;
import be.bagofwords.util.SocketConnection;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class RemoteExecService {

    public static final String SOCKET_NAME = "remote-exec";

    public SocketConnection execRemotely(String host, int port, RemoteExecConfig remoteExecConfig) throws IOException {
        SocketConnection socketConnection = new SocketConnection(host, port, SOCKET_NAME);
        socketConnection.writeValue(remoteExecConfig.pack());
        boolean success = socketConnection.readBoolean();
        if (!success) {
            String error = socketConnection.readString();
            socketConnection.writeBoolean(true);
            IOUtils.closeQuietly(socketConnection);
            String remoteError = "\nREMOTE ERROR START:\n" + error + "\nREMOTE ERROR END\n";
            throw new RuntimeException("Failed to execute remote class " + remoteExecConfig.getExecutorClassName() + remoteError);
        } else {
            socketConnection.writeBoolean(true);
        }
        return socketConnection;
    }

}
