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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;

import java.util.ArrayList;

public class PhoneSearchActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener, PhoneDisplayListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    protected Fragment searchFragment, detailsFragment;
    protected FragmentManager fragmentManager;
    protected FrameLayout frameLayout;
    private DatabaseHandler db;

    private String brand = "";
    private String device = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_search);
        db = new DatabaseHandler(this);
        configureActionBar();
        if(savedInstanceState != null){
            if(savedInstanceState.getString("device") != null){
                this.device = savedInstanceState.getString("device");
            }
            if(savedInstanceState.getString("brand") != null){
                this.brand = savedInstanceState.getString("brand");
            }
        }
        if(device.length() > 0 && brand.length() > 0){
            searchFragment = new PhoneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device", this.device);
            bundle.putString("brand", this.brand);
            searchFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.phone_fragment_container, searchFragment, "searchFragment").commit();
        }else if(device.length() > 0){
            searchFragment = new PhoneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device", this.device);
            searchFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.phone_fragment_container, searchFragment, "searchFragment").commit();
        }else{
            searchFragment = new PhoneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("device", "getLatest");
            searchFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.phone_fragment_container, searchFragment, "searchFragment").commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.device = savedInstanceState.getString("device");
        this.brand = savedInstanceState.getString("brand");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("device", device);
        outState.putString("brand", brand);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.right_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                }else if(menuItem.getItemId() == R.id.nav_newsSearch){
                    Intent intent = new Intent(getApplicationContext(), NewsSearchActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_clear){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneSearchActivity.this);
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
                    Toast.makeText(PhoneSearchActivity.this, "Favourites cleared!", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    public void displayPhoneLandscape(Phone phone){
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

    private void showFilterDialog(){
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.setOnFragmentDismissListener(this);
        filterDialog.show(fm, "filter_dialog");
    }

    @Override
    public void onDialogDismiss(ArrayList<String> query) {
        this.device = query.get(0);
        this.brand = query.get(1);
        searchFragment = new PhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("device", query.get(0));
        bundle.putString("brand", query.get(1));
        searchFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("searchFragment") != null){
            Fragment remove_fragment = fragmentManager.findFragmentByTag("searchFragment");
            fragmentManager.beginTransaction().remove(remove_fragment).commit();
        }

        fragmentManager.beginTransaction().add(R.id.phone_fragment_container, searchFragment, "searchFragment").commit();
    }
}
