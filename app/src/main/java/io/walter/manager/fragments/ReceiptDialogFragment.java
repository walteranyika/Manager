package io.walter.manager.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.models.TemporaryItem;

/**
 * Created by walter on 7/10/17.
 */
public class ReceiptDialogFragment extends DialogFragment {
    TextView tvCounter;
    Realm myRealm;
    int quantity;

    public interface ItemQuantityChangedListener{
        void onQuantityChanged(int code, int quantity);
    }

    public ReceiptDialogFragment() {
    }

    public static ReceiptDialogFragment newInstance(int code) {

        Bundle args = new Bundle();
        args.putInt("code", code);
        ReceiptDialogFragment fragment = new ReceiptDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        myRealm=Realm.getInstance(getContext());
        final int code = getArguments().getInt("code");
        quantity = getProductQuantity(code);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Change Quantity")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateQuantity(code,quantity);
                        ItemQuantityChangedListener listener = (ItemQuantityChangedListener)getActivity();
                        listener.onQuantityChanged(code,quantity);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.receipt_dialog_fragment, null);
        Button btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        Button btnMinus = (Button) rootView.findViewById(R.id.btnMinus);
        tvCounter = (TextView) rootView.findViewById(R.id.tv_frag_qty);
        tvCounter.setText("" +getProductQuantity(code));
        Log.d("PRODUCT_QTY", "" +getProductQuantity(code));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                Log.d("QUANTITY", "" + quantity);
                tvCounter.setText("" + quantity);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity >= 2)
                    quantity--;
                Log.d("QUANTITY", "" + quantity);
                tvCounter.setText("" + quantity);
            }
        });

        builder.setView(rootView);
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public int getProductQuantity(int code){
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> results = myRealm.where(TemporaryItem.class).equalTo("code",code).findAll();
        TemporaryItem item= results.get(0);
        myRealm.commitTransaction();
        return  item.getQuantity();
    }
    public void updateQuantity(int code, int quantity){
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> results = myRealm.where(TemporaryItem.class).equalTo("code",code).findAll();
        results.get(0).setQuantity(quantity);
        myRealm.commitTransaction();
    }
}
