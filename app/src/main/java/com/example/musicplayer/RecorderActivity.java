package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.musicplayer.Adapter.ViewPageAdapter;
import com.example.musicplayer.Fragments.RecorderFragment;
import com.example.musicplayer.Fragments.RecordingFragment;
import com.google.android.material.tabs.TabLayout;

public class RecorderActivity extends AppCompatActivity {
private Toolbar toolbar;
private ViewPager viewPager;
private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);


        toolbar=findViewById(R.id.toolbar);
        viewPager=findViewById(R.id.viewPage);
        tabLayout=findViewById(R.id.tabLayout);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new RecorderFragment(),"Recorder");
        viewPageAdapter.addFragment(new RecordingFragment(),"Recording");
        viewPager.setAdapter(viewPageAdapter);
    }
}