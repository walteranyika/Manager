package io.walter.manager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import io.walter.manager.AddCategoryActivity;
import io.walter.manager.R;
import io.walter.manager.adapters.CategoriesListAdapter;
import io.walter.manager.models.Category;

/**
 * Created by walter on 7/22/17.
 */

public class CategoryFragment extends Fragment {

    ArrayList<Category> data;
    CategoriesListAdapter adapter;
    Realm myRealm;
    TextView tvEmpty;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.categories_fragment, container, false);
        listView= (ListView) view.findViewById(R.id.listCategories);
        tvEmpty = (TextView) view.findViewById(R.id.textEmpty);
        myRealm = Realm.getInstance(getContext());
        data=new ArrayList<>();
        adapter=new CategoriesListAdapter(getActivity(),data);
        listView.setAdapter(adapter);
        setHasOptionsMenu(true);
        getAllCategories();
        toggleEmptyListVisibility();
        return view;
    }
    public void toggleEmptyListVisibility() {
        try {
            listView.setVisibility(adapter.isEmpty()? View.INVISIBLE:View.VISIBLE);
            tvEmpty.setVisibility(adapter.isEmpty()?View.VISIBLE:View.INVISIBLE);
        }catch (NullPointerException e){

        }
    }

    public void getAllCategories() {
        RealmResults<Category> results = myRealm.where(Category.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            data.add(results.get(i));
        }
        myRealm.commitTransaction();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_category,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_add_category)
        {
          startActivity(new Intent(getActivity(), AddCategoryActivity.class));
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        data.clear();
        getAllCategories();
        toggleEmptyListVisibility();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}