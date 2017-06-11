package com.rayzhang.android.rayzhangalbum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rayzhang.android.rzalbum.RZAlbum;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private final int REQUEST_RZALBUM = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "RZAlbumActivity");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                RZAlbum.ofLimitCount(2)
                        .ofSpanCount(3)
                        .withStatusBarColor(Color.parseColor("#AD1457"))
                        .withToolBarColor(Color.parseColor("#D81B60"))
                        .withToolBarTitle("Album")
                        .start(this, REQUEST_RZALBUM);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_RZALBUM:
                    List<String> paths = RZAlbum.parseResult(data);
                    Log.d(TAG, "Main getPath:" + paths);
                    break;
            }
        }
    }
}
