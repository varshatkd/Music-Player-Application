package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musicplayer.Model.UploadSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class recycler_activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<UploadSong> mUpload;
//    FirebaseStorage mStorage;
    DatabaseReference reference;
    MediaPlayer mediaPlayer;
    ValueEventListener valueEventListener;
    SongAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpload=new ArrayList<>();

//        List<UploadSong> mUpload;
        adapter=new SongAdapter(recycler_activity.this,mUpload);
        recyclerView.setAdapter(adapter);
        reference= FirebaseDatabase.getInstance().getReference().child("songs");

        valueEventListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUpload.clear();
                for (DataSnapshot dss:snapshot.getChildren()){
                    UploadSong uploadSong=dss.getValue(UploadSong.class);
                    uploadSong.setmKey(dss.getKey());
                    mUpload.add(uploadSong);
                }
                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(recycler_activity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(valueEventListener);
    }

    public void playSong(List<UploadSong> arrayListSong, int adapterPosition) throws IOException {

        UploadSong uploadSong=arrayListSong.get(adapterPosition);
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setDataSource(uploadSong.getSongLink());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayertwo) {
                mediaPlayertwo.start();

            }
        });

            mediaPlayer.prepareAsync();
    }
}