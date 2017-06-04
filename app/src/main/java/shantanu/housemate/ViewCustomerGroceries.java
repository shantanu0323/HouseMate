package shantanu.housemate;

import android.content.Context;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shantanu.housemate.Adapter.CustomerGroceriesAdapter;
import shantanu.housemate.Data.CustomerGroceryData;
import shantanu.housemate.DatabaseHelper.CustomerGroceryDatabaseHelper;

public class ViewCustomerGroceries extends AppCompatActivity {

    private CustomerGroceriesAdapter unselectedGroceriesAdapter, selectedGroceriesAdapter;
    private Context context;
    private TextView emptyStateTextView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_groceries);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customer Groceries");

        context = getBaseContext();
        username = getIntent().getStringExtra("username");

        final ListView customerUnselectedGroceriesListView = (ListView) findViewById(R.id
                .customerUnselectedGroceriesList);
        final ListView customerSelectedGroceriesListView = (ListView) findViewById(R.id
                .customerSelectedGroceriesList);

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        customerUnselectedGroceriesListView.setEmptyView(emptyStateTextView);

        ArrayList<CustomerGroceryData> unselectedGroceriesDatas = new
                ArrayList<CustomerGroceryData>();
        final CustomerGroceryDatabaseHelper db = new CustomerGroceryDatabaseHelper(context);
        try {
            db.open();
            unselectedGroceriesDatas = db.getUnselectedDataOfCustomer(username);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        unselectedGroceriesAdapter = new CustomerGroceriesAdapter(this, 0,
                unselectedGroceriesDatas);

        customerUnselectedGroceriesListView.setAdapter(unselectedGroceriesAdapter);
        customerUnselectedGroceriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                final CustomerGroceryData groceryData = (CustomerGroceryData) parent
                        .getItemAtPosition(position);

                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    groceryData.setSelected(String.valueOf(checkBox.isChecked()));
                    groceryData.setUsername("empty");
                } else {
                    checkBox.setChecked(true);
                    groceryData.setSelected(String.valueOf(checkBox.isChecked()));
                    groceryData.setUsername(username);
                }
                editInDatabase(groceryData);
                customerSelectedGroceriesListView.invalidate();
                customerUnselectedGroceriesListView.invalidate();
                customerUnselectedGroceriesListView.invalidateViews();
                customerSelectedGroceriesListView.invalidateViews();
            }

            private void editInDatabase(CustomerGroceryData groceryData) {
                CustomerGroceryDatabaseHelper db = new CustomerGroceryDatabaseHelper(context);
                db.open();
                db.deleteData(groceryData.getItemName());
                db.editData(groceryData);
                db.close();
            }
        });

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        customerSelectedGroceriesListView.setEmptyView(emptyStateTextView);

        ArrayList<CustomerGroceryData> selectedGroceriesDatas = new
                ArrayList<CustomerGroceryData>();
        final CustomerGroceryDatabaseHelper db2 = new CustomerGroceryDatabaseHelper(context);
        try {
            db2.open();
            selectedGroceriesDatas = db2.getSelectedDataOfCustomer(username);
            db2.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        selectedGroceriesAdapter = new CustomerGroceriesAdapter(this, 0, selectedGroceriesDatas);

        customerSelectedGroceriesListView.setAdapter(selectedGroceriesAdapter);
        customerSelectedGroceriesListView.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                final CustomerGroceryData groceryData = (CustomerGroceryData) parent
                        .getItemAtPosition(position);

                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    groceryData.setSelected(String.valueOf(checkBox.isChecked()));
                    groceryData.setUsername("empty");
                } else {
                    checkBox.setChecked(true);
                    groceryData.setSelected(String.valueOf(checkBox.isChecked()));
                    groceryData.setUsername(username);
                }
                editInDatabase(groceryData);
                customerSelectedGroceriesListView.invalidate();
                customerUnselectedGroceriesListView.invalidate();
                customerUnselectedGroceriesListView.invalidateViews();
                customerSelectedGroceriesListView.invalidateViews();
            }

            private void editInDatabase(CustomerGroceryData groceryData) {
                CustomerGroceryDatabaseHelper db = new CustomerGroceryDatabaseHelper(context);
                db.open();
                db.deleteData(groceryData.getItemName());
                db.editData(groceryData);
                db.close();
            }
        });

    }
}
