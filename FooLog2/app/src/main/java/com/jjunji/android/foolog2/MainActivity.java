package com.jjunji.android.foolog2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jjunji.android.foolog2.util.SharedPreferencesDb;
import com.jjunji.android.foolog2.login.LoginActivity;

import me.huseyinozer.TooltipIndicator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TooltipIndicator indicator;
    TextView txtNavi_Email, txtNavi_nickName;
    String email, nick;
    NavigationView navigationView;
    Fragment[] arr;
    ViewPager viewPager;
    MyPagerAdapter adapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("MainActivity","===================MainActivity"+"Start");
        initView();
        setFragment();
        setAdapter();
        setNaviView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initView(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TooltipIndicator) findViewById(R.id.tooltip_indicator);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setFragment(){
        arr = new Fragment[2];
        arr[0] = CalendarFragment.newInstance(mContext);
        arr[1] = ShowListFragment.newInstance(mContext);
    }

    public void setAdapter(){
        adapter = new MyPagerAdapter(getSupportFragmentManager(), arr);
        viewPager.setAdapter(adapter);
        indicator.setupViewPager(viewPager);
    }

    public void getPreferences(){
        email = SharedPreferencesDb.getId(MainActivity.this, "loginId");
        nick = SharedPreferencesDb.getNickName(MainActivity.this, "nickName");
    }

    public void setNaviView(){
        getPreferences();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtNavi_Email = (TextView) headerView.findViewById(R.id.navNickname);
        txtNavi_nickName = (TextView) headerView.findViewById(R.id.navEmail);
        txtNavi_nickName.setText(email);
        txtNavi_Email.setText(nick);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getBaseContext(), WriteActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {

        } else if (id == R.id.nav_circle_graph) {

        } else if (id == R.id.nav_graph) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            SharedPreferencesDb.DbClear();
            Toast.makeText(MainActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
