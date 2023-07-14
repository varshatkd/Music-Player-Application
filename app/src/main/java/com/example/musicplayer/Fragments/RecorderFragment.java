package com.example.musicplayer.Fragments;



import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

//import com.example.musicplayer.Manifest;
import com.example.musicplayer.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import android.Manifest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import pl.droidsonroids.gif.GifImageView;

public class RecorderFragment extends Fragment {
    View view;
ImageButton btnRec;
TextView txtRecStatus;
Chronometer timeRec;
GifImageView gifView;


    private static final int REQUEST_PERMISSION_CODE = 1001;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



private static String fileName;
private MediaRecorder recorder;
boolean isRecording;
File path;
//File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MPVRecorder");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_recorder,container,false);
        btnRec=view.findViewById(R.id.btnRec);
        txtRecStatus=view.findViewById(R.id.textRecStatus);
        gifView=view.findViewById(R.id.gifView);
        timeRec=view.findViewById(R.id.timeRec);

        isRecording=false;


        path = new File(requireActivity().getExternalFilesDir(null), "MPVRecorder");



//        askruntimePermission();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHMMSS", Locale.getDefault());
         String date=format.format(new Date());
        fileName=path+"/recording"+date+".amr";
        if(!path.exists()){
            path.mkdirs();
        }


        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                if (!isRecording) {
                    if (checkPermissions()) {
                        startRecording();
                        gifView.setVisibility(View.VISIBLE);
                        timeRec.setBase(SystemClock.elapsedRealtime());
                        timeRec.start();
                        txtRecStatus.setText("Recording...");
                        btnRec.setImageResource(R.drawable.ic_stop);
                        isRecording = true;
                    } else {
                        requestPermissions();
                    }
                } else {
                    stopRecording();
                    gifView.setVisibility(View.GONE);
                    timeRec.setBase(SystemClock.elapsedRealtime());
                    timeRec.stop();
                    txtRecStatus.setText("");
                    btnRec.setImageResource(R.drawable.ic_record);
                    isRecording = false;
                }
            }
        });



        return view;
    }







    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                startRecording();
                gifView.setVisibility(View.VISIBLE);
                timeRec.setBase(SystemClock.elapsedRealtime());
                timeRec.start();
                txtRecStatus.setText("Recording...");
                btnRec.setImageResource(R.drawable.ic_stop);
                isRecording = true;
            } else {
                Toast.makeText(requireContext(), "Permissions denied!", Toast.LENGTH_SHORT).show();

            }}}
















    private void startRecording(){
        fileName = generateFileName();
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();

            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Couldn't start recording!", Toast.LENGTH_SHORT).show();

        }
//        recorder.start();
    }

    private void stopRecording(){
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(requireContext(), "Recording saved: " + fileName, Toast.LENGTH_SHORT).show();
        }
        }




    private String generateFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date = format.format(new Date());
        File directory = new File(path.getAbsolutePath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath() + File.separator + "recording_" + date + ".amr";
    }






    }

