package com.mayi.flipboardsheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.mayi.library.FlipboardSheetView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FlipboardSheetView mFlipboardSheetView;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlipboardSheetView = (FlipboardSheetView) findViewById(R.id.flipboard_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SimpleAdapter(this));
        mFlipboardSheetView.addStateChangeCallback(new FlipboardSheetView.OnStateChangeCallback() {
            @Override
            public void onStateChange(@FlipboardSheetView.State int state) {
                switch (state) {
                    case FlipboardSheetView.HIDDEN:
                        Log.i(TAG, "onStateChange: HIDDEN");
                        break;
                    case FlipboardSheetView.COLLAPSED:
                        Log.i(TAG, "onStateChange: COLLAPSED");
                        break;
                    case FlipboardSheetView.EXPANDED:
                        Log.i(TAG, "onStateChange: EXPANDED");
                        break;
                }
            }
        });
    }

    public void click(View view) {
        mFlipboardSheetView.switchState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(new SimpleAdapter(this));
                break;
            case R.id.item_2:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                mRecyclerView.setAdapter(new SimpleAdapter(this));
                break;
            case R.id.item_3:
                mFlipboardSheetView.setInterpolator(new BounceInterpolator());
                break;
        }
        return true;
    }
}
