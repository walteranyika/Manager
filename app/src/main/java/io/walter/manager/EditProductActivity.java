package io.walter.manager;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.models.Category;
import io.walter.manager.models.Product;

public class EditProductActivity extends AppCompatActivity {
    EditText mEditTextName, mEditTextPrice, mEditTextDescription, mEditTextQty, mEditTextCode,edtItemReOrderLevel;;
    Spinner mSpinner;//TODO Fetch data from db
    ArrayList<String> spinnerData;
    ArrayAdapter<String> spinnerAdapter;
    Realm myRealm;
    Product mProduct;
    RadioButton radioTaxable;
    boolean isTaxable=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        myRealm=Realm.getInstance(this);
        int code = getIntent().getIntExtra("code", 0);
        mProduct=getProduct(code);
        mEditTextName = (EditText) findViewById(R.id.editItemName);
        mEditTextPrice = (EditText) findViewById(R.id.edtItemPrice);
        radioTaxable = (RadioButton) findViewById(R.id.radioTaxable);
        mEditTextDescription = (EditText) findViewById(R.id.edtItemDescription);
        mEditTextQty = (EditText) findViewById(R.id.edtItemQuantity);
        mSpinner = (Spinner) findViewById(R.id.spinnerItemCategory);
        mEditTextCode = (EditText) findViewById(R.id.edtItemCode);
        edtItemReOrderLevel = (EditText) findViewById(R.id.edtItemReOrderLevel);

        spinnerData = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        populateSpinner();
        if (mProduct != null) {
            mEditTextName.setText(mProduct.getTitle());
            mEditTextCode.setText(String.valueOf(mProduct.getCode()));
            mEditTextPrice.setText(String.valueOf(mProduct.getPrice()));
            mEditTextQty.setText(String.valueOf(mProduct.getQuantity()));
            mEditTextDescription.setText(mProduct.getDescription());
            mSpinner.setSelection(getSelectedPosition(mProduct.getCategory()));
            radioTaxable.setChecked(mProduct.isTaxable());
            edtItemReOrderLevel.setText(String.valueOf(mProduct.getReOrderLevel()));
        }

        radioTaxable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isTaxable=true;
                }else {
                    isTaxable=false;
                }
            }
        });
    }

    private Product getProduct(int code) {
        RealmResults<Product> results = myRealm.where(Product.class).equalTo("code",code).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return  results.get(0);
    }
    private void populateSpinner() {
        RealmResults<Category> results = myRealm.where(Category.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            spinnerData.add(results.get(i).getTitle());
            items.add(results.get(i).getTitle());
        }
        myRealm.commitTransaction();
        spinnerAdapter.notifyDataSetChanged();
    }
    ArrayList<String> items=new ArrayList<>();
    private int getSelectedPosition(String category){
        int position=0;
        for(int i=0; i<items.size(); i++){
            if(category.toLowerCase().contains(items.get(i).toLowerCase()))
            {
                position=i;
                break;
            }
        }
        return  position;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hideKeyBoard();
        if(item.getItemId()==R.id.menu_item_add){
            if (validateField(mEditTextCode) && validateField(mEditTextName) && validateField(mEditTextPrice) && validateField(mEditTextQty) && validateField(mEditTextDescription)) {
                String name = mEditTextName.getText().toString().trim();
                String price_str = mEditTextPrice.getText().toString().trim();
                String qty_str = mEditTextQty.getText().toString().trim();
                String desc = mEditTextDescription.getText().toString().trim();
                String code_str = mEditTextCode.getText().toString().trim();
                String category = mSpinner.getSelectedItem().toString();
                String re_level=edtItemReOrderLevel.getText().toString().trim();
                double re_order=Double.parseDouble(re_level);
                double price = Double.parseDouble(price_str);
                int quantity = Integer.parseInt(qty_str);
                int code = Integer.parseInt(code_str);
                Category categorySelected=getSelectedCategory(category);
                Product p=new Product(code,name,price,quantity,re_order,desc,category,categorySelected.getColor(),isTaxable);
                updateProductToReal(p, code);
                Snackbar.make(mEditTextCode,"Item Was Updated Successfully",Snackbar.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return true;
    }
    public  Category getSelectedCategory(String categoryTitle){
        RealmResults<Category> results = myRealm.where(Category.class).equalTo("title",categoryTitle).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return  results.get(0);
    }
    public void updateProductToReal(Product new_model, int code) {
        Product model = myRealm.where(Product.class).equalTo("code", code).findFirst();
        myRealm.beginTransaction();
        model.setCategory(new_model.getCategory());
        model.setColor(new_model.getColor());
        model.setPrice(new_model.getPrice());
        model.setQuantity(new_model.getQuantity());
        model.setTitle(new_model.getTitle());
        model.setDescription(new_model.getDescription());
        myRealm.commitTransaction();
    }
    public boolean validateField(EditText editTextName) {
        boolean ok = false;
        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError("Fill in here");
        } else {
            ok = true;
        }
        return ok;
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
