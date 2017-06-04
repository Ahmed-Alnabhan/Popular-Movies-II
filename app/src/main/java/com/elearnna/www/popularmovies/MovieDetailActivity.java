package com.elearnna.www.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//
        setContentView(R.layout.activity_movie_detail);
        customizeToolBar();
        MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_movie_detail, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void customizeToolBar() {

        // Make a full screen activity
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // Remove activity's title.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Make the toolbar transparent
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(256, 0, 0, 0)));
    }

}
