import datagram.DatagramChannel;
import datagram.DatagramMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

class UDPClient {

    public static void main(String[] args) throws Exception {

        final InetAddress ipAddress = InetAddress.getByName("localhost");
        final int port = 9876;
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddress, port);

        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader inFromUser = new BufferedReader(inputStreamReader);
        String sentence = inFromUser.readLine();

        DatagramSocket clientSocket = new DatagramSocket();
        final DatagramChannel<String> stringDatagramChannel = new DatagramChannel<>(clientSocket, String.class);

        stringDatagramChannel.send(sentence, inetSocketAddress);
        final DatagramMessage<String> message = stringDatagramChannel.listen();

        System.out.println(message.getPayload());

        clientSocket.close();
    }
}
