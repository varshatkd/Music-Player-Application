package com.example.musicplayer.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.musicplayer.Manifest;
import com.example.musicplayer.Adapter.RecAdapter;
import com.example.musicplayer.OnSelectListener;
import com.example.musicplayer.PlaySongActivity;
import com.example.musicplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import com.karumi.dexter.Dexter;

public class RecordingFragment extends Fragment implements OnSelectListener {


    private static final int REQUEST_PERMISSION_CODE = 1001;


    private RecyclerView recyclerView;
    private List<File> fileList;
    private RecAdapter recAdapter;
//    File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MPVRecorder");
    private File path;
    View view;
    public RecordingFragment(){ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_recording,container,false);

        path = new File(requireActivity().getExternalFilesDir(null), "MPVRecorder");
        checkPermissions();

//        displayFiles();
        return view;
    }





    private void checkPermissions() {
        String[] permissions = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            displayFiles();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_PERMISSION_CODE);
        }
    }








    private void displayFiles() {

        recyclerView=view.findViewById(R.id.recycler_records);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        fileList=new ArrayList<>();
        fileList.addAll(findFile(path));
        recAdapter=new RecAdapter(getContext(),fileList,this);
        recyclerView.setAdapter(recAdapter);
    }

    public ArrayList<File> findFile(File file){
        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
    if(files!=null){
        for(File singleFile:files){
            if(singleFile.getName().toLowerCase().endsWith(".amr")){
                arrayList.add(singleFile);
            }}
        }
        return arrayList;
    }

    @Override
    public void OnSelected(File file) {
//Uri uri= FileProvider.getUriForFile(getContext(),getContext().getApplicationContext().getPackageName()+".provider",file);
        Uri uri= FileProvider.getUriForFile(requireContext(),requireContext().getPackageName()+".provider",file);


        Intent i=new Intent(Intent.ACTION_VIEW);
    i.setDataAndType(uri,"audio/x-wav");
    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    getContext().startActivity(i);
        startActivity(i);

    }

    @Override
    public void onDelete(File file) {
        if (file.delete()) {
            fileList.remove(file);
            recAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Recording deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to delete recording", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRename(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Rename Recording");

        // Create an EditText view for user input
        EditText editText = new EditText(requireContext());
        editText.setText(file.getName());
        builder.setView(editText);

        // Set positive button to rename the file
        builder.setPositiveButton("Rename", (dialogInterface, i) -> {
            String newName = editText.getText().toString().trim();
            if (!newName.isEmpty()) {
                File newFile = new File(file.getParent(), newName);
                if (file.renameTo(newFile)) {
                    Toast.makeText(getContext(), "Recording renamed successfully", Toast.LENGTH_SHORT).show();
                    recAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to rename recording", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button to cancel the dialog
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onShare(File file) {
try {
    Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("audio/x-wav");
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    startActivity(Intent.createChooser(shareIntent, "Share Recording"));
}catch (Exception e){
    Toast.makeText(getContext(), "Failed to share recording", Toast.LENGTH_SHORT).show();
}

    }

//    @Override
//    public void onPlay(File file) {
//        Intent intent = new Intent(getActivity(), PlaySongActivity.class);
//        intent.putExtra("file_path", file.getAbsolutePath());
//        startActivity(intent);
//    }


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
                displayFiles();
            } else {
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Permission denied. Cannot access recordings.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
//            displayFiles();
            checkPermissions();
        }
    }
}
