package com.solaredge.diagnostictool;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        Toast.makeText(getApplicationContext(), "page1", Toast.LENGTH_SHORT).show();
                    case R.id.page_2:
                        switchActivities();
                    default:
                        Toast.makeText(getApplicationContext(), "Select someone", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, TestFotocamera.class);
        startActivity(switchActivityIntent);
    }
}