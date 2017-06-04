package shantanu.housemate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.Data.CustomerGroceryData;
import shantanu.housemate.Data.OrderData;
import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;
import shantanu.housemate.DatabaseHelper.OrderDatabaseHelper;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class RetailerProfile extends AppCompatActivity {
    //Declaring the layout items
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPhoneNo;
    private TextView tvAddress;
    private TextView tvCity;
    private TextView tvPincode;
    private TextView tvRos;
    private FloatingActionButton bViewGroceries;
    private ImageButton bRetailerMap;
    private String username;
    private String title;
    private boolean buttonEnabled;
    private boolean isCustomer;
    private FloatingActionButton bMakePayment;
    private String customerUsername;
    private Context context;
    private String shopName;
    private TextView tvName;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_retailer_profile);
        context = getBaseContext();
        username = getIntent().getStringExtra("retailerUsername");
        customerUsername = getIntent().getStringExtra("customerUsername");
        isCustomer = getIntent().getBooleanExtra("isCustomer", false);
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvPincode = (TextView) findViewById(R.id.tvPincode);
        tvRos = (TextView) findViewById(R.id.tvRos);
        tvName = (TextView) findViewById(R.id.tvName);
        bViewGroceries = (FloatingActionButton) findViewById(R.id.bViewGroceries);
        bRetailerMap = (ImageButton) findViewById(R.id.bRetailerMap);
        bMakePayment = (FloatingActionButton) findViewById(R.id.bMakePayment);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        if (isAdmin) {
            bMakePayment.setVisibility(View.GONE);
        }
        if (!isCustomer) {
            checkForOrders();
            Log.e("ORDER","received");
        }

        RetailerDatabaseHelper db = new RetailerDatabaseHelper(getBaseContext());
        RetailerData retailerData = new RetailerData();
        try {
            db.open();
            retailerData = db.getData(username);
            db.close();
        } catch (SQLException e) {
            Snackbar.make(getCurrentFocus(), e.getMessage(),
                    Snackbar.LENGTH_LONG).show();
        }
        shopName = retailerData.getShopName();
        tvUsername.setText(retailerData.getUsername());
        tvEmail.setText(retailerData.getEmail());
        tvPhoneNo.setText(retailerData.getPhoneNo());
        tvAddress.setText(retailerData.getAddress());
        tvCity.setText(retailerData.getCity());
        tvPincode.setText(retailerData.getPinCode());
        tvRos.setText("" + retailerData.getRadiusOfService());
        title = retailerData.getShopName();
        tvName.setText(title);
//        getSupportActionBar().setTitle(title);

        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        buttonEnabled = getIntent().getBooleanExtra("buttonEnabled", true);
//        Toast.makeText(getBaseContext(), "isCustomer: " + isCustomer, Toast.LENGTH_LONG).show();
        if (isCustomer) {
            final RetailerData finalRetailerData = retailerData;
            bRetailerMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Clicked", Toast.LENGTH_LONG).show();
                    Uri intentUri = Uri.parse("geo:0,0?q=" + finalRetailerData
                            .getShopName() + " " + finalRetailerData.getAddress() + " "
                            + finalRetailerData.getCity());
                    Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
            });
            bMakePayment.setVisibility(View.VISIBLE);
            bMakePayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDatabaseHelper db = new OrderDatabaseHelper(context);
                    OrderData orderData = new OrderData();
                    try {
                        db.open();
                        orderData = db.getOrderDataOf(customerUsername);
                        db.deleteData(customerUsername);
                        orderData.setRetailerUsername(username);
                        db.putData(orderData);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getBaseContext(), Payments.class);
                    intent.putExtra("customerUsername", customerUsername);
                    startActivity(intent);
                }
            });
        } else {
            bRetailerMap.setVisibility(View.GONE);
            bMakePayment.setVisibility(View.GONE);
        }
        if (buttonEnabled) {
//            Toast.makeText(getBaseContext(), "buttonEnabled", Toast.LENGTH_LONG).show();
            bViewGroceries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ViewRetailerGroceries.class);
                    startActivity(intent);
                }
            });
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "FAB is clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getBaseContext(), RetailerRegistration.class);
                    intent.putExtra("isEditing", true);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            });

        } else {
            fabEdit.setVisibility(View.GONE);
            bViewGroceries.setVisibility(View.GONE);
        }
    }

    private void checkForOrders() {
        OrderDatabaseHelper db = new OrderDatabaseHelper(context);
        OrderData orderData = new OrderData();
        try {
            db.open();
            orderData = db.getOrderDataOfRetailer(username);
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!orderData.getDod().equals("empty")) {
            Toast.makeText(getBaseContext(), "You have an order", Toast.LENGTH_LONG).show();
            customerUsername = orderData.getCustomerUsername();
            CustomerDatabaseHelper cdb = new CustomerDatabaseHelper(context);
            CustomerData customerData = new CustomerData();
            try {
                cdb.open();
                customerData = cdb.getData(customerUsername);
                cdb.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            addNotification(customerData.getFirstName() + " " + customerData.getLastName(),
                    orderData.getBill());
        } else {
            Toast.makeText(getBaseContext(), "You DONT have any order", Toast.LENGTH_LONG).show();
        }
    }

    private void addNotification(String customerName, String bill) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_grocery)
                        .setContentTitle("Welcome " + shopName + "!!!")
                        .setContentText("You have an Order of Rs. " + bill + " from\n" +
                                customerName)
                        .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, PlaceOrder.class);
        notificationIntent.putExtra("isRetailer", true);
        notificationIntent.putExtra("customerUsername", customerUsername);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCustomer) {
            finish();
        }
    }
}
