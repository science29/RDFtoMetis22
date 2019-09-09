package distiributed;

import org.omg.PortableServer.POA;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

public class Receiver extends Thread {

    private static final int BASE_PORT = 49158;
    private int port;
    private final String host;
    private Transporter transporter;
    private boolean stop = false;
    private Socket server;
    private ServerSocket serverSocket;
    private final int id;

    public Receiver(Transporter transporter, String host, int id) {
        this.transporter = transporter;
        this.host = host;
        String[] arr = host.split("\\.");
        this.port = Integer.valueOf(arr[arr.length - 1]) + BASE_PORT;
        this.id = id;
    }

    public void stopWorking() {
        stop = true;
        closeSockets();
        //TODO ..
    }


    private void closeSockets() {
        try {
            if (server != null && !server.isClosed())
                server.close();
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            System.out.println("Reciver socket with " + host + ":" + port + " is closed ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            try {
                server = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(server.getInputStream()));
                System.out.println("ready to receive from remote at port:" + port);
                while (!stop) {

                    int length = in.readInt();
                    System.err.println("got something .. length "+length);
                    if(length == Transporter.QUERY_MSG){
                        System.out.println("Received Query from "+host);
                        int queryNo = in.readInt();
                        String query = in.readUTF();
                        System.out.println("Received Query from "+host + " "+query);
                        transporter.recievedQuery(query , queryNo);
                    }
                    if (length == Transporter.PING_MESSAGE) {
                        System.out.println("recieved ping msg from " + host + ":" + port);
                        transporter.pingBack(id);
                        continue;
                    }
                    if (length == Transporter.PING_REPLY_MESSAGE) {
                        System.out.println("recieved ping reply msg from " + host + ":" + port);
                        transporter.gotPingReply(id);
                        continue;
                    }
                    if (length > 0) {
                        byte[] data = new byte[length];
                        in.readFully(data, 0, data.length);
                        SendItem sendItem = SendItem.fromByte(data);
                        if (transporter != null)
                            transporter.receiverGotResult(sendItem);

                        /*if (host.matches("172.20.32.8")) { //TODO remove remove remove...
                            System.out.println("sending it back");
                            transporter.sendToAll(sendItem);
                        }*/
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSockets();
    }
}
