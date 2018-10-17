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

public class DailyDetailsFragment extends Fragment {

    TextView tvFromDate, tvToDate;
    ListView listDailySales;
    DailySalesListAdapter adapter;
    ArrayList<DailySaleSummary> data;
    SalesDatabase db;

    public DailyDetailsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_sales, container, false);
        ((ReportsActivity) getActivity()).getSupportActionBar().setTitle("Daily Details Sales Report");
        tvFromDate =  view.findViewById(R.id.tvFromDate);
        db=new SalesDatabase(getContext());
        tvToDate = view.findViewById(R.id.tvToDate);
        listDailySales= view.findViewById(R.id.listDailySales);
        data = db.getData();
        adapter=new DailySalesListAdapter(getContext(), data,"Daily Details Sales");
        listDailySales.setAdapter(adapter);
        return view;
    }


}
