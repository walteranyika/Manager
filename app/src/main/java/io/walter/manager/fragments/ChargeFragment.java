package io.walter.manager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.SalesActivity;
import io.walter.manager.models.DailySale;
import io.walter.manager.models.Product;
import io.walter.manager.models.PurchaseSummary;
import io.walter.manager.models.PurchasedItem;
import io.walter.manager.models.TemporaryItem;
import io.walter.manager.reportingsql.SalesDatabase;
import io.walter.manager.utils.CalendarUtils;

/**
 * Created by walter on 7/11/17.
 */

public class ChargeFragment extends Fragment {
    boolean charged=false;
    Realm myRealm;
    int purchase_summary_id=1;
    int purchase_item_id=1;
    SalesDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charge_fragment, container, false);
        ((SalesActivity) getActivity()).getSupportActionBar().setTitle("Checkout");
        final Button btnCharge = (Button) view.findViewById(R.id.buttonCharge);
        TextView textViewAmount = (TextView) view.findViewById(R.id.tvChargeAmount);
        final ImageView imgCheck= (ImageView) view.findViewById(R.id.imgCheck);
        final TextView textViewStatus = (TextView) view.findViewById(R.id.tvChargeStatus);
        db=new SalesDatabase(getContext());
        myRealm=Realm.getInstance(getContext());
        getLastSummaryId();
        textViewAmount.setText("KES " +getProductsTotalCost());
        if(getProductsTotalCost()<1){
            btnCharge.setEnabled(false);
            textViewAmount.setText("No items in the shopping cart");
        }
        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (getProductsTotalCost()>0) {
                if (!charged) {
                    long purchaseDate = System.currentTimeMillis();
                    int date = (int) purchaseDate;
                    Date tarehe=new Date(purchaseDate);
                    String raw_date = CalendarUtils.ConvertToDateString(purchaseDate);
                    String month = CalendarUtils.ConvertToMonthString(purchaseDate);
                    ArrayList<TemporaryItem> data = getProducts();
                    String today=CalendarUtils.ConvertToPureDateString(purchaseDate);
                    double cost = getProductsTotalCost();
                    myRealm.beginTransaction();
                    PurchaseSummary summary = new PurchaseSummary(purchase_summary_id, cost, tarehe, month, raw_date, 1);
                    PurchaseSummary purchaseSummary = myRealm.copyToRealm(summary);
                    myRealm.commitTransaction();
                    for (TemporaryItem p : data) {
                        getPurchasedItemId();
                        myRealm.beginTransaction();
                        PurchasedItem item = new PurchasedItem(purchase_item_id, p.getTitle(), p.getPrice(), p.getQuantity(), date, month, raw_date);
                        PurchasedItem managedItem = myRealm.copyToRealm(item);
                        purchaseSummary.getPurchasedItems().add(managedItem);
                        updateProductQuantity(p.getCode(), p.getQuantity());
                        myRealm.commitTransaction();
                        saveToSQL(p,db,today);
                    }

                    myRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.clear(TemporaryItem.class);
                        }
                    });
                    textViewStatus.setTextColor(Color.parseColor("#00a65a"));
                    textViewStatus.setText("Transaction Successfull");
                    textViewStatus.setScaleX(0);
                    textViewStatus.setScaleY(0);
                    textViewStatus.animate().setDuration(1500)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .scaleY(1)
                            .scaleX(1)
                            .start();

                    imgCheck.setVisibility(View.VISIBLE);
                    imgCheck.setScaleX(0);
                    imgCheck.setScaleX(0);
                    imgCheck.animate().setDuration(800)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .scaleY(1)
                            .scaleX(1)
                            .start();

                    charged = true;

                    btnCharge.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Item Already Charged", Toast.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(textViewStatus,"No Items in the cart",Snackbar.LENGTH_SHORT).show();
            }

            }
        });
        return view;
    }
    public double getProductsTotalCost(){
        double total=0;
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items =myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item:items){
            total+=item.getQuantity()*item.getPrice();
        }
        myRealm.commitTransaction();
        return  total;
    }
    public ArrayList<TemporaryItem> getProducts(){
        ArrayList<TemporaryItem> data=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items =myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item:items){
            data.add(item);
        }
        myRealm.commitTransaction();
        return  data;
    }
    private void getLastSummaryId(){
        RealmResults<PurchaseSummary> results = myRealm.where(PurchaseSummary.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            purchase_summary_id = myRealm.where(PurchaseSummary.class).max("code").intValue() + 1;
        else
            purchase_summary_id=1;//starting ID
        myRealm.commitTransaction();
    }
    private void getPurchasedItemId(){
        RealmResults<PurchasedItem> results = myRealm.where(PurchasedItem.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            purchase_item_id = myRealm.where(PurchasedItem.class).max("code").intValue() + 1;
        else
            purchase_item_id=1;//starting ID
        myRealm.commitTransaction();
    }
    private  void updateProductQuantity(int code,int quantity){
        RealmResults<Product> results = myRealm.where(Product.class).equalTo("code",code).findAll();
        results.get(0).setQuantity(results.get(0).getQuantity()-quantity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }

    private void saveToSQL(TemporaryItem item, SalesDatabase db,String date){
        //(String sale_date, String item, double quantity, double pri
        DailySale sale=new DailySale(date, item.getTitle(), item.getQuantity(), item.getPrice());
        db.saveData(sale.getSale_date(),sale.getItem(),sale.getQuantity(),sale.getPrice());

    }
}
