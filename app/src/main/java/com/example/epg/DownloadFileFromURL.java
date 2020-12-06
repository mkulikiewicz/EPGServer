package com.example.epg;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
    private String filePath;
    private ProgressDialog progressBar;

    public DownloadFileFromURL(String filePath, ProgressDialog progressBar) {
        this.filePath = filePath;
        this.progressBar = progressBar;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(filePath);
            byte[] data = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + ((int) ((total * 100) / lenghtOfFile)));
                output.write(data, 0, count);
            }

            output.close();
            input.close();
            progressBar.setMessage("Correcting the file...");
            FileCorrector.correctId(filePath);
        } catch (Exception e) {
            Log.e("AppError", e.getMessage());
        }
        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        progressBar.setProgress(Integer.parseInt(progress[0]) * -1 - 3);
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        progressBar.dismiss();
    }

}