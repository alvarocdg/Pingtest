package de.zh32.pingtest;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import lombok.Data;

@Data
public final class ServerListPing14 {
    private InetSocketAddress address;
    private int timeout = 2500;
    private int pingVersion = -1;
    private int protocolVersion = -1;
    private String gameVersion;
    private String motd;
    private int playersOnline = -1;
    private int maxPlayers = -1;

    public boolean fetchData() throws Exception {
        Socket socket = new Socket();
        OutputStream outputStream;
        DataOutputStream dataOutputStream;
        InputStream inputStream;
        InputStreamReader inputStreamReader;

        socket.setSoTimeout(timeout);
        socket.setKeepAlive(false);

        socket.connect(address, this.getTimeout());
        outputStream = socket.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);

        inputStream = socket.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));
        dataOutputStream.write(new byte[]{
            (byte) 0xFE,
            (byte) 0x01

        });


        int packetId = inputStream.read();

        if (packetId == -1) {
                throw new IOException("Premature end of stream.");
        }

        if (packetId != 0xFF) {
                throw new IOException("Invalid packet ID (" + packetId + ").");
        }

        int length = inputStreamReader.read();

        if (length == -1) {
                throw new IOException("Premature end of stream.");
        }

        if (length == 0) {
                throw new IOException("Invalid string length.");
        }

        char[] chars = new char[length];

        if (inputStreamReader.read(chars,0,length) != length) {
                throw new IOException("Premature end of stream.");
        }

        String string = new String(chars);


        String[] data = string.split("\00");

        this.pingVersion = Integer.parseInt(data[0].substring(1));
        this.protocolVersion = Integer.parseInt(data[1]);
        this.gameVersion = data[2];
        this.motd = data[3];
        this.playersOnline = Integer.parseInt(data[4]);
        this.maxPlayers = Integer.parseInt(data[5]);


        dataOutputStream.close();
        outputStream.close();

        inputStreamReader.close();
        inputStream.close();
        socket.close();

        return true;
    }
}

