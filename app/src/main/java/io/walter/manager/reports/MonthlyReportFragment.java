package io.walter.manager.reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.ReportsActivity;
import io.walter.manager.adapters.MonthlySalesListAdapter;
import io.walter.manager.models.PurchaseSummary;
import io.walter.manager.reportingsql.Database;
import io.walter.manager.reportingsql.MonthlySale;
import io.walter.manager.utils.CalendarUtils;



public class MonthlyReportFragment extends Fragment {

    TextView tvFromDate, tvToDate;
    ListView listDailySales;
    MonthlySalesListAdapter adapter;
    ArrayList<MonthlySale> data;
    Realm myRealm;
    Database db;

    public MonthlyReportFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_sales_report, container, false);
        ((ReportsActivity) getActivity()).getSupportActionBar().setTitle("Monthly Sales Report");
        db=new Database(getContext());
/*        tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
        tvToDate = (TextView) view.findViewById(R.id.tvToDate);*/
      /*  tvFromDate.setOnClickListener(new View.OnClickListener() {
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
        });*/
        db.getData();
        myRealm=Realm.getInstance(getContext());
        listDailySales= (ListView) view.findViewById(R.id.listDailySales);
        data = new ArrayList<>();
        data.addAll(getPurchaseSummary());
        adapter=new MonthlySalesListAdapter(getContext(), data,"Daily Sales");
        listDailySales.setAdapter(adapter);
        return view;
    }

    private ArrayList<MonthlySale> getPurchaseSummary() {
        SimpleDateFormat format=new SimpleDateFormat("yyyy");
        myRealm.beginTransaction();
        RealmResults<PurchaseSummary> results = myRealm.where(PurchaseSummary.class).findAll();
        for (PurchaseSummary ps:results){
           Date d=ps.getPurchase_date();
           String y= format.format(d);
           Log.d("TAG_YEAR", "getYear: "+y);
           db.saveData(ps.getPurchase_month(),ps.getCode(),ps.getTotal_price(),1,y);
        }
        myRealm.commitTransaction();
        return  db.getData();
    }
   /* private ArrayList<PurchaseSummary> getSummaryInRange(Date start, Date end) {
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
    }*/

    long from=0,to=0;


   /* private void showToDialog() {
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
                data.addAll(getSummaryInRange(dateFrom,dateTo));
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
    }*/

}
