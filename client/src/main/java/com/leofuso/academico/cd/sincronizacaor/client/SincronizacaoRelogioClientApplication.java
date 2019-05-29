package com.leofuso.academico.cd.sincronizacaor.client;

import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramChannel;
import com.leofuso.academico.cd.sincronizacaor.core.datagram.DatagramMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class SincronizacaoRelogioClientApplication {

    public static void main(String[] args) throws Exception {

        final InetAddress ipAddress = extractIP(args);
        final int port = 9876;
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddress, port);

        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader inFromUser = new BufferedReader(inputStreamReader);

        while (true) {

            String sentence = inFromUser.readLine();

            DatagramSocket clientSocket = new DatagramSocket();
            final DatagramChannel<String> stringDatagramChannel = new DatagramChannel<>(clientSocket, String.class);
            stringDatagramChannel.send(sentence, inetSocketAddress);

            final DatagramMessage<String> message = stringDatagramChannel.listen();
            System.out.println(message.getPayload());

            clientSocket.close();
        }

    }

    private static InetAddress extractIP(String[] args) throws UnknownHostException {
        Objects.requireNonNull(args);

        final boolean hasIP = args.length == 1;

        String ip = "localhost";

        if (hasIP)
            ip = args[0];

        return InetAddress.getByName(ip);
    }
}
