package io.walter.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.walter.manager.fragments.ReceiptDialogFragment;
import io.walter.manager.fragments.ReceiptFragment;
import io.walter.manager.fragments.SalesFragment;


public class SalesActivity extends AppCompatActivity implements SalesFragment.OnShoppingBasketSelectedListener, ReceiptDialogFragment.ItemQuantityChangedListener{

    public static final String SALES_FRAGMENT = "sales_fragment";
    public static final String RECEIPT_FRAGMENT = "receipt_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        //show the receipt fragment
        ReceiptFragment savedFragment = (ReceiptFragment) getSupportFragmentManager().findFragmentByTag(RECEIPT_FRAGMENT);
        if (savedFragment == null) {
            ReceiptFragment listFragment = new ReceiptFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeHolderReports, listFragment, RECEIPT_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onShoppingBasketClicked() {

        ReceiptFragment receiptFragment = new ReceiptFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeHolderReports, receiptFragment, RECEIPT_FRAGMENT);
        fragmentTransaction.addToBackStack("added");
        fragmentTransaction.commit();

    }

    @Override
    public void onQuantityChanged(int code, int quantity) {
        ReceiptFragment receiptFragment = (ReceiptFragment) getSupportFragmentManager().findFragmentByTag(RECEIPT_FRAGMENT);
        if (receiptFragment!=null)
        {
            Log.d("FRAGMENT","NOT NULL");
            receiptFragment.refresh();
        }
        else
        {
            Log.d("FRAGMENT","NULL");
        }
    }
    public void openContacts(View view) {
       // startActivity(new Intent(this, AddressBookActivity.class));
    }
    public void openInventory(View view) {
        startActivity(new Intent(this, StocksActivity.class));
    }
    
}