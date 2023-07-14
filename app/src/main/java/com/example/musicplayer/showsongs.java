 package com.example.musicplayer;

import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
//import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.Model.UploadSong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showsongs extends AppCompatActivity {
ListView listview;
//Button delete;
ArrayList<UploadSong> songTitle=new ArrayList<>();
ArrayList<String> arrayListSongsName=new ArrayList<>();
ArrayList<String> arrayListSongsDuration=new ArrayList<>();
ArrayList<String> arrayListSongsartist=new ArrayList<>();
ArrayAdapter<String> arrayAdapter;
private CustomAdapter customAdapter;

@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsongs);
        listview=findViewById(R.id.myListView);
//        delete=findViewById(R.id.del);

        retriveSongs();

    }



     private void retriveSongs() {

         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the user ID
         if (currentUser != null) {
             String userId = currentUser.getUid();

             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs").child(userId).child(MainActivity.songsCategory);//here child(userId) extra
             databaseReference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     arrayListSongsName.clear();
                     arrayListSongsDuration.clear();
                     arrayListSongsartist.clear();
                     songTitle.clear();
                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
                         UploadSong songobj = ds.getValue(UploadSong.class);
                         if (songobj != null) {
                             arrayListSongsName.add(songobj.getSongTitle());
                             arrayListSongsDuration.add(songobj.getSongDuration());
                             arrayListSongsartist.add(songobj.getArtist());
                             songTitle.add(songobj); // Add the song to the songTitle ArrayList
                         }
                     }
//                listview.setAdapter(arrayAdapter);
                     CustomAdapter customAdapter = new CustomAdapter();
                     listview.setAdapter(customAdapter);
//
                     listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
                             UploadSong selectedSong = songTitle.get(i);
//                        String songName = arrayListSongsName.get(i);
                             Intent intent = new Intent(getApplicationContext(), PlaySongActivity.class);
                             intent.putExtra("pos", i);
                             intent.putParcelableArrayListExtra("songs", songTitle);
//                        intent.putExtra("songname", songName);
                             intent.putExtra("songname", selectedSong.getSongTitle());
                             startActivity(intent);
                         }
                     });
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
         } else {
             Toast.makeText(showsongs.this, "User not authenticated", Toast.LENGTH_SHORT).show();
         }

     }

     class CustomAdapter extends BaseAdapter{


         @Override
         public int getCount() {
             return arrayListSongsName.size();
         }

         @Override
         public Object getItem(int position) {
//             return null;
             return arrayListSongsName.get(position);
         }

         @Override
         public long getItemId(int position) {
//             return 0;
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             View myView=getLayoutInflater().inflate(R.layout.song_item,parent,false);
             TextView textsong=myView.findViewById(R.id.txtsongname);
             textsong.setSelected(true);
             String songName = arrayListSongsName.get(position);
             textsong.setText(songName);


//             delete.setOnClickListener(new View.OnClickListener() {
//                 @Override
//                 public void onClick(View v) {
//                     AlertDialog.Builder builder = new AlertDialog.Builder(showsongs.this);
//                     builder.setTitle("Delete Song");
//                     builder.setMessage("Are you sure you want to delete this song?");
//                     builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                         @Override
//                         public void onClick(DialogInterface dialog, int which) {
//                             deleteSong(position);
//                         }
//                     });
//                     builder.setNegativeButton("Cancel", null);
//                     builder.show();
//                 }
//                 private void deleteSong(int position) {
//
//                     UploadSong selectedSong = songTitle.get(position);
//
//                     // Remove the song from the ArrayList
//                     songTitle.remove(position);
//
//                     // Update the ArrayLists used by the adapter
//                     arrayListSongsName.remove(position);
//                     arrayListSongsDuration.remove(position);
//                     arrayListSongsartist.remove(position);
//
//                     // Update the Firebase database by removing the song
//                     DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs").child(MainActivity.songsCategory);
//                     databaseReference.child(selectedSong.getSongTitle()).removeValue();
//
//                     // Notify the adapter that the data has changed
//                     customAdapter.notifyDataSetChanged();
//
//
//                 }







//             });
             return myView;
         }
     }


//
//                 })
//                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                     public void onClick(DialogInterface dialog, int id) {
//                         // User cancelled the dialog
//                     }
//                 });
//         builder.create().show();
//
//     }

//     private void deleteSong(int position) {
//
//         UploadSong selectedSong = songTitle.get(position);
//         String songId = selectedSong.getmKey();
//
//         DatabaseReference songRef = FirebaseDatabase.getInstance().getReference("songs")
//                 .child(MainActivity.songsCategory).child(songId);
//         songRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//             @Override
//             public void onSuccess(Void aVoid) {
//                 Toast.makeText(showsongs.this, "Song deleted successfully", Toast.LENGTH_SHORT).show();
//
//                 // Remove the song from the lists
//                 arrayListSongsName.remove(position);
//                 arrayListSongsDuration.remove(position);
//                 arrayListSongsartist.remove(position);
//                 songTitle.remove(position);
//
//                 // Notify the adapter about the data change
//                 customAdapter.notifyDataSetChanged();
//             }
//         }).addOnFailureListener(new OnFailureListener() {
//             @Override
//             public void onFailure(@NonNull Exception e) {
//                 Toast.makeText(showsongs.this, "Failed to delete the song", Toast.LENGTH_SHORT).show();
//             }
//         });
//
//     }






 }