package com.example.audioapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnStopRecord, btnPlay, btnStop;

    String pathsave=" ";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaplayer;

    final int REQUEST_PERMISSION_CODE=1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!CheckPermissionFromDevice())
            requestPermissions();

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnRecord = (Button)findViewById(R.id.btnStartRecord);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStopRecord = (Button)findViewById(R.id.btnStopRecord);


        btnRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(CheckPermissionFromDevice())
                {
                    pathsave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        btnStopRecord.setEnabled(true);
                        btnPlay.setEnabled(false);
                        btnRecord.setEnabled(false);
                        btnStop.setEnabled(false);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    requestPermissions();
                }
            }
        });
        btnStopRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStopRecord.setEnabled(false);
                btnPlay.setEnabled(true);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);

            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setEnabled(true);
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(false);

                mediaplayer = new MediaPlayer();
                try{
                    mediaplayer.setDataSource(pathsave);
                    mediaplayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaplayer.start();
                Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStopRecord.setEnabled(false);
                btnRecord.setEnabled(true);
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);

                if(mediaplayer !=null)
                {
                    mediaplayer.stop();
                    mediaplayer.release();
                    setupMediaRecorder();
                }

            }
        });


    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);


    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        },REQUEST_PERMISSION_CODE );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private boolean CheckPermissionFromDevice()
    {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

}