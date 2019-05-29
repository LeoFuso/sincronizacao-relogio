package com.leofuso.academico.cd.sincronizacaor.core.datagram;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.logging.Logger;

public class DatagramMessage<T extends Serializable> {

    private static final Logger LOGGER = Logger.getLogger(DatagramMessage.class.getName());
    private static final byte[] MESSAGE_SIZE = new byte[1024];

    private final T payload;
    private final InetSocketAddress inetSocketAddress;

    DatagramMessage(DatagramPacket packet, Class<T> reference) {
        Objects.requireNonNull(packet, String.format("%s cannot be null", packet.getClass().getName()));

        final byte[] byteArray = packet.getData();
        verifyPayloadSize(byteArray);

        final ByteArrayInputStream byteArrayInputStream
                = new ByteArrayInputStream(byteArray);

        T payload;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            payload = reference.cast(objectInputStream.readObject());
            final InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();

            this.payload = payload;
            this.inetSocketAddress = socketAddress;

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    DatagramMessage(T payload, InetSocketAddress inetSocketAddress) {
        this.payload = Objects.requireNonNull(payload, String.format("%s cannot be null", payload.getClass().getName()));
        this.inetSocketAddress = Objects.requireNonNull(inetSocketAddress, String.format("%s cannot be null", inetSocketAddress.getClass().getName()));
    }

    static DatagramPacket defaultPacket() {
        return new DatagramPacket(MESSAGE_SIZE, MESSAGE_SIZE.length);
    }

    private static void verifyPayloadSize(byte[] payloadInByteArray) {
        final boolean illegalSize = payloadInByteArray.length > MESSAGE_SIZE.length;
        if (illegalSize) {
            final String message = "Payload has illegal size [ " + payloadInByteArray.length + " ]";
            LOGGER.severe(message);
            throw new IllegalStateException(message);
        }
    }

    DatagramPacket getPacket() {

        final InetAddress address = this.inetSocketAddress.getAddress();
        final int port = this.inetSocketAddress.getPort();

        ByteArrayOutputStream byteArrayOutputStream
                = new ByteArrayOutputStream();

        try (ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)) {
            oos.writeObject(payload);
            oos.flush();
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        verifyPayloadSize(data);

        return new DatagramPacket(data, data.length, address, port);
    }

    public T getPayload() {
        return payload;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }
}
