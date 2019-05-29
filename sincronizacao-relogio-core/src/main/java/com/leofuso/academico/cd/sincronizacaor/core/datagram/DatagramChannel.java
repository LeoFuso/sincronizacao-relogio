package com.leofuso.academico.cd.sincronizacaor.core.datagram;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class DatagramChannel<T extends Serializable> {

    private static final Logger LOGGER = Logger.getLogger(DatagramChannel.class.getName());

    private final DatagramSocket socket;
    private final Class<T> reference;

    public DatagramChannel(DatagramSocket socket, Class<T> reference) {
        this.socket = socket;
        this.reference = reference;
    }

    public DatagramMessage<T> listen() throws IOException {
        final DatagramPacket datagramPacket = DatagramMessage.defaultPacket();
        socket.receive(datagramPacket);

        return new DatagramMessage<>(datagramPacket, reference);
    }

    public void send(T payload, InetSocketAddress address) {

        final DatagramMessage datagramMessage = new DatagramMessage<>(payload, address);
        final DatagramPacket packet = datagramMessage.getPacket();

        try {
            socket.send(packet);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void reply(T payload, DatagramMessage<T> message) {
        final InetSocketAddress inetSocketAddress = message.getInetSocketAddress();
        send(payload, inetSocketAddress);
    }
}
