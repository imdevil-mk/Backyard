package com.imdevil.scrcpy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScrcpyServer {

    public static void main(String[] args) {
        //*/
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        VideoHandler videoHandler = null;
        ControlHandler controlHandler = null;

        try (ServerSocket serverSocket = new ServerSocket(5435)) {
            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                if (videoHandler == null) {
                    videoHandler = new VideoHandler(clientSocket);
                    executorService.execute(videoHandler);
                } else if (controlHandler == null) {
                    controlHandler = new ControlHandler(clientSocket);
                    executorService.execute(controlHandler);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*/
        new com.imdevil.scrcpy.ui.GameWindow().init(null);
        //*/
    }
}
