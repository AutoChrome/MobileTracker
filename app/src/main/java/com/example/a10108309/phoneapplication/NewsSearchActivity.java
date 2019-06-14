package com.example.a10108309.phoneapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a10108309.database.DatabaseHandler;

import java.util.ArrayList;

public class NewsSearchActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    DatabaseHandler db;

    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_search);
        db = new DatabaseHandler(this);
        fragmentManager = getSupportFragmentManager();
        fragment = new NewsFragment();
        fragmentManager.beginTransaction().add(R.id.news_search_frame_layout, fragment, "searchArticleFragment").commit();
        configureActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_filter:
                showFilterDialog();
                return true;
        }
        return false;
    }

    private void showFilterDialog(){
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.setOnFragmentDismissListener(this);
        filterDialog.show(fm, "filter_dialog");
    }

    public void configureActionBar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                if(menuItem.getItemId() == R.id.nav_home){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_phoneSearch){
                    Intent intent = new Intent(getApplicationContext(), PhoneSearchActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_clear){
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewsSearchActivity.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", null).show();
                }
                return false;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences sharedPreferences = getSharedPreferences("phoneCache", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear().commit();
                    db.clearFavourites();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(NewsSearchActivity.this, "Favourites cleared!", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDialogDismiss(ArrayList<String> query) {
        this.query = query.get(0);

        fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("query", this.query);
        fragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("searchArticleFragment") != null){
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("searchArticleFragment")).commit();
        }

        fragmentManager.beginTransaction().add(R.id.news_search_frame_layout, fragment, "searchArticleFragment").commit();
    }
}
