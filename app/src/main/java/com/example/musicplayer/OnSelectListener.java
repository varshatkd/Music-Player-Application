package com.example.musicplayer;

import java.io.File;

public interface OnSelectListener {
    void OnSelected(File file);
    void onDelete(File file);
    void onRename(File file);
    void onShare(File file);
}
