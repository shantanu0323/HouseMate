package shantanu.housemate;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import shantanu.housemate.Adapter.GenerateListAdapter;
import shantanu.housemate.Data.CustomerGroceryData;
import shantanu.housemate.Data.GenerateListData;
import shantanu.housemate.Data.OrderData;
import shantanu.housemate.DatabaseHelper.CustomerGroceryDatabaseHelper;
import shantanu.housemate.DatabaseHelper.GenerateListDatabaseHelper;
import shantanu.housemate.DatabaseHelper.OrderDatabaseHelper;
import shantanu.housemate.bluetoothchat.BluetoothActivity;

public class GenerateList extends AppCompatActivity {
    private GenerateListAdapter groceriesAdapter;
    private Context context;
    private TextView emptyStateTextView, tvBill;
    private String username;
    private Button bNearbyRetailers;
    private String city;
    private float bill;
    private String quantityValue;
    private float quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Quantity");

        context = getBaseContext();
        username = getIntent().getStringExtra("username");
        city = getIntent().getStringExtra("city");
        quantityValue = getIntent().getStringExtra("quantityValue");
        if (quantityValue != null) {
            quantity = Float.parseFloat(quantityValue);
            quantity = (quantity / 10) * 1;
            quantityValue = "" + quantity;
            Log.e("QUANTITY", quantityValue);
        }
        bill = 0;
        bNearbyRetailers = (Button) findViewById(R.id.bNearbyRetailers);
        bNearbyRetailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewNearbyRetailers.class);
                intent.putExtra("city", city);
                intent.putExtra("customerUsername", username);
                intent.putExtra("isCustomer", true);
                startActivity(intent);
            }
        });

        tvBill = (TextView) findViewById(R.id.tvBill);
        final ListView generatedList = (ListView) findViewById(R.id.generatedList);

        emptyStateTextView = (TextView) findViewById(R.id.emptyView);
        generatedList.setEmptyView(emptyStateTextView);

        ArrayList<CustomerGroceryData> selectedGroceriesDatas = new
                ArrayList<CustomerGroceryData>();
        ArrayList<GenerateListData> groceriesDatas = new ArrayList<GenerateListData>();

        // Retrieving the seleceted groceryData from the customerGroceryDatabase
        final CustomerGroceryDatabaseHelper cdb = new CustomerGroceryDatabaseHelper(context);
        try {
            cdb.open();
            selectedGroceriesDatas = cdb.getSelectedDataOfCustomer(username);
            cdb.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Putting the datas into the GenerateListData
        final GenerateListDatabaseHelper db = new GenerateListDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            bill = 0;
            for (int i = 0; i < selectedGroceriesDatas.size(); i++) {
                db.putData(new GenerateListData(selectedGroceriesDatas.get(i).getItemName(),
                        selectedGroceriesDatas.get(i).getItemPrice(), username));
            }
            db.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Retrieving the data from the GenerateListDatabase
        final GenerateListDatabaseHelper fdb = new GenerateListDatabaseHelper(context);
        try {
            fdb.open();
            groceriesDatas = fdb.getAllData();
            fdb.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        groceriesDatas.get(0).setQuantity(quantityValue);
        GenerateListData groceryData = groceriesDatas.get(0);
        groceryData.setQuantity(quantityValue);
        groceryData.setUsername(username);
        editInDatabase(groceryData);
        groceriesAdapter = new GenerateListAdapter(this, 0, groceriesDatas);


        generatedList.setAdapter(groceriesAdapter);

        generatedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                final GenerateListData groceryData = (GenerateListData) parent
                        .getItemAtPosition(position);
                final EditText etQuantity = (EditText) view.findViewById(R.id.etQuantity);
                if (position == 0) {
                    etQuantity.setText(quantityValue);
                }
                final TextView tvItemPrice = (TextView) view.findViewById(R.id.tvItemPrice);


                final LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
                if (!etQuantity.isFocused()) {
                    itemLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    if (etQuantity.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                        groceryData.setQuantity(etQuantity.getText().toString());
//                        editInDatabase(groceryData);
                    }
                } else {
                    itemLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//                    groceryData.setQuantity(etQuantity.getText().toString());
//                    editInDatabase(groceryData);
                }
                etQuantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String quantityStr = etQuantity.getText().toString();
                        Log.e("QUANTITYSTR", quantityStr);
                        groceryData.setQuantity(quantityStr);
                        groceryData.setUsername(username);
                        editInDatabase(groceryData);
                    }
                });
            }

            private void editInDatabase(GenerateListData groceryData) {
                GenerateListDatabaseHelper db = new GenerateListDatabaseHelper(context);
                db.open();
                db.deleteData(groceryData.getItemName());
                db.editData(groceryData);
                db.close();
            }
        });


        Button bCalculateResult = (Button) findViewById(R.id.bCalculateBill);
        bCalculateResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieving the data from the GenerateListDatabase
                final GenerateListDatabaseHelper fdb = new GenerateListDatabaseHelper(context);
                ArrayList<GenerateListData> groceriesDatas = new ArrayList<GenerateListData>();
                try {
                    fdb.open();
                    groceriesDatas = fdb.getAllData();
                    fdb.close();
                } catch (SQLException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                String str = "";
                bill = 0;
                int i = 0;
                for (i = 0; i < groceriesDatas.size(); i++) {
                    str += groceriesDatas.get(i).getItemName() + " : " + groceriesDatas.get(i)
                            .getQuantity() + "\t\n";
                    try {
                        String quantityStr = groceriesDatas.get(i).getQuantity();
                        float quantity = Float.parseFloat(quantityStr);
                        String value = groceriesDatas.get(i).getItemPrice();
                        String priceStr = value.replaceAll("[^0-9]", "");
                        float price = Integer.parseInt(priceStr);
                        bill += quantity * price;
                    } catch (NumberFormatException e) {
                        Toast.makeText(getBaseContext(), "Please fill the quantity " +
                                "correctly" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                str += groceriesDatas.get(i-1).getUsername() + "'s Bill = \t" + bill;
                Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
                tvBill.setText("" + bill);
                if (bill > 0.0f) {
                    bNearbyRetailers.setVisibility(View.VISIBLE);
                    OrderData orderData= new OrderData();
                    orderData.setCustomerUsername(username);
                    orderData.setBill("" + bill);
                    OrderDatabaseHelper db = new OrderDatabaseHelper(context);
                    try {
                        db.open();
                        db.putData(orderData);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void editInDatabase(GenerateListData groceryData) {
        GenerateListDatabaseHelper db = new GenerateListDatabaseHelper(context);
        db.open();
        db.deleteData(groceryData.getItemName());
        db.editData(groceryData);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.generate_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resId = item.getItemId();
        switch (resId) {
            case R.id.retrieveQuantity:
                Intent intent = new Intent(getBaseContext(), BluetoothActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("city", city);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
