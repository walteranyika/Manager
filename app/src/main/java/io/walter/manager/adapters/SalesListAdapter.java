package io.walter.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import io.walter.manager.R;
import io.walter.manager.models.Product;

/**
 * Created by walter on 7/9/17.
 */

public class SalesListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<Product> temporaryArray;
    ArrayList<Product> permanentArray;

    public SalesListAdapter(Context context, ArrayList<Product> data) {
        this.mContext = context;
        this.temporaryArray = data;
        this.permanentArray=new ArrayList<>();
        this.permanentArray.addAll(data);
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
            convertView = inflater.inflate(R.layout.sales_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.itemPrice);
            viewHolder.codeTextView = (TextView) convertView.findViewById(R.id.itemCode);
            viewHolder.qtyTextView = (TextView) convertView.findViewById(R.id.itemQty);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product product = temporaryArray.get(position);
        viewHolder.titleTextView.setText(product.getTitle());
        viewHolder.priceTextView.setText("" + product.getPrice());
        viewHolder.codeTextView.setText("" + product.getCode());
        int qty =product.getQuantity();
        if (qty>0) {
            viewHolder.qtyTextView.setText("x " + qty);
        }
        return convertView;
    }

    public void filter(String text){
        text=text.toLowerCase();
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
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView codeTextView;
        TextView priceTextView;
        TextView qtyTextView;
    }
}

