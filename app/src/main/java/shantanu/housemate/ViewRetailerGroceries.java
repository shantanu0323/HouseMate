package shantanu.housemate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shantanu.housemate.Data.GroceryData;
import shantanu.housemate.DatabaseHelper.GroceryDatabaseHelper;
import shantanu.housemate.Adapter.RetailerGroceriesAdapter;

public class ViewRetailerGroceries extends AppCompatActivity {

    private RetailerGroceriesAdapter groceriesAdapter;
    private TextView emptyStateTextView;
    private LinearLayout addGrocery;
    private Button bAdd;
    private EditText etItemName, etItemPrice;
    private FloatingActionButton fabEdit;
    private TextInputLayout nameLabel, priceLabel;
    Context context;
    private Animation openAnimation, closeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_retailer_groceries);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Retailer Groceries");

        addGrocery = (LinearLayout) findViewById(R.id.addGrocery);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        bAdd = (Button) findViewById(R.id.bAddItem);
        nameLabel = (TextInputLayout) findViewById(R.id.nameLabel);
        priceLabel = (TextInputLayout) findViewById(R.id.priceLabel);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameLabel.setErrorEnabled(false);
                priceLabel.setErrorEnabled(false);
                String itemName = etItemName.getText().toString().trim();
                String itemPrice = etItemPrice.getText().toString().trim();
                boolean dataValid = true;
                if (itemName.equals("")) {
                    nameLabel.setError("Field cannot be empty");
                    dataValid = false;
                }
                if (itemPrice.equals("")) {
                    priceLabel.setError("Field cannot be empty");
                    dataValid = false;
                }
                if (dataValid) {
                    GroceryData groceryData = new GroceryData(itemName, itemPrice);
                    GroceryDatabaseHelper db = new GroceryDatabaseHelper(context);
                    try {
                        db.open();
                        db.putData(groceryData);
//                        if (isEditing) {
//                            db.deletePrevious(previousUsername);
//                            db.editData(customerData);
//                        } else {
//                            db.putData(customerData);
//                        }
                        db.close();
                    } catch (SQLException e) {
                        Snackbar.make(getCurrentFocus(), e.getMessage(),
                                Snackbar.LENGTH_LONG).show();
                    }

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Snackbar.make(getCurrentFocus(), "Item Added Successfully",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        addGrocery.setVisibility(View.GONE);
        context = getBaseContext();
        final ListView retailerGroceriesListView = (ListView) findViewById(R.id.retailerGroceriesList);

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        retailerGroceriesListView.setEmptyView(emptyStateTextView);

        ArrayList<GroceryData> groceriesDatas = new
                ArrayList<GroceryData>();
        final GroceryDatabaseHelper db = new GroceryDatabaseHelper(context);
        try {
            db.open();
            groceriesDatas = db.getAllData();
            db.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        groceriesAdapter = new RetailerGroceriesAdapter(this, 0, groceriesDatas);
        retailerGroceriesListView.setAdapter(groceriesAdapter);

        retailerGroceriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                final PopupMenu popupMenu = new PopupMenu(ViewRetailerGroceries.this, view, Gravity.CENTER);
                popupMenu.getMenuInflater().inflate(R.menu.menu_long_click, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Are you sure to" +
                                    " delete ?", Snackbar.LENGTH_LONG);
                            snackbar.setAction("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GroceryData groceryData =
                                            (GroceryData) parent.getItemAtPosition(position);
                                    try {
                                        db.open();
                                        db.deleteData(groceryData.getItemName());
                                        db.close();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        Toast.makeText(getBaseContext(), "Deleted!!!", Toast
                                                .LENGTH_LONG)
                                                .show();
                                    } catch (SQLException e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            snackbar.show();
                        } catch (Exception e) {
                            Log.e("Dialog", e.getMessage());
                        }
                        return true;
                    }
                });

                MenuPopupHelper menuHelper = new MenuPopupHelper(ViewRetailerGroceries.this, (MenuBuilder)
                        popupMenu.getMenu(), view);
                menuHelper.setForceShowIcon(true);
                try {
                    popupMenu.show();
                    menuHelper.show();
                } catch (Exception e) {
                    Log.e("PopUpMenu", e.getMessage());
                }
            }
        });

        openAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        closeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabEdit = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCircularReveal(v);
//                Toast.makeText(getBaseContext(), "FAB is clicked", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getBaseContext(), RetailerRegistration.class);
//                intent.putExtra("isEditing", true);
//                intent.putExtra("username", username);
//                startActivity(intent);
            }
        });


//        retailerGroceriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RetailerGroceriesData retailerGroceriesData = (RetailerGroceriesData) parent.getItemAtPosition(position);
//                Toast.makeText(getBaseContext(), retailerGroceriesData.getUsername(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getBaseContext(), RetailerGroceriesProfile.class);
//                intent.putExtra("username", retailerGroceriesData.getUsername());
//                startActivity(intent);
//            }
//        });
    }

    private void makeCircularReveal(View view) {

        int centerX = (view.getLeft() + view.getRight()) / 2;
        int centerY = (view.getTop() + view.getBottom()) / 2;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        float radius = Math.max(width, height) * 2.0f;

        if (addGrocery.getVisibility() == View.GONE) {
            addGrocery.setVisibility(View.VISIBLE);
            ViewAnimationUtils.createCircularReveal(addGrocery, centerX, centerY, 0, radius)
                    .setDuration(1000).start();
            fabEdit.startAnimation(openAnimation);

        } else {
            Animator reverse = ViewAnimationUtils.createCircularReveal(addGrocery, centerX, centerY,
                    radius, 0).setDuration(1000);
            reverse.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    addGrocery.setVisibility(View.GONE);
                }
            });
            reverse.start();
            fabEdit.startAnimation(closeAnimation);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_retailer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final GroceryDatabaseHelper db = new GroceryDatabaseHelper(context);
        int resId = item.getItemId();
        switch (resId) {
            case R.id.delete:
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Are you sure to" +
                        " delete ?", Snackbar.LENGTH_LONG);
                snackbar.setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            db.open();
                            db.clearDatabase();
                            db.close();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            Toast.makeText(getBaseContext(), "All details cleared successfully!!!", Toast
                                    .LENGTH_LONG).show();
                        } catch (SQLException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                snackbar.show();
                break;
        }
        return true;
    }

}
