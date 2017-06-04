package shantanu.housemate;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import shantanu.housemate.Data.OrderData;
import shantanu.housemate.DatabaseHelper.OrderDatabaseHelper;

public class Payments extends AppCompatActivity {

    private Button bPayPal;
    private Button bCOD;
    private String customerUsername;
    private TextView tvBill;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        context = getBaseContext();

        customerUsername = getIntent().getStringExtra("customerUsername");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make Payment");

        OrderDatabaseHelper db = new OrderDatabaseHelper(context);
        OrderData orderData = new OrderData();
        try {
            db.open();
            orderData = db.getOrderDataOf(customerUsername);
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvBill = (TextView) findViewById(R.id.tvBill);
        tvBill.setText(orderData.getBill());

        bPayPal = (Button) findViewById(R.id.bPayPal);
        bCOD = (Button) findViewById(R.id.bCOD);

        final OrderData finalOrderData = orderData;
        bPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalOrderData.setPaymentType("PayPal");
                OrderDatabaseHelper db = new OrderDatabaseHelper(context);
                try {
                    db.open();
                    db.deleteData(customerUsername);
                    db.editData(finalOrderData);
                    db.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), PayPalReg.class);
                startActivity(intent);
            }
        });

        bCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalOrderData.setPaymentType("Cash on Delivery");
                OrderDatabaseHelper db = new OrderDatabaseHelper(context);
                try {
                    db.open();
                    db.deleteData(customerUsername);
                    db.editData(finalOrderData);
                    db.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), PlaceOrder.class);
                intent.putExtra("customerUsername", customerUsername);
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
