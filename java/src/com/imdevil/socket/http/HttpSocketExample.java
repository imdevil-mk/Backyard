package com.imdevil.socket.http;

import java.io.*;
import java.net.Socket;

public class HttpSocketExample {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 3000;
        String path = "/topic/detail?actid=111551188";

        // create a socket and connect to the server
        Socket socket = new Socket(host, port);

        // create an output stream to write to the server
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        String request = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + host + ":" + port + "\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "\r\n";
        out.write(request);
        out.flush();

        // create an input stream to read from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("---" + inputLine);
            response.append(inputLine);
            response.append("\n");
        }

        // print the response from the server
        System.out.println(response.toString());

        // close the socket
        socket.close();
    }
}
