package io.walter.manager.reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.AddItemActivity;
import io.walter.manager.R;
import io.walter.manager.adapters.InventoryListAdapter;
import io.walter.manager.models.Product;

/**
 * Created by walter on 7/22/17.
 */

public class ItemsReportFragment extends Fragment {

    InventoryListAdapter adapter;
    ArrayList<Product> data;
    Realm myRealm;
    TextView tvEmpty;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.items_fragment, container, false);
        myRealm = Realm.getInstance(getContext());
        listView= (ListView) view.findViewById(R.id.inventory_items_list);
        tvEmpty = (TextView) view.findViewById(R.id.textEmpty);
        data = new ArrayList<>();
        adapter = new InventoryListAdapter(getActivity(), data);
        listView.setAdapter(adapter);
        setHasOptionsMenu(true);
        getAllProducts();
        toggleEmptyListVisibility();
        return view;
    }

    public void getAllProducts() {
        RealmResults<Product> results = myRealm.where(Product.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            data.add(results.get(i));
        }
        myRealm.commitTransaction();
        adapter.notifyDataSetChanged();
    }

    public void toggleEmptyListVisibility() {
        try {
            listView.setVisibility(adapter.isEmpty()? View.INVISIBLE:View.VISIBLE);
            tvEmpty.setVisibility(adapter.isEmpty()?View.VISIBLE:View.INVISIBLE);
        }catch (NullPointerException e){

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        data.clear();
        getAllProducts();
        toggleEmptyListVisibility();
    }
}
