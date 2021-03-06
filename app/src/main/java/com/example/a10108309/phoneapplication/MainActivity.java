package com.example.a10108309.phoneapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;
import com.example.a10108309.service.NewsSearchService;


public class MainActivity extends AppCompatActivity implements PhoneDisplayListener{

    private TabLayout tabLayout;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    DatabaseHandler db;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        configureActionBar();
        configureTabLayout();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }
    public void configureTabLayout(){
        tabLayout = findViewById(R.id.MainActivity_tab_layout);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#e2e2e2"));
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPageAdapter);
        tabLayout.setupWithViewPager(mViewPager);
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
                if(menuItem.getItemId() == R.id.nav_phoneSearch){
                    Intent intent = new Intent(getApplicationContext(), PhoneSearchActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_newsSearch){
                    Intent intent = new Intent(getApplicationContext(), NewsSearchActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_clear){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    Toast.makeText(MainActivity.this, "Favourites cleared!", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    @Override
    public void displayPhoneLandscape(Phone phone) {
        fragmentManager = getSupportFragmentManager();
        PhoneDisplayFragment fragment = new PhoneDisplayFragment();
        if(fragmentManager.findFragmentByTag("details_fragment") != null){
            Fragment remove_fragment = fragmentManager.findFragmentByTag("details_fragment");
            fragmentManager.beginTransaction().remove(remove_fragment).commit();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("phone", phone);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.phone_details_framelayout, fragment, "details_fragment").commit();
    }
}
