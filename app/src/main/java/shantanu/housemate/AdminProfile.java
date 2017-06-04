package shantanu.housemate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminProfile extends AppCompatActivity {

    TextView tvAdminName, tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tvAdminName = (TextView) findViewById(R.id.tvAdminName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        String data[] = getIntent().getStringArrayExtra("data");
        tvAdminName.setText(data[0]);
        tvUsername.setText("@" + data[1]);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button bViewCust = (Button) findViewById(R.id.bViewCust);
        bViewCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewCustomers.class);
                startActivity(intent);
            }
        });

        Button bViewRetail = (Button) findViewById(R.id.bViewRetail);
        bViewRetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewRetailers.class);
                startActivity(intent);
            }
        });

    }
}
