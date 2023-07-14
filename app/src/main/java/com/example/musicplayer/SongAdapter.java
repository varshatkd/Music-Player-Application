package com.example.musicplayer;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Model.Upload;
import com.example.musicplayer.Model.UploadSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter  extends RecyclerView.Adapter<SongAdapter.SongsAdapterViewHolder> {

    Context context;
    ArrayList<UploadSong> arrayListSong;

    public SongAdapter(Context context, List<UploadSong> arrayListSong){
        this.context=context;
        this.arrayListSong= (ArrayList<UploadSong>) arrayListSong;
    }
    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(context).inflate(R.layout.song_item,parent,false);
//        return new SongsAdapterViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapterViewHolder holder, int position) {

        UploadSong uploadSong=arrayListSong.get(position);
        holder.textViewTitle.setText(uploadSong.getSongTitle());
        holder.textViewduration.setText(uploadSong.getSongDuration());
        holder.textViewartist.setText(uploadSong.getArtist());

    }

    @Override
    public int getItemCount() {

        return arrayListSong.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView textViewTitle,textViewduration,textViewartist;
        ImageView simage;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
//            textViewTitle=itemView.findViewById(R.id.SongTitle);
//            textViewduration=itemView.findViewById(R.id.SongDuration);
//            textViewartist=itemView.findViewById(R.id.Artist);
            simage=itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                try {
                    ((recycler_activity) context).playSong(arrayListSong, getAdapterPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
