package com.example.drill;
/**
 * Created by User on 21/08/2017.
 */

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fi.iki.elonen.NanoHTTPD;


public class Server extends NanoHTTPD {
    private static Server server = null;

//    @Override
//    public Response serve(IHTTPSession session) {
//        String msg = " My Server in Android\n";
//        msg += " Hi , this is a response from your android server !";
//        return new Response(msg + "\n");
//
//    }

    @Override
    public Response serve(IHTTPSession session) {
        String tests = null;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               byte[] test =  Files.readAllBytes(Paths.get(Environment
                       .getExternalStorageDirectory().toString()
                       + "/maciek.xml"));
                tests = new String(test).toLowerCase();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(tests);
    }

    public Server() throws IOException {
        super(8080);

    }

    // This static method let you access the unique instance of your server  class
    public static Server getServer() throws IOException {
        if (server == null) {

            server = new Server();
        }
        return server;

    }


}