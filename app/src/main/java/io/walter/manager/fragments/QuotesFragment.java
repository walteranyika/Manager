package io.walter.manager.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.adapters.OrdersListAdapter;
import io.walter.manager.models.Order;

public class QuotesFragment extends Fragment {
    Realm myRealm;
    OrdersListAdapter adapter;
    ArrayList<Order> data;
    ListView mListView;
    public QuotesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quotes, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listViewOrders);
        myRealm=Realm.getInstance(getContext());
        data= getQuoteSummary();
        adapter=new OrdersListAdapter(getActivity(),data,"Quote");
        mListView.setAdapter(adapter);
        return rootView;
    }
    private ArrayList<Order> getQuoteSummary() {
        ArrayList<Order> orders=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<Order> results=myRealm.where(Order.class).equalTo("type","quote").findAll();
        Log.d("RESULTS","SIZE IS "+results.size());
        if (results.size()>0)
        {
            for (Order o: results){
                orders.add(o);
            }
        }
        myRealm.commitTransaction();
        return  orders;
    }
}
