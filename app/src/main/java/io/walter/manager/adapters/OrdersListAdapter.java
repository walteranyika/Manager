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

/**
 * Created by walter on 7/9/17.
 */

public class OrdersListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<Order> temporaryArray;
    ArrayList<Order> permanentArray;
    String type;

    public OrdersListAdapter(Context context, ArrayList<Order> data, String type) {
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
            convertView = inflater.inflate(R.layout.orders_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.orderItemTitle = (TextView) convertView.findViewById(R.id.orderItemTitle);
            viewHolder.orderItemDate = (TextView) convertView.findViewById(R.id.orderItemDate);
            viewHolder.orderItemTotal = (TextView) convertView.findViewById(R.id.orderItemTotal);
            viewHolder.orderItemNumber = (TextView) convertView.findViewById(R.id.orderItemNumber);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order order = temporaryArray.get(position);
        viewHolder.orderItemTitle.setText(order.getClient());
        viewHolder.orderItemDate.setText(order.getDate());
        viewHolder.orderItemTotal.setText("" + order.getTotal());
        viewHolder.orderItemNumber.setText(type+" #"+order.getCode());

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
        TextView orderItemTitle;
        TextView orderItemTotal;
        TextView orderItemDate;
        TextView orderItemNumber;
    }
}

