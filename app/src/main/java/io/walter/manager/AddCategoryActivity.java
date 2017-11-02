package io.walter.manager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import io.realm.Realm;
import io.realm.RealmResults;
import io.walter.manager.models.Category;
import io.walter.manager.utils.General;


public class AddCategoryActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{
    EditText mEditTextCategory;
    View colorView;
    TextView colorTextView;
    int colorChosen;
    Realm myRealm;
    int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        mEditTextCategory= (EditText) findViewById(R.id.edtCategoryAdd);
        colorView=findViewById(R.id.colorView);
        colorTextView= (TextView) findViewById(R.id.colorTextView);
        generateColor();


        myRealm = Realm.getInstance(this);
        getLastId();
    }

    private void generateColor() {
        colorChosen = General.generateRandomColor();
        GradientDrawable gd=new GradientDrawable();
        gd.setColor(colorChosen);
        gd.setCornerRadius(5);
        colorView.setBackground(gd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if no view has focus:
        hideKeyBoard();
        if (item.getItemId()==R.id.menu_item_add){
            String category=mEditTextCategory.getText().toString().trim();
            if (!category.isEmpty()) {
                String capitalized = category.substring(0,1).toUpperCase()+category.substring(1).toLowerCase();
                Category cat=new Category(id,capitalized,0, colorChosen);
                saveCategoryToRealm(cat);
                Snackbar.make(mEditTextCategory, "Category Added", Snackbar.LENGTH_LONG).show();
                mEditTextCategory.setText("");
                generateColor();
            }else
            {
                Snackbar.make(mEditTextCategory, "You must provide  a Category", Snackbar.LENGTH_SHORT).show();
            }
        }else if(item.getItemId()==android.R.id.home)
        {
          this.finish();
        }
        return true;
    }

    private void saveCategoryToRealm(Category model) {
        myRealm.beginTransaction();
        myRealm.copyToRealm(model);
        myRealm.commitTransaction();
        id++;
    }

    private void getLastId(){
        RealmResults<Category> results = myRealm.where(Category.class).findAll();
        myRealm.beginTransaction();
        if (results.size() > 0)
            id = myRealm.where(Category.class).max("id").intValue() + 1;
        else
            id=0;
        myRealm.commitTransaction();
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}
