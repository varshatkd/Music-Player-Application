package com.example.musicplayer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecViewHolder extends RecyclerView.ViewHolder{
    public TextView tvName;
    public LinearLayout container;
    public Button deleteButton;
    public Button renameButton;
    public Button shareButton;
    public RecViewHolder(@NonNull View itemView) {
        super(itemView);

        deleteButton = itemView.findViewById(R.id.deleteButton);
        renameButton = itemView.findViewById(R.id.renameButton);
        shareButton=itemView.findViewById(R.id.shareButton);
        tvName=itemView.findViewById(R.id.txtName);
        container=itemView.findViewById(R.id.container);
    }
}
