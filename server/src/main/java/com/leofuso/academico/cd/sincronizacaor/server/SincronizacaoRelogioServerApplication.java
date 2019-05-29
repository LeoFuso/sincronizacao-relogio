package com.leofuso.academico.cd.sincronizacaor.server;

import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramChannel;
import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramMessage;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class SincronizacaoRelogioServerApplication {

    public static void main(String[] args) throws Exception {

        final DatagramSocket serverSocket = new DatagramSocket(9876);
        final DatagramChannel<String> stringDatagramChannel = new DatagramChannel<>(serverSocket, String.class);

        while (true) {

            final DatagramMessage<String> message = stringDatagramChannel.listen();

            final InetAddress address = message.getInetSocketAddress().getAddress();
            final int port = message.getInetSocketAddress().getPort();
            String sentence = message.getPayload();

            final String output = String.format("[ %s:%d ]: %s", address.toString(), port, sentence);
            System.out.println(output);

            String capitalizedSentence = sentence.toUpperCase();
            stringDatagramChannel.reply(capitalizedSentence, message);

        }
    }
}
