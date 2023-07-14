package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.Model.UploadSong;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

public String uploadId;
Button openalbum;
Button logout;
Button recorder;
    public static TextView textViewImage;
    ProgressBar progressBar;
    Uri audioUri;
    StorageReference mStorageref;
    StorageTask mUploadsTask;
    DatabaseReference referenceSongs;
    FirebaseAuth fauth;
    public static String songsCategory;
    MediaMetadataRetriever metadataRetriever;
    byte[] art;
    String title1,artist1,album_art1="",durations1;
    TextView title,artist,album,durations,dataa;
    ImageView album_art;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout=findViewById(R.id.logout);
        textViewImage=findViewById(R.id.textViewSongsFileSelected);
        progressBar=findViewById(R.id.progressBar);
        title=findViewById(R.id.title);
        artist=findViewById(R.id.artist);
        durations=findViewById(R.id.duration);
        album=findViewById(R.id.album);
        dataa=findViewById(R.id.dataa);
        album_art=findViewById(R.id.imageview);
        recorder=findViewById(R.id.btnRec);
        fauth=FirebaseAuth.getInstance();

        recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RecorderActivity.class);
                startActivity(intent);
            }
        });


logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

fauth.signOut();
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
});

        openalbum=findViewById(R.id.openimageuploadactivity);
        openalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,recycler_activity.class));
                startActivity(new Intent(MainActivity.this,showsongs.class));

            }
        });



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference songsCollection = firestore.collection("songs");


        metadataRetriever=new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("songs");
        mStorageref= FirebaseStorage.getInstance().getReference().child("songs");
        FirebaseApp.initializeApp(this);

        Spinner spinner=findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);


        List <String> categories =new ArrayList<>();
        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");
        categories.add("Devotional Songs");
        categories.add("Others");

        ArrayAdapter <String> dataAdpter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);

        dataAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdpter);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        songsCategory=adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this,"Selected:"+songsCategory,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void openAudioFiles(View v){
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101&&resultCode==RESULT_OK&&data.getData()!=null){
            audioUri=data.getData();
            String fileName=getFileName(audioUri);
            textViewImage.setText(fileName);


            metadataRetriever.setDataSource(this,audioUri);
            art=metadataRetriever.getEmbeddedPicture();

            if (art != null) {

            Bitmap bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
            album_art.setImageBitmap(bitmap);}
            else{
                album_art.setImageResource(R.drawable.polotno2);

            }


            album.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            durations.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            title.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

            artist1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        }
    }


    @SuppressLint("Range")
    private  String getFileName(Uri uri){
        String result=null;
        if(audioUri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst())
                {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }

        }

        finally{
                if(cursor!=null){
            cursor.close();}
        }
}
        if(result==null){
            result=uri.getPath();
            int cut=result.lastIndexOf('/');
            if(cut!=-1){
                result=result.substring(cut+1);
            }
        }
        return result;
    }

    public void uploadFileTofirebase(View v){
        if(textViewImage.equals("No File Selected")){
            Toast.makeText(this,"please select an image!",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mUploadsTask!=null&&mUploadsTask.isInProgress()){
                Toast.makeText(this,"Songs upload is in progress!",Toast.LENGTH_SHORT).show();
            }
            else{
                uploadFiles();
            }
        }
    }




    public void uploadFiles() {
        if (audioUri != null) {
            Toast.makeText(this, "Uploading! Please Wait", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);

            final StorageReference storageReference = mStorageref.child(System.currentTimeMillis() + "." + getfileextension(audioUri));
            mUploadsTask = storageReference.putFile(audioUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                boolean hasThumbnail =art!=null;
                                        UploadSong uploadSong = new UploadSong(songsCategory, title1, artist1, album_art1, durations1, uri.toString());
//                        UploadSong uploadSong = new UploadSong(title1, artist1, album_art1, durations1, uri.toString());



                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();


//                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the user ID
                        DatabaseReference userSongsRef = referenceSongs.child(userId).child(songsCategory); // Create a reference to the user's songs under their user ID and songs category
                        String uploadId = userSongsRef.push().getKey(); // Generate a unique key for the song
                        userSongsRef.child(uploadId).setValue(uploadSong) // Set the song details under the unique key
                                .addOnCompleteListener(task ->{










//                        String uploadId = referenceSongs.child(songsCategory).push().getKey();
//                        referenceSongs.child(songsCategory).child(uploadId).setValue(uploadSong)
//                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Song uploaded successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to upload song metadata", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                });
                    }


                        else{Toast.makeText(MainActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }



                    ))
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Song upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
        } else {
            Toast.makeText(this, "No file selected to upload", Toast.LENGTH_SHORT).show();
        }
    }




    private String getfileextension(Uri audioUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

    public void openAlbumUploadsActivity(View view){
//        Intent intent=new Intent(MainActivity.this,recycler_activity.class);
        Intent intent=new Intent(MainActivity.this,showsongs.class);

        startActivity(intent);

    }
}