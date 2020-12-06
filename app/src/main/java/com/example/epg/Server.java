package com.example.epg;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {
    private static Server server;
    private final String fileToPresent;

    public static Server getInstance(String fileToPresent) {
        if(server == null)
        {
            server = new Server(fileToPresent);
        }
        return server;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Response serve(IHTTPSession session) {
        final Path path = Paths.get(fileToPresent);
        final Response resp;
        try {
            resp = newFixedLengthResponse(
                    Response.Status.OK,
                    "text/xml",
                    Files.newInputStream(path),
                    Files.size(path)
            );
            resp.addHeader("Content-Disposition", "attachment; filename=\"guide.xml\"");
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "ERROR");
        }
    }

    protected Server(String fileToPresent) {
        super(8080);
        this.fileToPresent = fileToPresent;
    }
}