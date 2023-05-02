package com.imdevil.scrcpy;

import com.imdevil.scrcpy.ui.GameWindow;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ControlHandler implements Runnable {
    private final Socket client;

    public ControlHandler(Socket clientSocket) {
        this.client = clientSocket;
    }

    @Override
    public void run() {
        OutputStream outputStream;
        try {
            outputStream = client.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (outputStream != null) {
            new GameWindow().init(outputStream);
        }
    }
}