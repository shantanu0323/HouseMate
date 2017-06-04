package shantanu.housemate;

import android.content.Intent;
import android.database.SQLException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;
import shantanu.housemate.bluetoothchat.BluetoothActivity;

public class CustomerProfile extends AppCompatActivity {

    //Declaring the layout items
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPhoneNo;
    private TextView tvAddress;
    private TextView tvCity;
    private TextView tvPincode;
    private TextView tvNog;
    private TextView tvName;
    private FloatingActionButton bChooseGroceries;
    private String username;
    private String title;
    private boolean buttonEnabled;
    private FloatingActionButton bGenerateList;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_customer_profile);

        username = getIntent().getStringExtra("username");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvPincode = (TextView) findViewById(R.id.tvPincode);
        tvNog = (TextView) findViewById(R.id.tvNog);
        tvName = (TextView) findViewById(R.id.tvName);
        bChooseGroceries = (FloatingActionButton) findViewById(R.id.bChooseGroceries);
        bGenerateList = (FloatingActionButton) findViewById(R.id.bGenerateList);
        if (isAdmin) {
            bGenerateList.setVisibility(View.GONE);
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        CustomerDatabaseHelper db = new CustomerDatabaseHelper(getBaseContext());
        CustomerData customerData = new CustomerData();
        try {
            db.open();
            customerData = db.getData(username);
            db.close();
        } catch (SQLException e) {
            Snackbar.make(getCurrentFocus(), e.getMessage(),
                    Snackbar.LENGTH_LONG).show();
        }
        tvUsername.setText(customerData.getUsername());
        tvEmail.setText(customerData.getEmail());
        tvPhoneNo.setText(customerData.getPhoneNo());
        tvAddress.setText(customerData.getAddress());
        tvCity.setText(customerData.getCity());
        tvPincode.setText(customerData.getPinCode());
        tvNog.setText("" + customerData.getNoOfG());

        title = customerData.getFirstName() + " " + customerData.getLastName();
        tvName.setText(title);
//        getSupportActionBar().setTitle(title);


        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        buttonEnabled = getIntent().getBooleanExtra("buttonEnabled", true);
        if (buttonEnabled) {

            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "FAB is clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getBaseContext(), CustomerRegistration.class);
                    intent.putExtra("isEditing", true);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            });

            bChooseGroceries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ViewCustomerGroceries.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            });

            final CustomerData finalCustomerData1 = customerData;
            bGenerateList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), GenerateList.class);
                    intent.putExtra("username", finalCustomerData1.getUsername());
                    intent.putExtra("city", finalCustomerData1.getCity());
                    startActivity(intent);
                }
            });
        } else {
            fabEdit.setVisibility(View.GONE);
            bChooseGroceries.setVisibility(View.GONE);
        }
    }

}
