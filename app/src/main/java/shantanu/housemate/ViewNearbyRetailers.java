package shantanu.housemate;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shantanu.housemate.Adapter.RetailerAdapter;
import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class ViewNearbyRetailers extends AppCompatActivity {

    private RetailerAdapter retailerAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;
    private String city;
    Context context;
    boolean isCustomer = true; /*getIntent().getBooleanExtra("isCustomer", false);*/
    private String customerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_retailers);

        city = getIntent().getStringExtra("city");
        customerUsername = getIntent().getStringExtra("customerUsername");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Retailers");

        context = getBaseContext();
        final ListView retailerListView = (ListView) findViewById(R.id.retailerList);

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        emptyStateTextView.setText("Sorry!!!\nNo Nearby Retailers found");
        retailerListView.setEmptyView(emptyStateTextView);

        ArrayList<RetailerData> retailerDatas = new ArrayList<RetailerData>();
        final RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
        try {
            db.open();
            retailerDatas = db.getDataOfCity(city);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        retailerAdapter = new RetailerAdapter(this, 0, retailerDatas);

        retailerListView.setAdapter(retailerAdapter);

        retailerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popupMenu = new PopupMenu(ViewNearbyRetailers.this, view, Gravity.CENTER);
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
                                    RetailerData retailerData = (RetailerData) parent.getItemAtPosition(position);
                                    try {
                                        db.open();
                                        db.deleteData(retailerData.getUsername());
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

                MenuPopupHelper menuHelper = new MenuPopupHelper(ViewNearbyRetailers.this, (MenuBuilder)
                        popupMenu.getMenu(), view);
                menuHelper.setForceShowIcon(true);
                try {
                    popupMenu.show();
                    menuHelper.show();
                } catch (Exception e) {
                    Log.e("PopUpMenu", e.getMessage());
                }
                return true;
            }
        });

        retailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RetailerData retailerData = (RetailerData) parent.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), retailerData.getUsername(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), RetailerProfile.class);
                intent.putExtra("retailerUsername", retailerData.getUsername());
                intent.putExtra("customerUsername", customerUsername);
                intent.putExtra("buttonEnabled", false);
                intent.putExtra("isCustomer", isCustomer);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
