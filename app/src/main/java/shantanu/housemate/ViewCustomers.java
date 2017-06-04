package shantanu.housemate;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import shantanu.housemate.Adapter.CustomerAdapter;
import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;

public class ViewCustomers extends AppCompatActivity {

    private CustomerAdapter customerAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Customers");

        context = getBaseContext();
        ListView customerListView = (ListView) findViewById(R.id.customerList);

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        customerListView.setEmptyView(emptyStateTextView);

        ArrayList<CustomerData> customerDatas = new ArrayList<CustomerData>();
        final CustomerDatabaseHelper db = new CustomerDatabaseHelper(context);
        try {
            db.open();
            customerDatas = db.getAllData();
            db.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        customerAdapter = new CustomerAdapter(this, 0, customerDatas);

        customerListView.setAdapter(customerAdapter);

        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerData customerData = (CustomerData) parent.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), customerData.getUsername(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), CustomerProfile.class);
                intent.putExtra("username", customerData.getUsername());
                intent.putExtra("buttonEnabled", false);
                intent.putExtra("isAdmin", true);
                startActivity(intent);
            }
        });

        customerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popupMenu = new PopupMenu(ViewCustomers.this, view, Gravity.CENTER);
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
                                    CustomerData CustomerData = (CustomerData) parent.getItemAtPosition(position);
                                    try {
                                        db.open();
                                        db.deleteData(CustomerData.getUsername());
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

        MenuPopupHelper menuHelper = new MenuPopupHelper(ViewCustomers.this, (MenuBuilder)
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


    }
}
