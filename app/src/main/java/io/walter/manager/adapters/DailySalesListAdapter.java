package io.walter.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.walter.manager.R;
import io.walter.manager.models.Order;
import io.walter.manager.models.PurchaseSummary;

/**
 * Created by walter on 7/9/17.
 */

public class DailySalesListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<PurchaseSummary> temporaryArray;
    ArrayList<PurchaseSummary> permanentArray;
    String type;

    public DailySalesListAdapter(Context context, ArrayList<PurchaseSummary> data, String type) {
        this.mContext = context;
        this.temporaryArray = data;
        this.permanentArray=new ArrayList<>();
        this.permanentArray.addAll(data);
        this.type=type;
    }

    @Override
    public int getCount() {
        return temporaryArray.size();// # of items in your arraylist
    }

    @Override
    public Object getItem(int position) {
        return temporaryArray.get(position);// get the actual movie
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.daily_sales_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemDate = (TextView) convertView.findViewById(R.id.itemDate);
            viewHolder.itemNumber = (TextView) convertView.findViewById(R.id.itemSaleNumber);
            viewHolder.itemTotal = (TextView) convertView.findViewById(R.id.itemTotal);
            viewHolder.itemQty = (TextView) convertView.findViewById(R.id.itemQty);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PurchaseSummary purchaseSummary = temporaryArray.get(position);
        viewHolder.itemNumber.setText(purchaseSummary.getRaw_date());
        viewHolder.itemDate.setText("Sale #"+purchaseSummary.getCode());
        viewHolder.itemTotal.setText("KES " + purchaseSummary.getTotal_price());
        viewHolder.itemQty.setText(purchaseSummary.getPurchasedItems().size()+" Items");


        return convertView;
    }

    public void filter(String text){
        /*text=text.toLowerCase();
        temporaryArray.clear();

        if(text.trim().length()==0)
        {
          temporaryArray.addAll(permanentArray);
        }
        else
        {
            for (Product p:permanentArray)
            {
                //|| (p.getCode()+"").contains(text) || (p.getPrice()+"").contains(text)
               if(p.getTitle().toLowerCase().contains(text) )
               {
                  temporaryArray.add(p);
               }
            }
            Log.d("SEARCH","COUNT "+temporaryArray.size());
        }
        notifyDataSetChanged();*/
    }

    static class ViewHolder {
        TextView itemDate;
        TextView itemTotal;
        TextView itemNumber;
        TextView itemQty;
    }
}

