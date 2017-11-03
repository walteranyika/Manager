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
import io.walter.manager.reportingsql.MonthlySale;

/**
 * Created by walter on 7/9/17.
 */

public class MonthlySalesListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<MonthlySale> temporaryArray;
    ArrayList<MonthlySale> permanentArray;
    String type;

    public MonthlySalesListAdapter(Context context, ArrayList<MonthlySale> data, String type) {
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
            convertView = inflater.inflate(R.layout.monthly_sales_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemMonth = (TextView) convertView.findViewById(R.id.itemMonth);
            viewHolder.itemTotalAmount = (TextView) convertView.findViewById(R.id.itemTotalAmount);
            viewHolder.itemTotalSales = (TextView) convertView.findViewById(R.id.itemSaleQuantity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MonthlySale purchaseSummary = temporaryArray.get(position);
        viewHolder.itemTotalAmount.setText(""+purchaseSummary.getTotalSales());
        viewHolder.itemMonth.setText(purchaseSummary.getMonth()+" "+purchaseSummary.getYear());
        viewHolder.itemTotalSales.setText(purchaseSummary.getNumberSales()+" Total sales");
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
        TextView itemMonth;
        TextView itemTotalSales;
        TextView itemTotalAmount;
    }
}

