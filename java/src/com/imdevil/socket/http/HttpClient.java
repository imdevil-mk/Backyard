package com.imdevil.socket.http;

import java.net.*;
import java.io.*;

public class HttpClient {

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 3000;
        String path1 = "/topic/detail?actid=111551188";
        String path2 = "/topic/detail?actid=111551189";

        // 创建 Socket 连接
        Socket socket = new Socket(host, port);
        OutputStream outputStream = socket.getOutputStream();

        // 发送第一个 HTTP 请求
        sendHttpRequest(outputStream, host, path1);

        // 接收第一个 HTTP 响应
        InputStream inputStream = socket.getInputStream();
        String response1 = readHttpResponse(inputStream);
        System.out.println(response1);

        // 发送第二个 HTTP 请求
        sendHttpRequest(outputStream, host, path2);

        // 接收第二个 HTTP 响应
        String response2 = readHttpResponse(inputStream);
        System.out.println(response2);

        // 关闭连接
        socket.close();
    }

    private static void sendHttpRequest(OutputStream outputStream, String host, String path) throws Exception {
        String request = "GET " + path + " HTTP/1.1\r\n"
                + "Host: " + host + "\r\n"
                + "User-Agent: Mozilla/5.0\r\n"
                + "\r\n";
        outputStream.write(request.getBytes());
    }

    private static String readHttpResponse(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line).append("\n");
        }
        return responseBuilder.toString();
    }
}
