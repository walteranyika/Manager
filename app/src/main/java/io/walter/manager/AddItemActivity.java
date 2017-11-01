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


public class AddItemActivity extends AppCompatActivity {
    EditText mEditTextName, mEditTextPrice, mEditTextDescription, mEditTextQty, mEditTextCode;
    Spinner mSpinner;
    ArrayList<String> spinnerData;
    ArrayAdapter<String> spinnerAdapter;
    Realm myRealm;
    int code=100;
    RadioButton radioTaxable;
    boolean isTaxable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        myRealm=Realm.getInstance(this);
        mEditTextName = (EditText) findViewById(R.id.editItemName);
        mEditTextPrice = (EditText) findViewById(R.id.edtItemPrice);
        mEditTextDescription = (EditText) findViewById(R.id.edtItemDescription);
        mEditTextQty = (EditText) findViewById(R.id.edtItemQuantity);
        mSpinner = (Spinner) findViewById(R.id.spinnerItemCategory);
        radioTaxable = (RadioButton) findViewById(R.id.radioTaxable);
        spinnerData=new ArrayList<>();
        spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mEditTextCode = (EditText) findViewById(R.id.edtItemCode);
        getLastId();
        mEditTextCode.setText(""+code);
        mEditTextCode.setEnabled(false);
        populateSpinner();
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
    private void populateSpinner() {
        RealmResults<Category> results = myRealm.where(Category.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            spinnerData.add(results.get(i).getTitle());
        }
        myRealm.commitTransaction();
        spinnerAdapter.notifyDataSetChanged();
    }
    private void getLastId(){
        RealmResults<Product> results = myRealm.where(Product.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            code = myRealm.where(Product.class).max("code").intValue() + 1;
        else
            code=100;//starting ID
        myRealm.commitTransaction();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add) {
            hideKeyBoard();
            if (validateField(mEditTextCode) && validateField(mEditTextName) && validateField(mEditTextPrice) && validateField(mEditTextQty) && validateField(mEditTextDescription) && !isTaxable) {
                String name = mEditTextName.getText().toString().trim();
                String price_str = mEditTextPrice.getText().toString().trim();
                String qty_str = mEditTextQty.getText().toString().trim();
                String desc = mEditTextDescription.getText().toString().trim();
                String code_str = mEditTextCode.getText().toString().trim();
                String category = mSpinner.getSelectedItem().toString().trim();
                double price = Double.parseDouble(price_str);
                int quantity = Integer.parseInt(qty_str);
                code = Integer.parseInt(code_str);
                Category categorySelected =getSelectedCategory(category);
                Product product=new Product(code,name, price, quantity,desc,category, categorySelected.getColor(),isTaxable);
                saveProductToRealm(categorySelected,product);
                clearFields(mEditTextName);
                clearFields(mEditTextPrice);
                clearFields(mEditTextQty);
                clearFields(mEditTextDescription);
                clearFields(mEditTextCode);
                getLastId();
                mEditTextCode.setText(""+code);
                Snackbar.make(mEditTextCode,"Product Added Successfully",Snackbar.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return true;
    }

    public void saveProductToRealm(Category category,Product product) {
        myRealm.beginTransaction();
        Product managedProduct = myRealm.copyToRealm(product);
        category.getProducts().add(managedProduct);
        myRealm.commitTransaction();
        code++;
    }
    public  Category getSelectedCategory(String categoryTitle){
        RealmResults<Category> results = myRealm.where(Category.class).equalTo("title",categoryTitle).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return  results.get(0);
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
    public  void clearFields(EditText editTextName){
        editTextName.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinnerData.clear();
        populateSpinner();
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
