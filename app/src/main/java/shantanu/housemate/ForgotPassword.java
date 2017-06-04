package shantanu.housemate;

import android.content.Context;
import android.database.SQLException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import shantanu.housemate.Data.AdminData;
import shantanu.housemate.DatabaseHelper.AdminDatabaseHelper;
import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;
import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class ForgotPassword extends AppCompatActivity {

    private String choice;
    private EditText etUsername;
    private TextView tvShowUsername, tvShowPasswrd;
    private LinearLayout frame;
    private Button bGetPassword, bOK;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUsername = (EditText) findViewById(R.id.etUsername);
        bGetPassword = (Button) findViewById(R.id.bGetPassword);
        bOK = (Button) findViewById(R.id.bOK);
        frame = (LinearLayout) findViewById(R.id.frame);
        tvShowUsername = (TextView) findViewById(R.id.tvShowUsername);
        tvShowPasswrd = (TextView) findViewById(R.id.tvShowPassword);

        choice = getIntent().getStringExtra("choice");
        bGetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                username = etUsername.getText().toString();
                boolean isUsernameInDatabase = false;
                switch (choice) {
                    case "admin":
                        AdminData adminData = new AdminData();
                        adminData = getAdminData(username);
                        if (adminData.getUsername().equals("empty")) {
                            isUsernameInDatabase = false;
                            break;
                        } else {
                            isUsernameInDatabase = true;
                        }
                        tvShowUsername.setText(adminData.getUsername());
                        tvShowPasswrd.setText(adminData.getPassword());
                        break;
                    case "customer":
                        CustomerData customerData = new CustomerData();
                        customerData = getCustomerData(username);
                        if (customerData.getUsername().equals("empty")) {
                            isUsernameInDatabase = false;
                            break;
                        } else {
                            isUsernameInDatabase = true;
                        }
                        tvShowUsername.setText(customerData.getUsername());
                        tvShowPasswrd.setText(customerData.getPassword());
                        break;
                    case "retailer":
                        RetailerData retailerData = new RetailerData();
                        retailerData = getRetailerData(username);
                        if (retailerData.getUsername().equals("empty")) {
                            isUsernameInDatabase = false;
                            break;
                        } else {
                            isUsernameInDatabase = true;
                        }
                        tvShowUsername.setText(retailerData.getUsername());
                        tvShowPasswrd.setText(retailerData.getPassword());
                        break;
                }
                if (isUsernameInDatabase) {
                    bOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            frame.setVisibility(View.GONE);
                            finish();
                        }
                    });
                    frame.setVisibility(View.VISIBLE);
                } else {
                    // Username not Present
                    if (username.equals("")) {
                        etUsername.setError("Username cannot be Empty");
                    } else {
                        Snackbar.make(getCurrentFocus(), "Username not in " +
                                "Database", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RetailerData getRetailerData(String username) {
        RetailerDatabaseHelper db = new RetailerDatabaseHelper(getBaseContext());
        RetailerData retailerData = new RetailerData();
        try {
            db.open();
            retailerData = db.getData(username);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return retailerData;
    }

    private CustomerData getCustomerData(String username) {
        CustomerDatabaseHelper db = new CustomerDatabaseHelper(getBaseContext());
        CustomerData custData = new CustomerData();
        try {
            db.open();
            custData = db.getData(username);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return custData;
    }

    private AdminData getAdminData(String username) {
        AdminDatabaseHelper db = new AdminDatabaseHelper(getBaseContext());
        AdminData adminData = new AdminData();
        try {
            db.open();
            adminData = db.retrieveData(username);
            db.close();
        } catch (SQLException e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return adminData;
    }
}
