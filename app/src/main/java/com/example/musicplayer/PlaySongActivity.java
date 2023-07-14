package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//import androidx.appcompat.widget.Toolbar;

import com.example.musicplayer.Model.UploadSong;

//import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class PlaySongActivity extends AppCompatActivity {
Button btnplay,btnnext,btnprev,btnff,btnfr,back;
TextView txtsname,txtsstart,txtsstop;
SeekBar seekmusic;
ImageView image;
//BarVisualizer visualizer;

//    Toolbar toolbar;
    String sname;
    public static final String EXTRA_NAME = "song_name";
    private MediaPlayer mediaPlayer;
    int position;
//    ArrayList<UploadSong> arrayListSongsName=new ArrayList<>();
ArrayList<UploadSong> arrayListSongsName=new ArrayList<>();
Thread updateseekbar;
Handler handler=new Handler();
Runnable runnable;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    //    @SuppressLint("MissingInflatedId")
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);


//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle("Now Playing");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.playbtn);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        txtsname = findViewById(R.id.txtsn);
        txtsstart = findViewById(R.id.txtstart);
        txtsstop = findViewById(R.id.txtstop);
        seekmusic = findViewById(R.id.seekbar);
        image = findViewById(R.id.imageview);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaySongActivity.this, showsongs.class);
                startActivity(i);
                mediaPlayer.pause();
                finish();
            }
        });

//        visualizer = findViewById(R.id.blast);

        mediaPlayer = new MediaPlayer();

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.baseline_play);
                    mediaPlayer.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.baseline_pause);
                    mediaPlayer.start();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });


        Intent intent = getIntent();
        position = intent.getIntExtra("pos", 0);
        ArrayList<UploadSong> songTitle = intent.getParcelableArrayListExtra("songs");
//        arrayListSongsName = intent.getParcelableArrayListExtra("songs");
//        UploadSong selectedSong = arrayListSongsName.get(position);
        UploadSong selectedSong = songTitle.get(position);
        sname = selectedSong.getSongTitle();
//        String songLink=selectedSong.getSongLink();
        txtsname.setText(sname);


        String songUrl = selectedSong.getSongLink();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(songUrl));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();

                seekmusic.setMax(mp.getDuration());
                String endTime = createTime(mp.getDuration());
                txtsstop.setText(endTime);


            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();


                if (songTitle.isEmpty()) {
                    // Handle the case where the song list is empty
                    Toast.makeText(PlaySongActivity.this, "No songs available", Toast.LENGTH_SHORT).show();
                    return;
                }


//                position=(position+1)%arrayListSongsName.size();
//                UploadSong selectedSong = arrayListSongsName.get(position);
                position = (position + 1) % songTitle.size();
                UploadSong selectedSong = songTitle.get(position);
                Uri u = Uri.parse(selectedSong.getSongLink());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = selectedSong.getSongTitle();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause);
                startAnimation(image);


                String endTime = createTime(mediaPlayer.getDuration());
                txtsstop.setText(endTime);


            }
        });


        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if (songTitle.isEmpty()) {
                    // Handle the case where the song list is empty
                    Toast.makeText(PlaySongActivity.this, "No songs available", Toast.LENGTH_SHORT).show();
                    return;
                }


                position = ((position - 1) < 0) ? (songTitle.size() - 1) : (position - 1);
                UploadSong selectedSong = songTitle.get(position);
                Uri songUri = Uri.parse(selectedSong.getSongLink());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songUri);
                sname = selectedSong.getSongTitle();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause);
                startAnimation(image);
            }
        });


//    updateseekbar=new Thread(new Runnable(){
//        public void run() {
////        int totalDuration = mediaPlayer.getDuration();
////        int currentPosition = 0;
////        while (currentPosition < totalDuration) {
////            try {
////                sleep(500);
////                currentPosition = mediaPlayer.getCurrentPosition();
////                seekmusic.setProgress(currentPosition);
////
////                final int finalCurrentPosition = currentPosition;
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        seekmusic.setProgress(finalCurrentPosition);
////                    }
////                });
//            try {
//                while (mediaPlayer.isPlaying()) {
////                    Thread.sleep(1000);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            int currentPosition = mediaPlayer.getCurrentPosition();
//                            seekmusic.setProgress(currentPosition);
//                            txtsstart.setText(createTime(currentPosition));
//                        }
//                    });
//                }
//            } catch (InterruptedException |IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }
//    });


//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null) {
//                    int currentPosition = mediaPlayer.getCurrentPosition();
//                    seekmusic.setProgress(currentPosition);
//                    txtsstart.setText(createTime(currentPosition));
//                }
//            }
//        };

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekmusic.setProgress(currentPosition);
                    txtsstart.setText(createTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);


        seekmusic.setMax(mediaPlayer.getDuration());
//updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
//
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        String endTime = createTime(mediaPlayer.getDuration());
        txtsstop.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (mediaPlayer != null) {


                    String currentTime = createTime(mediaPlayer.getCurrentPosition());
                    txtsstart.setText(currentTime);
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);


        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+1000);
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    int forwardTime = 10000; // 10 seconds

                    if (currentPosition + forwardTime <= duration) {
                        mediaPlayer.seekTo(currentPosition + forwardTime);
                    } else {
                        mediaPlayer.seekTo(duration);
                    }


                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-1000);


                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int backwardTime = 10000; // 10 seconds

                    if (currentPosition - backwardTime >= 0) {
                        mediaPlayer.seekTo(currentPosition - backwardTime);
                    } else {
                        mediaPlayer.seekTo(0);
                    }


                }
            }
        });



    }













    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(runnable); // Remove the seekbar update runnable

    }






    public void startAnimation(View view){
        ObjectAnimator animator=ObjectAnimator.ofFloat(image,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration)
    {
        String time="";
        int min=duration /1000 / 60;
        int sec=duration /1000 % 60;

//        time+=min+":";
//
//        if(sec<10){
//            time+="0";
//        }
//        time+=sec;

        time += String.format(Locale.getDefault(),"%02d:%02d", min, sec);

        return time;

    }}







