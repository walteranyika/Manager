package io.walter.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //https://github.com/dkmeteor/CircleAnimation
    //https://stackoverflow.com/questions/43194243/notification-badge-on-action-item-android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void salesBtn(View view) {
        startActivity(new Intent(this, SalesActivity.class));
    }

    public void inventoryBtn(View view) {
        startActivity(new Intent(this, StocksActivity.class));
    }

    public void reportsBtn(View view) {
        startActivity(new Intent(this, ReportsActivity.class));
    }

    public void quotesBtn(View view) {
        startActivity(new Intent(this, QuotesOrdersActivity.class));
    }

    public void solidBtn(View view) {
        startActivity(new Intent(this, SolidActivity.class));
    }

    public void tutorialsBtn(View view) {
        startActivity(new Intent(this, TutorialsActivity.class));
    }

    public void addressBtn(View view) {
       // startActivity(new Intent(this, AddressBookActivity.class));
    }

    public void settingsBtn(View view) {
       startActivity(new Intent(this, SettingsActivity.class));
    }
}
