package com.example.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.PlaySongActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.RecViewHolder;
import com.example.musicplayer.OnSelectListener;

import java.io.File;
import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecViewHolder> {
    private Context context;
    private List<File> fileList;
    private OnSelectListener listener;

    public RecAdapter(Context context, List<File> fileList, OnSelectListener listener) {
        this.context = context;
        this.fileList = fileList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File file = fileList.get(position);
        holder.tvName.setText(fileList.get(position).getName());
        holder.tvName.setSelected(true);
        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.OnSelected(fileList.get(position));
//                playRecording(fileList.get(position));
            }

//            private void playRecording(File file) {
//                Intent intent = new Intent(context, PlaySongActivity.class);
//                intent.putExtra("file_path", file.getAbsolutePath());
//                context.startActivity(intent);
//            }
        });
        holder.deleteButton.setOnClickListener(view -> {
            listener.onDelete(file);
            fileList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fileList.size());
        });


//        holder.container.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
//            menu.setHeaderTitle("Options");
//            menu.add(0, position, 0, "Delete");
//            menu.add(0, position, 0, "Rename");
//        });
//        holder.renameButton.setOnClickListener(view -> {
//            // Handle rename functionality here
//            Toast.makeText(context, "Rename clicked for: " + file.getName(), Toast.LENGTH_SHORT).show();
//        });

        holder.renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = fileList.get(position);
                String currentName = file.getName();

                // Create an AlertDialog with an EditText for the new name
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Rename Recording");

                final EditText input = new EditText(context);
                input.setText(currentName);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString().trim();
                        if (!newName.isEmpty()) {
                            File newFile = new File(file.getParentFile(), newName);
                            boolean renamed = file.renameTo(newFile);

                            if (renamed) {
                                // Update the file list and notify the adapter
                                fileList.set(position, newFile);
                                notifyItemChanged(position);
                            }
                            else {
                                Toast.makeText(context, "Failed to rename the recording", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });




        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                File file = fileList.get(position);

                Uri fileUri = FileProvider.getUriForFile(context, "com.example.musicplayer.provider", file);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent, "Share Recording"));
            }
        });


    }

//    private void playRecording(File file) {
//
//            Intent intent = new Intent(context, PlaySongActivity.class);
//            intent.putExtra("file_path", file.getAbsolutePath());
//            context.startActivity(intent);
//
//    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
