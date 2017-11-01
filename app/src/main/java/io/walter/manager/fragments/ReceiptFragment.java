package io.walter.manager.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.R;
import io.walter.manager.SalesActivity;
import io.walter.manager.adapters.ReceiptListAdapter;
import io.walter.manager.models.TemporaryItem;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by walter on 7/9/17.
 */

public class ReceiptFragment extends Fragment {
    private static final String FRAGMENT_CHARGE = "charge_fragment";
    ArrayList<TemporaryItem> data;
    ReceiptListAdapter adapter;
    TextView tvTotal;
    TextView tvCounter;
    FancyButton productsBtn, checkOutBtn;
    Realm myRealm;
    private static ReceiptFragment instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_fragment, container, false);
        ((SalesActivity) getActivity()).getSupportActionBar().setTitle("Cart");
        myRealm = Realm.getInstance(getContext());
        setHasOptionsMenu(true);
        instance = this;
        tvTotal = (TextView) view.findViewById(R.id.tvReceiptTotals);
        tvCounter = (TextView) view.findViewById(R.id.tvReceiptCounter);




        updateSummary();
        productsBtn = (FancyButton) view.findViewById(R.id.products);
        checkOutBtn = (FancyButton) view.findViewById(R.id.btnCheckOut);

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeFragment chargeFragment = new ChargeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.placeHolderReports, chargeFragment, ChargeFragment.class.getName());
                ft.addToBackStack(ChargeFragment.class.getName());
                ft.commit();
            }
        });

        try {
            productsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SalesFragment salesFragment = new SalesFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    ft.replace(R.id.placeHolderReports, salesFragment, SalesActivity.SALES_FRAGMENT);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        } catch (NullPointerException e) {
            Log.d("KEEPER", e.getMessage());
            e.printStackTrace();
        }

        ListView list = (ListView) view.findViewById(R.id.receiptList);

        data = getProducts();
        adapter = new ReceiptListAdapter(getActivity(), data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReceiptDialogFragment frag = ReceiptDialogFragment.newInstance(data.get(position).getCode());
                frag.show(getFragmentManager(), "it_works");
            }
        });
        return view;
    }

    public static ReceiptFragment getInstance() {
        return instance;
    }


    public void refresh() {
        data.clear();
        Log.d("ITEMS", "B4 :" + data.size());
        ArrayList<TemporaryItem> items = getProducts();
        data.addAll(items);
        Log.d("ITEMS", "AF:" + data.size());
        adapter.notifyDataSetChanged();
        updateSummary();
    }

    public ArrayList<TemporaryItem> getProducts() {
        ArrayList<TemporaryItem> data = new ArrayList<>();
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items = myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item : items) {
            data.add(item);
        }
        myRealm.commitTransaction();
        return data;
    }

    public int countItems() {
        int count = 0;
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items = myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item : items) {
            count += item.getQuantity();
        }
        myRealm.commitTransaction();
        return count;
    }

    public double getProductsTotalCost() {
        double total = 0;
        myRealm.beginTransaction();
        RealmResults<TemporaryItem> items = myRealm.where(TemporaryItem.class).findAll();
        for (TemporaryItem item : items) {
            total += item.getTotal();
        }
        myRealm.commitTransaction();
        return total;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_receipt_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete) {
            new MaterialDialog.Builder(getContext())
                    .title("Clear Receipt")
                    .content("Are you sure you want to delete clear the receipt" + "? Your action will be irreversible.")
                    .positiveText("Delete")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            myRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.clear(TemporaryItem.class);
                                }
                            });
                            data.clear();
                            adapter.notifyDataSetChanged();
                            updateSummary();
                        }
                    })
                    .show();


        }
        return true;
    }

    public void updateSummary() {
        tvTotal.setText("KES " + getProductsTotalCost());
        tvCounter.setText(countItems() + " Items");
    }
}
