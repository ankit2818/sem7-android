package com.zhulie.zhulie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private NavigationView navView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /** Check SharedPreferences */
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("zhulie", 0);
    if (!sharedPreferences.getBoolean("loggedIn", false)) {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      finish();
    }

    /**
     * write code to get user email id to search for the projects user is part of.
     * */

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    drawerLayout = findViewById(R.id.drawerLayout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    navView = findViewById(R.id.navView);
    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
    navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment selectedFragment = null;
        switch (id) {
          case R.id.home:
            selectedFragment = new HomeFragment();
            break;
          case R.id.setting:
            selectedFragment = new SettingFragment();
            break;
          case R.id.notifications:
            selectedFragment = new NotificationFragment();
            break;
          case R.id.logout:
            logout();
            break;
        }
        if(selectedFragment != null) {
          getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
      }
    });
  }

  private void logout() {
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("zhulie", 0);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.clear();
    editor.apply();
    startActivity(new Intent(MainActivity.this, LoginActivity.class));
    finish();
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
}
