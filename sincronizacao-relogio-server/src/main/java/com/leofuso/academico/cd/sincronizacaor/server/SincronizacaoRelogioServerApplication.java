package com.leofuso.academico.cd.sincronizacaor.server;

import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramChannel;
import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramMessage;

import java.net.DatagramSocket;

public class SincronizacaoRelogioServerApplication {

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
