package io.walter.manager.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.SalesActivity;
import io.walter.manager.adapters.SalesListAdapter;
import io.walter.manager.models.Category;
import io.walter.manager.models.Product;
import io.walter.manager.models.TemporaryItem;

/**
 * Created by walter on 7/10/17.
 */

public class SalesFragment extends Fragment {
    TextView tvSalesCounter, tvSalesTotal;
    SalesListAdapter adapter;
    Realm myRealm;
    public static final String TAG = "KEEPER";
    TextView textCartItemCount;
    int mCartItemCount = 0;
    Spinner spinnerCategories;
    ArrayList<String> spinnerItems;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<Product> productArrayList;
    TextView tvEmpty;
    ListView listView;



    public interface OnShoppingBasketSelectedListener{
        void onShoppingBasketClicked();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_fragment, container, false);
        ((SalesActivity) getActivity()).getSupportActionBar().setTitle("All Products");
        listView = (ListView) view.findViewById(R.id.sales_list);
        tvEmpty = (TextView) view.findViewById(R.id.textEmpty);
        setHasOptionsMenu(true);
        myRealm=Realm.getInstance(getContext());
        tvSalesCounter = (TextView) view.findViewById(R.id.tvSalesCounter);
        tvSalesTotal = (TextView) view.findViewById(R.id.tvSalesTotals);
        LinearLayout layoutStocks = (LinearLayout) view.findViewById(R.id.layoutCounter);

        tvSalesCounter.setText(countItems()+" Items");
        tvSalesTotal.setText("KES "+getProductsTotalCost());

        spinnerCategories= (Spinner) view.findViewById(R.id.spinnerCategories);
        spinnerItems= getAllCategories();
        spinnerAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(spinnerAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = spinnerItems.get(position);
                if (position==0){
                 ((SalesActivity) getActivity()).getSupportActionBar().setTitle("All Products");
                  productArrayList.clear();
                  productArrayList.addAll(getProducts());
                  adapter.notifyDataSetChanged();
                  toggleEmptyListVisibility();
                }else {
                    ((SalesActivity) getActivity()).getSupportActionBar().setTitle(category+" category");
                    productArrayList.clear();
                    productArrayList.addAll(getSpecificProducts(category));
                    adapter.notifyDataSetChanged();
                    toggleEmptyListVisibility();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productArrayList = getProducts();
        adapter = new SalesListAdapter(getActivity(), productArrayList);
        listView.setAdapter(adapter);
        tvSalesCounter.setText(countItems()+" Items");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked at "+productArrayList.get(position).getTitle());
                savePurchasedIntoTemporary(productArrayList.get(position));
                tvSalesCounter.setText("" +countItems()+" Items");
                tvSalesTotal.setText("KES "+getProductsTotalCost());
                Log.d(TAG, "" +getProductsTotalCost());
                setupBadge();
            }
        });

        layoutStocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(getProductsTotalCost()>0)
            {
                OnShoppingBasketSelectedListener listener = (OnShoppingBasketSelectedListener) getActivity();
                listener.onShoppingBasketClicked();
            }
            else
            {
                Toast.makeText(getActivity(), "Cart is empty", Toast.LENGTH_SHORT).show();
            }
            }
        });
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

    public ArrayList<String> getAllCategories() {
        ArrayList<String> data = new ArrayList<>();
        data.add("All");
        myRealm.beginTransaction();
        RealmResults<Category> items = myRealm.where(Category.class).findAll();
        for (Category item : items) {
            data.add(item.getTitle());
        }
        myRealm.commitTransaction();
        return data;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sales_fragment_menu,menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        SearchView searchView =(SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SEARCH", newText);
                adapter.filter(newText);
                toggleEmptyListVisibility();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_cart){
            FragmentManager manager= getActivity().getSupportFragmentManager();
            FragmentTransaction ft=manager.beginTransaction();
            if(manager.findFragmentByTag(SalesActivity.RECEIPT_FRAGMENT)==null){
                ReceiptFragment receiptFragment = new ReceiptFragment();
                ft.add(R.id.placeHolderReports,receiptFragment,SalesActivity.RECEIPT_FRAGMENT);
                ft.addToBackStack(SalesActivity.RECEIPT_FRAGMENT);
                ft.commit();
                Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
            }else
            {
                manager.popBackStack();
            }
        }
        return true;
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void savePurchasedIntoTemporary(Product item){
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> results = myRealm.where(TemporaryItem.class).equalTo("code",item.getCode()).findAll();
        if (results.size()>0){
           results.get(0).setQuantity(results.get(0).getQuantity()+1);
           results.get(0).setTotal(results.get(0).getPrice()*results.get(0).getQuantity());
           Log.d(TAG, "savePurchasedIntoTemporary: "+results.get(0).getPrice()*results.get(0).getQuantity());
        }else {
           myRealm.copyToRealm(new TemporaryItem(item.getCode(),item.getTitle(),item.getPrice(),1,item.getDescription(),item.getCategory(),item.getColor(),item.getPrice(),true));
        }
        myRealm.commitTransaction();
    }
    public ArrayList<Product> getProducts(){
        ArrayList<Product> data=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<Product> items =myRealm.where(Product.class).findAll();
        for (Product item:items){
            data.add(item);
        }
        myRealm.commitTransaction();
        return  data;
    }

    public ArrayList<Product> getSpecificProducts(String category){
        Log.d("CATEGORY",category);
        ArrayList<Product> data=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<Category> items =myRealm.where(Category.class).equalTo("title", category).findAll();
        if (items.size()>0){
           Category category1=items.get(0);
           Log.d("CATEGORY", category1.getTitle());
           Log.d("CATEGORY", "COUNT IS "+category1.getProducts().size());
           RealmList<Product>  products= category1.getProducts();
           for (Product p:products){
               data.add(p);
               Log.d("CATEGORY", "ITEM IS "+p.getTitle());
           }
        }
        myRealm.commitTransaction();
        return  data;
    }
    public int countItems(){
        int count=0;
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items =myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item:items){
            count+=item.getQuantity();
        }
        myRealm.commitTransaction();
        mCartItemCount=count;
        return  count;
    }
    public double getProductsTotalCost(){
        double total=0;
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items =myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item:items){
            total+=item.getTotal();
        }
        myRealm.commitTransaction();
        return  total;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
