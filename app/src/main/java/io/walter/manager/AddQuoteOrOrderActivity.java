package io.walter.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.adapters.SalesListAdapter;
import io.walter.manager.models.Product;
import io.walter.manager.models.TemporaryOrderItem;

public class AddQuoteOrOrderActivity extends AppCompatActivity {
    TextView tvSalesCounter, tvSalesTotal;
    Realm myRealm;
    SalesListAdapter adapter;
    EditText edtClientNames;
    MultiStateToggleButton select_btn;
    String type="order";
    private static final String TAG = "QUOTES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quote_or_order);
        ListView listView = (ListView)findViewById(R.id.listViewQuotesorOrders);
        myRealm=Realm.getInstance(this);
        getSupportActionBar().setTitle("Add Order");
        tvSalesCounter = (TextView)findViewById(R.id.tvSalesCounter);
        tvSalesTotal = (TextView)findViewById(R.id.tvSalesTotals);
        select_btn= (MultiStateToggleButton) findViewById(R.id.select_btn);
        select_btn.setValue(0);
        edtClientNames= (EditText) findViewById(R.id.edtClientName);
        final ArrayList<Product> data = getProducts();
        adapter = new SalesListAdapter(this, data);
        listView.setAdapter(adapter);
        updateItems();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveOrder(data.get(position));
                updateItems();
                adapter.notifyDataSetChanged();
            }
        });
        select_btn.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
               if (position==1){
                   getSupportActionBar().setTitle("Add Order");
                   type="quote";
               }else{
                   getSupportActionBar().setTitle("Add Quote");
                   type="order";
               }
                Log.d(TAG, "Type is "+type);
            }
        });


    }

    private void saveOrder(Product product) {
        myRealm.beginTransaction();
        RealmResults<TemporaryOrderItem> results = myRealm.where(TemporaryOrderItem.class).equalTo("code",product.getCode()).findAll();
        if (results.size()>0){
            results.get(0).setQuantity(results.get(0).getQuantity()+1);
            results.get(0).setTotal(results.get(0).getPrice()*results.get(0).getQuantity());
            Log.d(TAG, "savedTemporaryOrder: "+results.get(0).getPrice()*results.get(0).getQuantity());
        }else {
            myRealm.copyToRealm(new TemporaryOrderItem(product.getCode(),product.getTitle(),product.getPrice(),1,product.getDescription(),product.getCategory(),product.getColor(),product.isTaxable(),product.getPrice()));
        }
        myRealm.commitTransaction();
    }


    private ArrayList<Product> getProducts() {
        ArrayList<Product> data=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<Product> items =myRealm.where(Product.class).findAll();
        for (Product item:items){
            data.add(item);
        }
        myRealm.commitTransaction();
        return  data;
    }

    private void updateItems() {
        tvSalesCounter.setText(countItems()+" Items");
        tvSalesTotal.setText("KES "+getProductsTotalCost());
    }

    public int countItems(){
        int count=0;
        myRealm.beginTransaction();
        RealmResults<TemporaryOrderItem> items =myRealm.where(TemporaryOrderItem.class).findAll();
        for (TemporaryOrderItem item:items){
            count+=item.getQuantity();
        }
        myRealm.commitTransaction();
        return  count;
    }
    public double getProductsTotalCost(){
        double total=0;
        myRealm.beginTransaction();
        RealmResults<TemporaryOrderItem> items =myRealm.where(TemporaryOrderItem.class).findAll();
        for (TemporaryOrderItem item:items){
            total+=item.getTotal();
        }
        myRealm.commitTransaction();
        return  total;
    }

    public void confirm(View view) {
        if(edtClientNames.getText().toString().trim().isEmpty())
        {
            Snackbar.make(tvSalesCounter,"You must enter the "+type+" name",Snackbar.LENGTH_SHORT).show();
            return;
        }else if(countItems()==0)
        {
            Snackbar.make(tvSalesCounter,"You must add at least one item to your "+type,Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (countItems()>0 && !edtClientNames.getText().toString().trim().isEmpty()) {
            saveName(edtClientNames.getText().toString().trim());
            Intent intent = new Intent(this, ConfirmActivity.class);
            intent.putExtra("names", edtClientNames.getText().toString().trim());
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    private void saveName(String names) {
        SharedPreferences.Editor prefs=getSharedPreferences("names",MODE_PRIVATE).edit();
        prefs.putString("names",names);
        prefs.commit();
    }
    private void getNames(){
        SharedPreferences pres=getSharedPreferences("names",MODE_PRIVATE);
        edtClientNames.setText(pres.getString("names",""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateItems();
        getNames();
    }

}
