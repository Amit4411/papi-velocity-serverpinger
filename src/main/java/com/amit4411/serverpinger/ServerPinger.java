package com.amit4411.serverpinger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerPinger {
    public static ServerInfo ping(String address) throws Exception {
        String[] parts = address.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid address format. Use IP:PORT");
        }

        String host = parts[0];
        int port = Integer.parseInt(parts[1]);

        return ping(host, port);
    }

    private static ServerInfo ping(String host, int port) throws Exception {
        Socket socket = new Socket(host, port);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        try {
            // Send handshake
            ByteArrayOutputStream handshake = new ByteArrayOutputStream();
            writeVarInt(handshake, 0); // Packet ID for Handshake
            writeVarInt(handshake, 393); // Protocol version (1.20)
            writeString(handshake, host);
            out.write((byte) port);
            out.write((byte) (port >> 8));
            writeVarInt(handshake, 1); // Next state: Status

            byte[] data = handshake.toByteArray();
            writeVarInt(out, data.length);
            out.write(data);
            out.flush();

            // Send Status Request
            ByteArrayOutputStream statusRequest = new ByteArrayOutputStream();
            writeVarInt(statusRequest, 0); // Packet ID for Status Request
            byte[] statusData = statusRequest.toByteArray();
            writeVarInt(out, statusData.length);
            out.write(statusData);
            out.flush();

            // Read response
            int packetLength = readVarInt(in);
            int packetId = readVarInt(in);
            String jsonResponse = readString(in);

            // Parse JSON
            JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonObject players = json.getAsJsonObject("players");
            JsonObject version = json.getAsJsonObject("version");
            JsonElement descriptionElement = json.get("description");

            int onlinePlayers = players.get("online").getAsInt();
            int maxPlayers = players.get("max").getAsInt();
            String motd = descriptionElement.isJsonObject() 
                ? descriptionElement.getAsJsonObject().get("text").getAsString()
                : descriptionElement.getAsString();
            String versionName = version.get("name").getAsString();

            socket.close();
            return new ServerInfo(onlinePlayers, maxPlayers, motd, versionName);
        } catch (Exception e) {
            socket.close();
            throw e;
        }
    }

    private static void writeVarInt(OutputStream out, int value) throws IOException {
        while ((value & 0xFFFFFF80) != 0) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.write(value & 0x7F);
    }

    private static int readVarInt(InputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.read();
            if (k == -1) throw new IOException("End of stream");
            i |= (k & 0x7F) << (j++ * 7);
            if (j > 5) throw new IOException("VarInt is too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    private static void writeString(OutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    private static String readString(InputStream in) throws IOException {
        int length = readVarInt(in);
        byte[] bytes = new byte[length];
        in.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}