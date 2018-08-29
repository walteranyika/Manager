package io.walter.manager.reports;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.ReportsActivity;
import io.walter.manager.adapters.DailySalesListAdapter;
import io.walter.manager.models.DailySaleSummary;
import io.walter.manager.models.PurchaseSummary;
import io.walter.manager.reportingsql.SalesDatabase;
import io.walter.manager.utils.CalendarUtils;

public class DailySalesFragment extends Fragment {

    TextView tvFromDate, tvToDate;
    ListView listDailySales;
    DailySalesListAdapter adapter;
    ArrayList<DailySaleSummary> data;
    Realm myRealm;
    SalesDatabase db;

    public DailySalesFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_sales, container, false);
        ((ReportsActivity) getActivity()).getSupportActionBar().setTitle("Daily Sales Report");
        tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
        db=new SalesDatabase(getContext());
        tvToDate = (TextView) view.findViewById(R.id.tvToDate);
        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromDialog();
            }
        });
        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToDialog();
            }
        });
        myRealm=Realm.getInstance(getContext());
        listDailySales= (ListView) view.findViewById(R.id.listDailySales);
        data = db.getData();
        adapter=new DailySalesListAdapter(getContext(), data,"Daily Sales");
        listDailySales.setAdapter(adapter);
        return view;
    }

    private ArrayList<PurchaseSummary> getPurchaseSummary() {
        ArrayList<PurchaseSummary> items=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<PurchaseSummary> results = myRealm.where(PurchaseSummary.class).findAll();
        for (int i = 0; i < results.size(); i++) {
            items.add(results.get(i));

            Log.d("SIZE_SUMMARY", "getPurchaseSummary: COUNT "+results.get(i).getPurchasedItems().size());
        }
        myRealm.commitTransaction();
        return items;
    }

    private ArrayList<PurchaseSummary> getSummaryInRange(Date start, Date end) {
        ArrayList<PurchaseSummary> items=new ArrayList<>();
        //.between("purchase_date",start,end)
        if(start!=end) {
            RealmResults<PurchaseSummary> results = myRealm.where(PurchaseSummary.class).greaterThanOrEqualTo("purchase_date", start).lessThanOrEqualTo("purchase_date", end).findAll();
            myRealm.beginTransaction();
            Log.d("ITEMS", "getSummaryInRange: SIZE  " + results.size());
            for (int i = 0; i < results.size(); i++) {
                items.add(results.get(i));
                Log.d("TAG_PURCHASE", "Date " + results.get(i).getRaw_date());
            }
        }else {

            RealmResults<PurchaseSummary> results = myRealm.where(PurchaseSummary.class).greaterThanOrEqualTo("purchase_date", start).lessThanOrEqualTo("purchase_date", end).findAll();
            myRealm.beginTransaction();
            Log.d("ITEMS", "getSummaryInRange: SIZE  " + results.size());
            for (int i = 0; i < results.size(); i++) {
                items.add(results.get(i));
                Log.d("TAG_PURCHASE", "Date " + results.get(i).getRaw_date());
            }
        }
        myRealm.commitTransaction();
        return items;
    }

    long from=0,to=0;


    private void showToDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                Log.d("TAG",dayOfMonth+" "+month+" "+year);
                tvToDate.setText("To "+dayOfMonth+"-"+month+"-"+year);
                to= CalendarUtils.ConvertFromDateToLong(dayOfMonth+"/"+month+"/"+year);
                Log.d("TAG_DATE", "from "+from+" "+to);
                if(to==from){
                    long day=24*60*60*1000;
                    to+= day;
                }
                Date dateFrom = new Date(from);
                Date dateTo=new Date(to);
                data.clear();
                //data.addAll(getSummaryInRange(dateFrom,dateTo));
                adapter.notifyDataSetChanged();

            }
        }, 2017, 7, 1);
        datePickerDialog.setMessage("To Date");
        datePickerDialog.getDatePicker().setMinDate(from);
        datePickerDialog.show();
    }

    public void showFromDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                Log.d("TAG",dayOfMonth+" "+month+" "+year);
                tvFromDate.setText("From "+dayOfMonth+"-"+month+"-"+year);
                from= CalendarUtils.ConvertFromDateToLong(dayOfMonth+"/"+month+"/"+year);
            }
        }, 2017, 7, 1);
        datePickerDialog.setMessage("From Date");
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

}
