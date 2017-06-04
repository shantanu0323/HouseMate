package shantanu.housemate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class RetailerRegistration extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Assigning the elements to global varibles
    ImageButton bRadiusOfServiceInfo;
    private Button bRetailSubmit;
    private Animation blinkAnimation;
    private EditText shopName;
    private EditText email;
    private EditText phoneNo;
    private EditText address;
    private EditText city;
    private EditText pinCode;
    private EditText radiusOfService;
    private EditText username;
    private EditText password;
    private EditText cnfPassword;
    Context context = this;
    private boolean isEditing;
    private String usernameToEdit;
    private String previousUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_registration);

        // Initializing the components
        initEditTexts();
        isEditing = getIntent().getBooleanExtra("isEditing", false);

        if (isEditing) {
            usernameToEdit = getIntent().getStringExtra("username");
            RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
            RetailerData retailerData = new RetailerData();
            try {
                db.open();
                retailerData = db.getData(usernameToEdit);
                db.close();
            } catch (SQLException e) {
                Snackbar.make(getCurrentFocus(), e.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
            shopName.setText(retailerData.getShopName());
            email.setText(retailerData.getEmail());
            phoneNo.setText(retailerData.getPhoneNo());
            address.setText(retailerData.getAddress());
            city.setText(retailerData.getCity());
            pinCode.setText(retailerData.getPinCode());
            radiusOfService.setText("" + retailerData.getRadiusOfService());
            username.setText(retailerData.getUsername());
            password.setText(retailerData.getPassword());
            cnfPassword.setText(retailerData.getPassword());
            previousUsername = retailerData.getUsername();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Typeface font = null;
        bRetailSubmit = (Button) findViewById(R.id.bRetailSubmit);
        bRadiusOfServiceInfo = (ImageButton) findViewById(R.id.bRadiusOfServiceInfo);
        blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim
                .blink);
        radiusOfService = (EditText) findViewById(R.id.etRadiusOfService);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        // Setting up the customFonts
        try {
            font = Typeface.createFromAsset(getAssets(), "drift.ttf");
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


        // Actions
        setSupportActionBar(toolbar);


        // Customizing the layout
        if (font != null) {

        }

        // Setting the OnClickListeners
        drawer.setDrawerListener(toggle);
        if (bRetailSubmit != null) {
            bRetailSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    RetailerData retailerData = new RetailerData();
                    putDataInRetailer(retailerData);
                    if (isDataValid(retailerData)) {
                        Snackbar.make(getCurrentFocus(), "Cool!!! Details are Correct!!!",
                                Snackbar.LENGTH_LONG).show();
                        RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
                        try {
                            db.open();
                            if (isEditing) {
                                db.deletePrevious(previousUsername);
                                db.editInfo(retailerData);
                            } else {
                                db.putData(retailerData);
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
        if (bRadiusOfServiceInfo != null) {
            bRadiusOfServiceInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RetailerRegistration.this);
                    builder.setCancelable(true);
                    builder.setIcon(R.drawable.info);
                    builder.setTitle("What is Radius Of Service?");
                    builder.setMessage("It is basically the radius of the region around the shop " +
                            "throughout which the shop could extend its delivery services.");
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

        if (radiusOfService != null) {
            radiusOfService.setOnFocusChangeListener(new View
                    .OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        bRadiusOfServiceInfo.startAnimation(blinkAnimation);
                    } else {
                        bRadiusOfServiceInfo.clearAnimation();
                    }
                }
            });
        }

    }

    private void initEditTexts() {
        shopName = (EditText) findViewById(R.id.etRetailShopName);
        email = (EditText) findViewById(R.id.etRetailEmail);
        phoneNo = (EditText) findViewById(R.id.etRetailPhone);
        address = (EditText) findViewById(R.id.etRetailAddress);
        city = (EditText) findViewById(R.id.etRetailCityLabel);
        pinCode = (EditText) findViewById(R.id.etRetailPincode);
        radiusOfService = (EditText) findViewById(R.id.etRadiusOfService);
        username = (EditText) findViewById(R.id.etRetailUsername);
        password = (EditText) findViewById(R.id.etRetailPassword);
        cnfPassword = (EditText) findViewById(R.id.etRetailCnfPassword);
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
        getMenuInflater().inflate(R.menu.retailer_registration, menu);
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
        shopName.setText("");
        email.setText("");
        phoneNo.setText("");
        address.setText("");
        city.setText("");
        pinCode.setText("");
        radiusOfService.setText("");
        username.setText("");
        password.setText("");
        cnfPassword.setText("");

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
                intent = new Intent(RetailerRegistration.this, HomeActivity.class);
                break;
            case R.id.navRetailerRegistration:
                intent = new Intent(RetailerRegistration.this, RetailerRegistration.class);
                break;
            case R.id.navLogin:
                intent = new Intent(RetailerRegistration.this, LoginPage.class);
                break;
        }
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void putDataInRetailer(RetailerData retailerData) {
        retailerData.setShopName(shopName.getText().toString().trim());
        retailerData.setAddress(address.getText().toString());
        retailerData.setCity(city.getText().toString().trim());
        retailerData.setEmail(email.getText().toString().trim());
        String radiusOfServicestr = radiusOfService.getText().toString().trim();
        if (radiusOfServicestr.length() != 0) {
            retailerData.setRadiusOfService(Integer.parseInt(radiusOfServicestr));
        } else {
            retailerData.setRadiusOfService(0);
        }
        retailerData.setPhoneNo(phoneNo.getText().toString().trim());
        retailerData.setPinCode(pinCode.getText().toString().trim());
        retailerData.setUsername(username.getText().toString().trim());
        retailerData.setPassword(password.getText().toString());
        retailerData.setCnfPassword(cnfPassword.getText().toString());
    }

    public boolean isDataValid(RetailerData retailerData) {
        TextInputLayout shopName = (TextInputLayout) findViewById(R.id.retailShopNameLabel);
        TextInputLayout address = (TextInputLayout) findViewById(R.id.retailAddressLabel);
        TextInputLayout city = (TextInputLayout) findViewById(R.id.retailCityLabel);
        TextInputLayout email = (TextInputLayout) findViewById(R.id.retailEmailLabel);
        TextInputLayout phoneNo = (TextInputLayout) findViewById(R.id.retailPhoneLabel);
        TextInputLayout pinCode = (TextInputLayout) findViewById(R.id.retailPincodeLabel);
        TextInputLayout radiusOfService = (TextInputLayout) findViewById(R.id.radiusOfService);
        TextInputLayout username = (TextInputLayout) findViewById(R.id.retailUsernameLabel);
        TextInputLayout password = (TextInputLayout) findViewById(R.id.retailPasswordLabel);
        TextInputLayout cnfPassword = (TextInputLayout) findViewById(R.id.retailCnfPasswordLabel);

        boolean flag = true;

        shopName.setErrorEnabled(false);
        address.setErrorEnabled(false);
        city.setErrorEnabled(false);
        email.setErrorEnabled(false);
        phoneNo.setErrorEnabled(false);
        pinCode.setErrorEnabled(false);
        radiusOfService.setErrorEnabled(false);
        username.setErrorEnabled(false);
        password.setErrorEnabled(false);
        cnfPassword.setErrorEnabled(false);

        if (!(retailerData.getShopName().length() != 0)) {
            shopName.setError("Field can't be empty");
            flag = false;
        }
        if (!(retailerData.getAddress().length() != 0)) {
            address.setError("Field can't be empty");
            flag = false;
        }
        if (!(retailerData.getCity().length() != 0)) {
            city.setError("Field can't be empty");
            flag = false;
        }
        if (!(retailerData.getEmail().length() != 0)) {
            email.setError("Field can't be empty");
            flag = false;
        } else if (!(retailerData.getEmail().contains("@") && !retailerData.getEmail().contains(" "))) {
            email.setError("Please enter a valid Email-ID");
            flag = false;
        }
        if (!(retailerData.getPhoneNo().length() != 0)) {
            phoneNo.setError("Field can't be empty");
            flag = false;
        } else if (!(retailerData.getPhoneNo().matches("[0-9]+") &&
                retailerData.getPhoneNo().length() == 10)) {
            phoneNo.setError("Please enter a valid Phone No");
            flag = false;
        }
        if (!(retailerData.getPinCode().length() != 0)) {
            pinCode.setError("Field can't be empty");
            flag = false;
        } else if (!(retailerData.getPinCode().matches("[0-9]+") &&
                retailerData.getPinCode().length() == 6)) {
            pinCode.setError("Please enter a valid PIN Code");
            flag = false;
        }
        if (!(retailerData.getRadiusOfService() > 0 && retailerData.getRadiusOfService() < 100)) {
            radiusOfService.setError("Should be between 1 and 100");
            flag = false;
        }
        if (!(retailerData.getUsername().length() != 0)) {
            Log.e("isEditing", "" + isEditing);
            username.setError("Field can't be empty");
            flag = false;
        } else if (isEditing == false && (!checkUsernameWithDatabase(retailerData.getUsername()))) {
            username.setError("Username already taken");
            flag = false;
        }
        if (!(retailerData.getPassword().length() != 0)) {
            password.setError("Field can't be empty");
            flag = false;
        }
        if (!(retailerData.getCnfPassword().length() != 0)) {
            cnfPassword.setError("Field can't be empty");
            flag = false;
        } else if (!(retailerData.getPassword().equals(retailerData.getCnfPassword()))) {
            this.cnfPassword.setText("");
            cnfPassword.setError("Passwords do not match");
            flag = false;
        }
        return flag;
    }

    private boolean checkUsernameWithDatabase(String username) {
        RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
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
