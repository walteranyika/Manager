package io.walter.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.fragments.ReceiptFragment;
import io.walter.manager.models.Service;
import io.walter.manager.models.TemporaryItem;

/**
 * Created by walter on 7/9/17.
 */

public class ReceiptListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<TemporaryItem> data;

    public ReceiptListAdapter(Context context, ArrayList<TemporaryItem> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();// # of items in your arraylist
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);// get the actual movie
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
            convertView = inflater.inflate(R.layout.receipt_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.purchaseTitle);
            viewHolder.unitPriceTextView = (TextView) convertView.findViewById(R.id.purchaseUnitPrice);
            viewHolder.quantityTextView = (TextView) convertView.findViewById(R.id.purchaseQuantity);
            viewHolder.totalPriceTextView = (TextView) convertView.findViewById(R.id.purchaseTotalPrice);
            viewHolder.purchaseDeleteItem=(ImageView)convertView.findViewById(R.id.purchaseDeleteItem) ;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TemporaryItem product = data.get(position);
        viewHolder.titleTextView.setText(product.getTitle());
        viewHolder.unitPriceTextView.setText("" + product.getPrice());
        viewHolder.quantityTextView.setText("x " + product.getQuantity());
        viewHolder.totalPriceTextView.setText("" + (product.getQuantity()*product.getPrice()));

        viewHolder.purchaseDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     new MaterialDialog.Builder(mContext)
                        .title("Delete " + product.getTitle())
                        .content("Are you sure you want to delete " + product.getTitle() + "? Your action will be irreversible.")
                        .positiveText("Delete")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int code =data.get(position).getCode();
                                Realm realm=Realm.getInstance(mContext);
                                RealmResults<TemporaryItem> results = realm.where(TemporaryItem.class).equalTo("code", code).findAll();
                                realm.beginTransaction();
                                results.remove(0);
                                realm.commitTransaction();
                                data.remove(position);
                                notifyDataSetChanged();
                                realm.close();
                                ReceiptFragment.getInstance().updateSummary();
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView quantityTextView;
        TextView unitPriceTextView;
        TextView totalPriceTextView;
        ImageView purchaseDeleteItem;
    }
}

