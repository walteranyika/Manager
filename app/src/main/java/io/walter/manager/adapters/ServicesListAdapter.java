package io.walter.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.EditProductActivity;
import io.walter.manager.R;
import io.walter.manager.models.Product;
import io.walter.manager.models.Service;

public class ServicesListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Service> temporaryArray;
    private ArrayList<Service> permanentArray;
    public ServicesListAdapter(Context context, ArrayList<Service> data) {
        this.mContext = context;
        this.temporaryArray = data;
        this.permanentArray = new ArrayList<>();
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
            convertView = inflater.inflate(R.layout.services_items_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.itemTitleInventory);
            viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.itemPriceInventory);
            viewHolder.codeTextView = (TextView) convertView.findViewById(R.id.itemCodeInventory);
            viewHolder.descTextView = (TextView) convertView.findViewById(R.id.itemDescInventory);
            viewHolder.popupImageView = (ImageView) convertView.findViewById(R.id.popup_menu);
            viewHolder.colorView =convertView.findViewById(R.id.colorView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Service product = temporaryArray.get(position);
        viewHolder.titleTextView.setText(product.getTitle());
        viewHolder.priceTextView.setText("" + product.getPrice());
        viewHolder.codeTextView.setText("Code: " + product.getCode());
        viewHolder.descTextView.setText(product.getDescription());
        @ColorInt int color=product.getColor();
        GradientDrawable gd=new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(5);
        viewHolder.colorView.setBackground(gd);
        viewHolder.popupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up menu
                PopupMenu menu = new PopupMenu(mContext, v);
                menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equals("Delete Item")) {
                            //DONE  do the deletion
                            new MaterialDialog.Builder(mContext)
                                    .title("Delete " + product.getTitle())
                                    .content("Are you sure you want to delete " + product.getTitle() + "? Your action will be irreversible.")
                                    .positiveText("Delete")
                                    .negativeText("Cancel")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            int code =temporaryArray.get(position).getCode();
                                            Realm realm=Realm.getInstance(mContext);
                                            RealmResults<Service> results = realm.where(Service.class).equalTo("code", code).findAll();
                                            realm.beginTransaction();
                                            results.remove(0);
                                            realm.commitTransaction();
                                            temporaryArray.remove(position);
                                            notifyDataSetChanged();
                                            realm.close();
                                        }
                                    })
                                    .show();
                        } else if (item.getTitle().toString().equals("Edit Item")) {
                            Intent editIntent = new Intent(mContext, EditProductActivity.class);
                            editIntent.putExtra("code", product.getCode());
                            mContext.startActivity(editIntent);
                        }
                        else if (item.getTitle().toString().equals("Edit Price")) {
                            new MaterialDialog.Builder(mContext)
                                    .title("Adjust Price")
                                    .content("Edit price for " + product.getTitle())
                                    .inputType(InputType.TYPE_CLASS_NUMBER)
                                    .input("Price", "" ,new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(MaterialDialog dialog, CharSequence input) {
                                            try {
                                                Realm realm=Realm.getInstance(mContext);
                                                realm.beginTransaction();
                                                double price = Double.parseDouble(input.toString().trim());
                                                product.setPrice(price);
                                                notifyDataSetChanged();
                                                realm.commitTransaction();
                                                realm.close();
                                            }catch (NumberFormatException e)
                                            {

                                            }
                                        }
                                    }).show();
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        return convertView;
    }

    public void filter(String text) {
        text = text.toLowerCase();
        temporaryArray.clear();
        if (text.trim().length() == 0) {
            temporaryArray.addAll(permanentArray);
        } else {
            for (Service p : permanentArray) {
                //|| (p.getCode()+"").contains(text) || (p.getPrice()+"").contains(text)
                if (p.getTitle().toLowerCase().contains(text) || (p.getCode() + "").contains(text) || (p.getPrice() + "").contains(text) ||  p.getDescription().toLowerCase().contains(text)) {
                    temporaryArray.add(p);
                }
            }
            Log.d("SEARCH", "COUNT " + temporaryArray.size());
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView codeTextView;
        TextView priceTextView;
        TextView descTextView;
        ImageView popupImageView;
        View colorView;
    }
}

