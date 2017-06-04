package shantanu.housemate;

import android.content.Context;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import shantanu.housemate.Adapter.GenerateListAdapter;
import shantanu.housemate.Adapter.PlacedOrderAdapter;
import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.Data.GenerateListData;
import shantanu.housemate.Data.OrderData;
import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;
import shantanu.housemate.DatabaseHelper.GenerateListDatabaseHelper;
import shantanu.housemate.DatabaseHelper.OrderDatabaseHelper;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class PlaceOrder extends AppCompatActivity {

    private PlacedOrderAdapter groceriesAdapter;
    private String customerUsername;
    private Context context;
    private TextView tvCustomerName, tvRetailerName, tvDeliveryAddress, tvPaymentType, tvDod,
            tvBill;
    private Button bPlaceOrder;
    private ImageButton bDod;
    private DatePicker datePicker;
    private ListView orderedList;
    private boolean isRetailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        context = getBaseContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        isRetailer = getIntent().getBooleanExtra("isRetailer", false);
        if (isRetailer) {
            bPlaceOrder.setVisibility(View.GONE);
            bDod.setVisibility(View.GONE);
        }
        customerUsername = getIntent().getStringExtra("customerUsername");
        Toast.makeText(getBaseContext(), customerUsername,
                Toast.LENGTH_SHORT).show();
        OrderDatabaseHelper db = new OrderDatabaseHelper(context);
        OrderData orderData = new OrderData();
        try {
            db.open();
            orderData = db.getOrderDataOf(customerUsername);
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CustomerDatabaseHelper cdb = new CustomerDatabaseHelper(context);
        CustomerData customerData = new CustomerData();
        try {
            cdb.open();
            customerData = cdb.getData(customerUsername);
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        RetailerDatabaseHelper rdb = new RetailerDatabaseHelper(context);
        RetailerData retailerData = new RetailerData();
        try {
            rdb.open();
            retailerData = rdb.getData(orderData.getRetailerUsername());
            rdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tvCustomerName.setText(customerData.getFirstName() + " " + customerData.getLastName());
        tvRetailerName.setText(retailerData.getShopName());
        tvPaymentType.setText(orderData.getPaymentType());
        tvDeliveryAddress.setText(customerData.getAddress() + ", " + customerData.getCity() + "-"
                + customerData.getPinCode());
        tvBill.setText(orderData.getBill());
        tvDod.setText(orderData.getDod());


        ArrayList<GenerateListData> groceriesDatas = new ArrayList<GenerateListData>();

        // Retrieving the data from the GenerateListDatabase
        final GenerateListDatabaseHelper fdb = new GenerateListDatabaseHelper(context);
        try {
            fdb.open();
            groceriesDatas = fdb.getAllData();
            fdb.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        groceriesAdapter = new PlacedOrderAdapter(this, 0, groceriesDatas);
        orderedList.setAdapter(groceriesAdapter);

        bDod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        final String formatedDate = sdf.format(calendar.getTime());
        Date currentDate = new Date();
        try {
            currentDate = sdf.parse(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date finalCurrentDate = currentDate;
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear() - 1900;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                String formatedDate = sdf.format(new Date(year, month, day));
                datePicker.setVisibility(View.GONE);
                Date date = new Date();
                try {
                    date = sdf.parse(formatedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long difference = (date.getTime() - finalCurrentDate.getTime()) / (1000 * 60 * 60 *
                        24);
                if (difference >= 3) {
                    Toast.makeText(getBaseContext(), "Will be delivered within " + difference +
                            " days", Toast.LENGTH_SHORT).show();
                    tvDod.setText(formatedDate);
                    datePicker.setVisibility(View.GONE);
                    OrderDatabaseHelper db = new OrderDatabaseHelper(context);
                    OrderData orderData = new OrderData();
                    try {
                        db.open();
                        orderData = db.getOrderDataOf(customerUsername);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    orderData.setDod(formatedDate);
                    try {
                        db.open();
                        db.deleteData(orderData.getCustomerUsername());
                        db.editData(orderData);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    datePicker.setVisibility(View.VISIBLE);
                    Toast.makeText(getBaseContext(), "Cannot be delivered less than 3 days",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        bPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDod.getText().toString().equals("Date of Delivery") || tvDod.getText()
                        .toString().equals("empty")) {
                    tvDod.setError("Please provide the Delivery Date that you prefer");
                } else {
                    Toast.makeText(getBaseContext(), "You have successfully placed the " +
                            "order\nThank You", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    private void init() {
        tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
        tvRetailerName = (TextView) findViewById(R.id.tvRetailerName);
        tvDeliveryAddress = (TextView) findViewById(R.id.tvDeliveryAddress);
        tvPaymentType = (TextView) findViewById(R.id.tvPaymentType);
        tvDod = (TextView) findViewById(R.id.tvDod);
        tvBill = (TextView) findViewById(R.id.tvBill);

        bPlaceOrder = (Button) findViewById(R.id.bPlaceOrder);
        bDod = (ImageButton) findViewById(R.id.bDod);

        datePicker = (DatePicker) findViewById(R.id.datePicker);

        orderedList = (ListView) findViewById(R.id.orderedList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
