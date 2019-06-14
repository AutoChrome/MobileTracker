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
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;

public class PhoneDetailsActivity extends AppCompatActivity {

    Phone phone;
    FrameLayout frameLayout;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        frameLayout = findViewById(R.id.phone_details_framelayout);
        Bundle data = getIntent().getExtras().getBundle("data");
        phone = (Phone) data.getParcelable("phone");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment phone_details_fragment = new PhoneDisplayFragment();
        phone_details_fragment.setArguments(data);
        if(fragmentManager.findFragmentByTag("phone_details_fragment") == null){
            fragmentManager.beginTransaction().add(R.id.phone_details_framelayout, phone_details_fragment, "phone_details_fragment").commit();
        }
        setContentView(R.layout.activity_phone_details);
        configureActionBar();
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
                }else if(menuItem.getItemId() == R.id.nav_newsSearch){
                    Intent intent = new Intent(getApplicationContext(), NewsSearchActivity.class);
                    startActivity(intent);
                    finish();
                }else if(menuItem.getItemId() == R.id.nav_clear){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneDetailsActivity.this);
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
                    Toast.makeText(PhoneDetailsActivity.this, "Favourites cleared!", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    public Phone getPhone(){
        return this.phone;
    }
}
