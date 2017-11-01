package io.walter.manager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.models.Service;
import io.walter.manager.utils.General;

public class AddServiceActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{
    EditText mEditTextName, mEditTextPrice, mEditTextDescription,mEditTextCode;
    Realm myRealm;
    TextView colorTextView;
    View colorView;
    RadioButton radioProduct;
    boolean isService=false;
    int service_code=100;
    int colorChosen =-769226;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        myRealm=Realm.getInstance(this);
        mEditTextName = (EditText) findViewById(R.id.editItemName);
        mEditTextPrice = (EditText) findViewById(R.id.edtItemPrice);
        mEditTextDescription = (EditText) findViewById(R.id.edtItemDescription);
        colorView=findViewById(R.id.colorView);
        colorTextView= (TextView) findViewById(R.id.colorTextView);
        radioProduct= (RadioButton) findViewById(R.id.radioTaxable);
        mEditTextCode = (EditText) findViewById(R.id.edtItemCode);
        getLastServiceId();
        mEditTextCode.setText(""+service_code);

        colorChosen= General.generateRandomColor();
        GradientDrawable gd=new GradientDrawable();
        gd.setColor(colorChosen);
        gd.setCornerRadius(5);
        colorView.setBackground(gd);


        mEditTextCode.setEnabled(false);
        radioProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isService=false;
                }else {
                    isService=true;
                }
            }
        });
    }
    private void getLastServiceId(){
        RealmResults<Service> results = myRealm.where(Service.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            service_code = myRealm.where(Service.class).max("code").intValue() + 1;
        else
            service_code=100;//starting ID
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
           if(validateField(mEditTextCode) && validateField(mEditTextName) && validateField(mEditTextPrice) && validateField(mEditTextDescription) )
            {
                String name = mEditTextName.getText().toString().trim();
                String price_str = mEditTextPrice.getText().toString().trim();
                String desc = mEditTextDescription.getText().toString().trim();
                double price = Double.parseDouble(price_str);
                myRealm.beginTransaction();
                Log.d("COLOR_GREY",""+Color.parseColor("#808080"));
                Service service=new Service(service_code,name,price,desc, colorChosen);
                myRealm.copyToRealm(service);
                myRealm.commitTransaction();
                clearFields(mEditTextName);
                clearFields(mEditTextPrice);
                clearFields(mEditTextDescription);
                getLastServiceId();
                mEditTextCode.setText(""+service_code);
                Snackbar.make(mEditTextCode,"Service Added Successfully",Snackbar.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }
        return true;
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

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        colorTextView.setTextColor(selectedColor);
        colorChosen =selectedColor;
        GradientDrawable gd=new GradientDrawable();
        gd.setColor(selectedColor);
        gd.setCornerRadius(5);
        colorView.setBackground(gd);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }
    public void chooseColor(View view) {

        int[] primary = new int[] {
                Color.parseColor("#F44336")
        };
        int[][] secondary = new int[][]
                {
                        new int[] { Color.parseColor("#f44336"),
                                Color.parseColor("#e91e63"),
                                Color.parseColor("#9c27b0"),
                                Color.parseColor("#880e4f"),
                                Color.parseColor("#4a148c"),
                                Color.parseColor("#3f51b5"),
                                Color.parseColor("#2196f3"),
                                Color.parseColor("#1a237e"),
                                Color.parseColor("#006064"),
                                Color.parseColor("#1b5e20"),
                                Color.parseColor("#ff6f00"),
                                Color.parseColor("#757575"),}
                };

        new ColorChooserDialog.Builder(this, R.string.app_name)
                .customColors(primary,secondary)
                .show();
    }
}
