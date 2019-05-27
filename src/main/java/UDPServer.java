import datagram.DatagramChannel;
import datagram.DatagramMessage;

import java.net.DatagramSocket;

class UDPServer {
    public static void main(String[] args) throws Exception {

        final DatagramSocket serverSocket = new DatagramSocket(9876);
        final DatagramChannel<String> stringDatagramChannel = new DatagramChannel<>(serverSocket, String.class);

        while (true) {
            final DatagramMessage<String> message = stringDatagramChannel.listen();

            String sentence = message.getPayload();
            System.out.println(sentence);

            String capitalizedSentence = sentence.toUpperCase();
            stringDatagramChannel.reply(capitalizedSentence, message);
        }
    }
}
