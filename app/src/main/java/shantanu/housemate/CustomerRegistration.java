package shantanu.housemate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;

public class CustomerRegistration extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener {

    // Assigning the elements to global variables
    ImageButton bNoOfGroceronsorsInfo = null;
    Button bCustSubmit;
    private Animation blinkAnimation;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNo;
    private EditText address;
    private EditText city;
    private EditText pinCode;
    private EditText noOfG;
    private EditText username;
    private EditText password;
    private EditText cnfPassword;
    Context context = this;
    private boolean isEditing = false;
    private String usernameToEdit;
    private String previousUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        isEditing = getIntent().getBooleanExtra("isEditing", false);
        // Initilize the variables
        initEditTexts();

        if (isEditing) {
            usernameToEdit = getIntent().getStringExtra("username");
            CustomerDatabaseHelper db = new CustomerDatabaseHelper(context);
            CustomerData customerData = new CustomerData();
            try {
                db.open();
                customerData = db.getData(usernameToEdit);
                db.close();
            } catch (SQLException e) {
                Snackbar.make(getCurrentFocus(), e.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
            firstName.setText(customerData.getFirstName());
            lastName.setText(customerData.getLastName());
            email.setText(customerData.getEmail());
            phoneNo.setText(customerData.getPhoneNo());
            address.setText(customerData.getAddress());
            city.setText(customerData.getCity());
            pinCode.setText(customerData.getPinCode());
            noOfG.setText("" + customerData.getNoOfG());
            username.setText(customerData.getUsername());
            password.setText(customerData.getPassword());
            cnfPassword.setText(customerData.getPassword());
            previousUsername = customerData.getUsername();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById
                (R.id.customerRegistrationCollapsingT);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim
                .blink);
        bNoOfGroceronsorsInfo = (ImageButton) findViewById(R.id.bNoOfGroceronsorsInfo);
        bCustSubmit = (Button) findViewById(R.id.bCustSubmit);

        // Actions
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        // Setting the OnClickListeners
        if (noOfG != null) {
            noOfG.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        bNoOfGroceronsorsInfo.startAnimation(blinkAnimation);
                    } else {
                        bNoOfGroceronsorsInfo.clearAnimation();
                    }
                }
            });
        }
        if (bCustSubmit != null) {
            bCustSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    CustomerData customerData = new CustomerData();
                    putDataInCustomer(customerData);
                    if (isDataValid(customerData)) {
                        CustomerDatabaseHelper db = new CustomerDatabaseHelper(context);
                        try {
                            db.open();
                            if (isEditing) {
                                db.deletePrevious(previousUsername);
                                db.editData(customerData);
                            } else {
                                db.putData(customerData);
                            }
                            db.close();
                        } catch (SQLException e) {
                            Snackbar.make(getCurrentFocus(), e.getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                        Toast.makeText(getBaseContext(), "Sign Up Successful!!!\nYou would be now" +
                                " redirected to the Login Page", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), LoginPage.class);
                        startActivity(intent);
                    } else {
                        Snackbar.make(getCurrentFocus(), "Please fill the details properly",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
        if (bNoOfGroceronsorsInfo != null) {
            bNoOfGroceronsorsInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegistration.this);
                    builder.setCancelable(true);
                    builder.setIcon(R.drawable.info);
                    builder.setTitle("What are Groceronsors?");
                    builder.setMessage("Groceronsors are the sensors that are going to be attached in" +
                            " each grocery container at your kitchen in order to measure their" +
                            " respective quantity at any instant of time.");
                    builder.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    private void initEditTexts() {
        firstName = (EditText) findViewById(R.id.etCustFirstName);
        lastName = (EditText) findViewById(R.id.etCustLastName);
        email = (EditText) findViewById(R.id.etCustEmail);
        phoneNo = (EditText) findViewById(R.id.etCustPhone);
        address = (EditText) findViewById(R.id.etCustAddress);
        city = (EditText) findViewById(R.id.etCustCityLabel);
        pinCode = (EditText) findViewById(R.id.etCustPincode);
        noOfG = (EditText) findViewById(R.id.etNoOfG);
        username = (EditText) findViewById(R.id.etCustUsername);
        password = (EditText) findViewById(R.id.etCustPassword);
        cnfPassword = (EditText) findViewById(R.id.etCustCnfPassword);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resId = item.getItemId();
        switch (resId) {
            case R.id.action_reset:
                Snackbar.make(getCurrentFocus(), "Reset button clicked", Snackbar.LENGTH_LONG).show();
                resetEditTexts();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void resetEditTexts() {
        // hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // clear the edit texts
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        phoneNo.setText("");
        address.setText("");
        city.setText("");
        pinCode.setText("");
        noOfG.setText("");
        username.setText("");
        password.setText("");
        cnfPassword.setText("");

        TextInputLayout firstName = (TextInputLayout) findViewById(R.id.custFirstNameLabel);
        TextInputLayout lastName = (TextInputLayout) findViewById(R.id.custLastNameLabel);
        TextInputLayout address = (TextInputLayout) findViewById(R.id.custAddressLabel);
        TextInputLayout city = (TextInputLayout) findViewById(R.id.custCityLabel);
        TextInputLayout email = (TextInputLayout) findViewById(R.id.custEmailLabel);
        TextInputLayout phoneNo = (TextInputLayout) findViewById(R.id.custPhoneLabel);
        TextInputLayout pinCode = (TextInputLayout) findViewById(R.id.custPincodeLabel);
        TextInputLayout noOfG = (TextInputLayout) findViewById(R.id.noOfG);
        TextInputLayout username = (TextInputLayout) findViewById(R.id.custUsernameLabel);
        TextInputLayout password = (TextInputLayout) findViewById(R.id.custPasswordLabel);
        TextInputLayout cnfPassword = (TextInputLayout) findViewById(R.id.custCnfPasswordLabel);

        firstName.setErrorEnabled(false);
        lastName.setErrorEnabled(false);
        address.setErrorEnabled(false);
        city.setErrorEnabled(false);
        email.setErrorEnabled(false);
        phoneNo.setErrorEnabled(false);
        pinCode.setErrorEnabled(false);
        noOfG.setErrorEnabled(false);
        username.setErrorEnabled(false);
        password.setErrorEnabled(false);
        cnfPassword.setErrorEnabled(false);

        Snackbar.make(getCurrentFocus(), "Details Cleared!!!", Snackbar.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.navHome:
                intent = new Intent(CustomerRegistration.this, HomeActivity.class);
                break;
            case R.id.navRetailerRegistration:
                intent = new Intent(CustomerRegistration.this, RetailerRegistration.class);
                break;
            case R.id.navLogin:
                intent = new Intent(CustomerRegistration.this, LoginPage.class);
                break;
        }
        finish();
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void putDataInCustomer(CustomerData customerData) {
        customerData.setFirstName(firstName.getText().toString().trim());
        customerData.setLastName(lastName.getText().toString().trim());
        customerData.setAddress(address.getText().toString());
        customerData.setCity(city.getText().toString().trim());
        customerData.setEmail(email.getText().toString().trim());
        String noOfGstr = noOfG.getText().toString().trim();
        if (noOfGstr.length() != 0) {
            customerData.setNoOfG(Integer.parseInt(noOfGstr));
        } else {
            customerData.setNoOfG(0);
        }
        customerData.setPhoneNo(phoneNo.getText().toString().trim());
        customerData.setPinCode(pinCode.getText().toString().trim());
        customerData.setUsername(username.getText().toString().trim());
        customerData.setPassword(password.getText().toString());
        customerData.setCnfPassword(cnfPassword.getText().toString());
    }

    public boolean isDataValid(CustomerData customerData) {
        TextInputLayout firstName = (TextInputLayout) findViewById(R.id.custFirstNameLabel);
        TextInputLayout lastName = (TextInputLayout) findViewById(R.id.custLastNameLabel);
        TextInputLayout address = (TextInputLayout) findViewById(R.id.custAddressLabel);
        TextInputLayout city = (TextInputLayout) findViewById(R.id.custCityLabel);
        TextInputLayout email = (TextInputLayout) findViewById(R.id.custEmailLabel);
        TextInputLayout phoneNo = (TextInputLayout) findViewById(R.id.custPhoneLabel);
        TextInputLayout pinCode = (TextInputLayout) findViewById(R.id.custPincodeLabel);
        TextInputLayout noOfG = (TextInputLayout) findViewById(R.id.noOfG);
        TextInputLayout username = (TextInputLayout) findViewById(R.id.custUsernameLabel);
        TextInputLayout password = (TextInputLayout) findViewById(R.id.custPasswordLabel);
        TextInputLayout cnfPassword = (TextInputLayout) findViewById(R.id.custCnfPasswordLabel);

        boolean flag = true;

        firstName.setErrorEnabled(false);
        lastName.setErrorEnabled(false);
        address.setErrorEnabled(false);
        city.setErrorEnabled(false);
        email.setErrorEnabled(false);
        phoneNo.setErrorEnabled(false);
        pinCode.setErrorEnabled(false);
        noOfG.setErrorEnabled(false);
        username.setErrorEnabled(false);
        password.setErrorEnabled(false);
        cnfPassword.setErrorEnabled(false);

        if (!(customerData.getFirstName().length() != 0)) {
            firstName.setError("Field can't be empty");
            flag = false;
        }
        if (!(customerData.getLastName().length() != 0)) {
            lastName.setError("Field can't be empty");
            flag = false;
        }
        if (!(customerData.getAddress().length() != 0)) {
            address.setError("Field can't be empty");
            flag = false;
        }
        if (!(customerData.getCity().length() != 0)) {
            city.setError("Field can't be empty");
            flag = false;
        }
        if (!(customerData.getEmail().length() != 0)) {
            email.setError("Field can't be empty");
            flag = false;
        } else if (!(customerData.getEmail().contains("@") && !customerData.getEmail().contains(" "))) {
            email.setError("Please enter a valid Email-ID");
            flag = false;
        }
        if (!(customerData.getPhoneNo().length() != 0)) {
            phoneNo.setError("Field can't be empty");
            flag = false;
        } else if (!(customerData.getPhoneNo().matches("[0-9]+") &&
                customerData.getPhoneNo().length() == 10)) {
            phoneNo.setError("Please enter a valid Phone No");
            flag = false;
        }
        if (!(customerData.getPinCode().length() != 0)) {
            pinCode.setError("Field can't be empty");
            flag = false;
        } else if (!(customerData.getPinCode().matches("[0-9]+") &&
                customerData.getPinCode().length() == 6)) {
            pinCode.setError("Please enter a valid PIN Code");
            flag = false;
        }
        if (!(customerData.getNoOfG() > 0 && customerData.getNoOfG() <= 100)) {
            noOfG.setError("Should be between 1 and 100");
            flag = false;
        }
        if (!(customerData.getUsername().length() != 0)) {
            username.setError("Field can't be empty");
            flag = false;
        } else if (isEditing == false && (!checkUsernameWithDatabase(customerData.getUsername()))) {
            username.setError("Username already taken");
            flag = false;
        }
        if (!(customerData.getPassword().length() != 0)) {
            password.setError("Field can't be empty");
            flag = false;
        }
        if (!(customerData.getCnfPassword().length() != 0)) {
            cnfPassword.setError("Field can't be empty");
            flag = false;
        } else if (!(customerData.getPassword().equals(customerData.getCnfPassword()))) {
            this.cnfPassword.setText("");
            cnfPassword.setError("Passwords do not match");
            flag = false;
        }
        return flag;
    }

    private boolean checkUsernameWithDatabase(String username) {
        CustomerDatabaseHelper db = new CustomerDatabaseHelper(context);
        String[] usernames = new String[100];
        try {
            db.open();
            usernames = db.retrieveUsernames();
            db.close();
        } catch (SQLException e) {
            Snackbar.make(getCurrentFocus(), e.getMessage(),
                    Snackbar.LENGTH_LONG).show();
        }
        for (int i = 0; i < usernames.length; i++) {
            if (username.equals(usernames[i])) {
                return false;
            }
        }
        return true;
    }

}
