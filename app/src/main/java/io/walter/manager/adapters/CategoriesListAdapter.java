package io.walter.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import io.walter.manager.R;
import io.walter.manager.models.Category;

/**
 * Created by walter on 7/9/17.
 */

public class CategoriesListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<Category> temporaryArray;
    ArrayList<Category> permanentArray;

    public CategoriesListAdapter(Context context, ArrayList<Category> data) {
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
            convertView = inflater.inflate(R.layout.categories_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.tvCategoriesTitle);
            viewHolder.countTextView = (TextView) convertView.findViewById(R.id.tvCategoriesCount);
            viewHolder.colorView = convertView.findViewById(R.id.colorView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Category product = temporaryArray.get(position);
        viewHolder.titleTextView.setText(product.getTitle());
        String items =product.getProducts().size()>1?"Items":"Item";
        if (product.getProducts().size()==0){
            items="No Items";
            viewHolder.countTextView.setText(items);
        }else{
            viewHolder.countTextView.setText(product.getProducts().size()+" "+items);
        }

        @ColorInt int color=product.getColor();

        GradientDrawable gd=new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(5);
        viewHolder.colorView.setBackground(gd);

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
            for (Category p:permanentArray)
            {
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
        TextView countTextView;
        View colorView;
    }
}

