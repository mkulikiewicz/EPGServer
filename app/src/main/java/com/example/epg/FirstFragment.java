package com.example.epg;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import static android.content.Context.WIFI_SERVICE;

public class FirstFragment extends Fragment {

    private String filePath;
    private final String epgUrlPath = "https://epg-polska.net/guide.xml";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        filePath = Environment.getExternalStorageDirectory().toString() + "/guide.xml";
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.textview_first);
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {

                ProgressDialog progressBar = createProgressDialog();
                new DownloadFileFromURL(filePath, progressBar).execute(epgUrlPath);

                Server server = Server.getInstance(filePath);
                try {
                    server.start();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "App wasn't able to create server", Toast.LENGTH_LONG);
                }

                showMessage(textView, server);
            }
        });
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);//you can cancel it by pressing back button
        progressDialog.setMessage("File downloading ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);//initially progress is 0
        progressDialog.setMax(100);//sets the maximum value 100
        progressDialog.show();//displays the progress bar
        return progressDialog;
    }

    private void showMessage(TextView textView, Server server) {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

        textView.setText("Our local EPG server: http://" + formatedIpAddress + ":" + server.getListeningPort());
    }
}