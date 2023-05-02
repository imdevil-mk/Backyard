package com.imdevil.scrcpy;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VideoHandler implements Runnable {
    public static final int DEVICE_NAME_FIELD_LENGTH = 64;

    private final Socket client;

    public VideoHandler(Socket clientSocket) {
        this.client = clientSocket;
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            inputStream = client.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer = new byte[DEVICE_NAME_FIELD_LENGTH + 4];
        try {
            int l = inputStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String deviceName = new String(buffer, 0, DEVICE_NAME_FIELD_LENGTH, StandardCharsets.UTF_8).trim();
        int width = ((buffer[DEVICE_NAME_FIELD_LENGTH] & 0xFF) << 8) | (buffer[DEVICE_NAME_FIELD_LENGTH + 1] & 0xFF);
        int height = ((buffer[DEVICE_NAME_FIELD_LENGTH + 2] & 0xFF) << 8) | (buffer[DEVICE_NAME_FIELD_LENGTH + 3] & 0xFF);

        System.out.println("name: " + deviceName);
        System.out.println("width: " + width);
        System.out.println("height: " + height);
    }
}