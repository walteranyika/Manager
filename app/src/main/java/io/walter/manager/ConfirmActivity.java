package io.walter.manager;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.adapters.TemporaryOrdersListAdapter;
import io.walter.manager.models.Order;
import io.walter.manager.models.OrderItem;
import io.walter.manager.models.TemporaryOrderItem;
import io.walter.manager.utils.CalendarUtils;

public class ConfirmActivity extends AppCompatActivity {
    TextView tvSalesCounter, tvSalesTotal;
    ListView mListView;
    ArrayList<TemporaryOrderItem> mProducts;
    TemporaryOrdersListAdapter mListAdapter;
    Realm myRealm;
    String type,nameQuote;
    int order_code=1;
    int order_item_code=1;
    static ConfirmActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        mListView= (ListView) findViewById(R.id.listconfirmOrders);
        tvSalesCounter = (TextView)findViewById(R.id.tvSalesCounter);
        tvSalesTotal = (TextView)findViewById(R.id.tvSalesTotals);
        type=getIntent().getStringExtra("type");
        instance=this;
        getSupportActionBar().setTitle("Confirm "+type);
        nameQuote=getIntent().getStringExtra("names");
        myRealm=Realm.getInstance(this);
        mProducts=getTemporaryItems();
        getLastOrderId();
        mListAdapter=new TemporaryOrdersListAdapter(this,mProducts);
        mListView.setAdapter(mListAdapter);
        updateItems();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myRealm.beginTransaction();
                mProducts.get(position).setQuantity(mProducts.get(position).getQuantity()+1);
                mProducts.get(position).setQuantity(mProducts.get(0).getQuantity()+1);
                mProducts.get(position).setTotal(mProducts.get(0).getQuantity()*mProducts.get(0).getPrice());
                myRealm.commitTransaction();
                mListAdapter.notifyDataSetChanged();
                updateItems();
            }
        });
    }
    public static ConfirmActivity getInstance(){
        return  instance;
    }

    private ArrayList<TemporaryOrderItem> getTemporaryItems() {
        ArrayList<TemporaryOrderItem> data=new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<TemporaryOrderItem> items =myRealm.where(TemporaryOrderItem.class).findAll();
        for (TemporaryOrderItem item:items){
            data.add(item);
        }
        myRealm.commitTransaction();
        return  data;
    }

    public void updateItems() {
        tvSalesCounter.setText(countItems()+" Items");
        tvSalesTotal.setText("KES "+getProductsTotalCost());
    }

    public void confirm(View view) {
        if (countItems()>0) {
            saveNewOrder(type, nameQuote, mProducts);
            clearRecords();
            mProducts.clear();
            mListAdapter.notifyDataSetChanged();
            updateItems();
            view.setEnabled(false);
            Snackbar.make(tvSalesCounter, type + " has been saved successfully", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void clearRecords() {
       myRealm.executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               realm.clear(TemporaryOrderItem.class);
           }
       });
    }
    private void saveNewOrder(String type, String nameQuote, ArrayList<TemporaryOrderItem> products) {
        double total=getProductsTotalCost();
        getLastOrderItemId();
        myRealm.beginTransaction();
        long time=System.currentTimeMillis();
        String date= CalendarUtils.ConvertToPureDateString(time);
        //TODO change clients id
        Order order =new Order(order_code,type,total,date,nameQuote,1);
        Order x=myRealm.copyToRealm(order);
        for (TemporaryOrderItem item:products){
            OrderItem o_i=new OrderItem(order_item_code,item.getTitle(),item.getPrice(),item.getQuantity(),item.getDescription(),item.getCategory(),item.getColor(),item.isTaxable());
            OrderItem managed_o_i=myRealm.copyToRealm(o_i);
            x.getOrderItems().add(managed_o_i);
            order_item_code++;
        }
        myRealm.commitTransaction();
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
    private void getLastOrderId(){
        RealmResults<Order> results = myRealm.where(Order.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            order_code= myRealm.where(Order.class).max("code").intValue() + 1;
        else
            order_code=1;
        myRealm.commitTransaction();
    }
    private void getLastOrderItemId(){
        RealmResults<OrderItem> results = myRealm.where(OrderItem.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            order_item_code= myRealm.where(OrderItem.class).max("code").intValue() + 1;
        else
            order_item_code=1;
        myRealm.commitTransaction();
    }


}
