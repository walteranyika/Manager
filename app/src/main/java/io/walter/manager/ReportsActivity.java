package io.walter.manager;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.walter.manager.fragments.ItemsFragment;
import io.walter.manager.fragments.ReceiptFragment;
import io.walter.manager.reports.DailySalesFragment;
import io.walter.manager.reports.ItemsReportFragment;


public class ReportsActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {

    private static final String DAILY_SALES_FRAGMENT = "DAILY_SALES_FRAGMENT";
    private static final String INVENTORY_REPORT = "INVENTORY_REPORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DailySalesFragment dailySalesFrag= (DailySalesFragment) getSupportFragmentManager().findFragmentByTag(DAILY_SALES_FRAGMENT);

        if(dailySalesFrag==null){
            DailySalesFragment frag=new DailySalesFragment();
            FragmentManager fragManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.add(R.id.placeHolderReports, frag,DAILY_SALES_FRAGMENT);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.reports, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager= this.getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if (id == R.id.nav_daily_reports) {
            // Handle the camera action
        } else if (id == R.id.nav_category_reports) {

        } else if (id == R.id.nav_top_selling_reports) {

        } else if (id == R.id.nav_inventory_reports) {
          //
            getSupportActionBar().setTitle("Inventory Report");
            ItemsReportFragment itemsFragment=new ItemsReportFragment();
            transaction.replace(R.id.placeHolderReports,itemsFragment);
            transaction.commit();
        } else if (id == R.id.nav_daily_charts) {

        } else if (id == R.id.nav_monthly_charts) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
