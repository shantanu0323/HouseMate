package shantanu.housemate;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import shantanu.housemate.Data.AdminData;
import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.Data.CustomerGroceryData;
import shantanu.housemate.Data.GroceryData;
import shantanu.housemate.Data.OrderData;
import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.AdminDatabaseHelper;
import shantanu.housemate.DatabaseHelper.CustomerDatabaseHelper;
import shantanu.housemate.DatabaseHelper.CustomerGroceryDatabaseHelper;
import shantanu.housemate.DatabaseHelper.GroceryDatabaseHelper;
import shantanu.housemate.DatabaseHelper.OrderDatabaseHelper;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Assigning the elements to global varibles
    Button LoginBtn, RegisterBtn;
    Bundle bundle;
    private Animation blinkAnimation;
    ImageView ivLogo;
    static boolean flag = true;
    Context context = this;
    public static final String PREFS_FILE = "MyPrefsFile";
    boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setAllowReturnTransitionOverlap(false);
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        getWindow().setExitTransition(fade);
//        fade.setDuration(800);
//        getWindow().setReenterTransition(fade);
        setContentView(R.layout.activity_home);
//        initialize();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Initializing the components
        SharedPreferences sp = getSharedPreferences(PREFS_FILE, 0);
        isFirstTime = sp.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            AdminData adminData = new AdminData();
            adminData.setName("Shantanu Pramanik");
            adminData.setUsername("shantanu0323");
            adminData.setPassword("admin0323");
            AdminDatabaseHelper db = new AdminDatabaseHelper(context);
            try {
                db.open();
                db.putInfo(adminData);
                db.close();
            } catch (SQLException e) {
                Snackbar.make(getCurrentFocus(), e.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
            sp = getSharedPreferences(PREFS_FILE, 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }

        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        RegisterBtn = (Button) findViewById(R.id.RegisterBtn);
        bundle = new Bundle();
        Typeface font1 = null;
        Typeface font2 = null;

        blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim
                .blink);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.startAnimation(blinkAnimation);

        final TypeWriter tv = (TypeWriter) findViewById(R.id.tv);
        tv.setText("");
        tv.setCharacterDelay(150);
        CharSequence[] texts = new CharSequence[4];
        texts[0] = "Welcome Housemates!!! ";
        texts[1] = "Buy groceries with the ease of a button ";
        texts[2] = "Make your daily life simpler ";
        texts[3] = "Relax & Let your phone do the job";
        tv.writeText(texts);

        // Setting up the customFonts
        try {
            font2 = Typeface.createFromAsset(getAssets(), "good_dog.otf");
            font1 = Typeface.createFromAsset(getAssets(), "drift.ttf");
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        // Customizing the layout
        if (font1 != null) {
            LoginBtn.setTypeface(font1);
            RegisterBtn.setTypeface(font1);
            tv.setTypeface(font2);
        }

        // Setting the OnClickListeners
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle bundle = null;
                    bundle = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this)
                            .toBundle();
                    Intent intent = new Intent(HomeActivity.this, LoginPage.class);
                    startActivity(intent, bundle);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(HomeActivity.this, RegistrationPage.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void displayInitialDialog() {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
        final View view = factory.inflate(R.layout.dialog_image_view, null);
        alertadd.setView(view);
        alertadd.setTitle("Hi Guys!!!");
        alertadd.setMessage("Thank You very much for using this app. This is a work by " +
                "\n\nShantanu Pramanik\n\nIf there is any bug that you want to be corrected " +
                "then feel free to contact me. :)\n\nMobile No: 9585427880\nEmail Id: srs" +
                ".shaan@gmail.com\n\nHave Fun!!!");
        alertadd.setPositiveButton("Alright!!!", new DialogInterface.OnClickListener
                () {
            public void onClick(DialogInterface dialog, int sumthin) {
                dialog.dismiss();
            }
        });

        alertadd.show();
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resId = item.getItemId();
        switch (resId) {
            case R.id.action_about:
                displayInitialDialog();
                break;
            case R.id.reinitializeGroceries:
                initializeCustomerGroceries();
                initializeRetailerGroceries();
                break;
            case R.id.reinitializeCustomers:
                initializeCustomers();
                break;
            case R.id.reinitializeRetailers:
                initializeRetailers();
                break;
            case R.id.reinitializeOrders:
                initializeOrders();
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.navCustomerRegistration:
                intent = new Intent(HomeActivity.this, CustomerRegistration.class);
                break;
            case R.id.navRetailerRegistration:
                intent = new Intent(HomeActivity.this, RetailerRegistration.class);
                break;
            case R.id.navLogin:
                intent = new Intent(HomeActivity.this, LoginPage.class);
                break;
        }
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeOrders() {
        OrderDatabaseHelper db = new OrderDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void initializeRetailers() {
        RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            db.putData(new RetailerData("MORE Supermarket", "morestore@rediffmail.com", "7thEast " +
                    "Main Road, Gandhinagar", "Vellore", "632006", "9244970160", 10, "ms", "ms",
                    "ms"));
            db.putData(new RetailerData("Harish Food Zone", "hfz@gmail.com", "Anna Sillai, " +
                    "Kosapet", "Vellore", "632001", "9360077143", 8, "hfz", "hfz", "hfz"));
            db.putData(new RetailerData("Big Basket", "bigbasket@gmail.com", "Chander Vihar, " +
                    "Mandawali", "Delhi", "110092", "18601231000", 9, "bb", "bb", "bb"));
            db.putData(new RetailerData("Aditya Birla RL", "abrl@gmail.com", "101, MIG Flats",
                    "Delhi", "110075", "9718595202", 7, "abrl", "abrl", "abrl"));
            db.putData(new RetailerData("Big Bazaar", "bigbazaar@gmail.com", "Baghuihati",
                    "Kolkata", "710009", "1800208457", 10, "fbb", "fbb", "fbb"));
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeCustomers() {
        CustomerDatabaseHelper db = new CustomerDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            db.putData(new CustomerData("Shantanu", "Pramanik", "srs.shaan@gmail.com", "VIT " +
                    "University", "Vellore", "9585427880", "632014", 23, "sp", "sp", "sp"));
            db.putData(new CustomerData("Ajay", "Bisht", "ajaybisht163@gmail.com", "VIT " +
                    "University", "Vellore", "9003725500", "632014", 40, "ab", "ab", "ab"));
            db.putData(new CustomerData("Nikhil", "Kaushik", "nikhilkaushik@gmail.com",
                    "104 Hector Colony", "Delhi", "9003766485", "810009", 71, "nk", "nk", "nk"));
            db.putData(new CustomerData("Debkalpa", "Banergee", "dkb.fcl@gmail.com", "Xaviers " +
                    "College", "Kolkata", "9644475588", "714007", 14, "dkb", "dkb", "dkb"));
            db.putData(new CustomerData("John", "Carter", "carterjohn@gmail.com", "Imperial " +
                    "College of Engineering", "Delhi", "8875554241", "810047", 57, "jc", "jc", "jc"));
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeCustomerGroceries() {
        CustomerGroceryDatabaseHelper db = new CustomerGroceryDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            db.putData(new CustomerGroceryData("Boiled Rice", "20/kg"));
            db.putData(new CustomerGroceryData("Idli Rice", " 40 / kg"));
            db.putData(new CustomerGroceryData("Raw Rice", " 30 / kg"));
            db.putData(new CustomerGroceryData("Basmati Rice", "60 / kg"));
            db.putData(new CustomerGroceryData("Toor Dal", "100 / kg"));
            db.putData(new CustomerGroceryData("Channa Dal", " 80 / kg"));
            db.putData(new CustomerGroceryData("Kaala Channa", " 90 / kg"));
            db.putData(new CustomerGroceryData("Mung Dal", " 95 / kg"));
            db.putData(new CustomerGroceryData("Rajma", " 130 / kg"));
            db.putData(new CustomerGroceryData("Urad", " 120 / kg"));
            db.putData(new CustomerGroceryData("Masoor Dal", "130/kg"));
            db.putData(new CustomerGroceryData("Peas", " 90 / kg"));
            db.putData(new CustomerGroceryData("Wheat", " 50 / kg"));
            db.putData(new CustomerGroceryData("Corn", " 60 / kg"));
            db.putData(new CustomerGroceryData("Soya Beans", "90 / kg"));
            db.putData(new CustomerGroceryData("Vegetable Oil", "100 / L"));
            db.putData(new CustomerGroceryData("Gingely oil", "80 / L"));
            db.putData(new CustomerGroceryData("Olive Oil", "80 / L"));
            db.putData(new CustomerGroceryData("Ghee", "60 / kg"));
            db.putData(new CustomerGroceryData("Butter", "30 / 200gm"));
            db.putData(new CustomerGroceryData("CoconutOil", "100 / L"));
            db.putData(new CustomerGroceryData("Castor Oil", "120 / L"));
            db.putData(new CustomerGroceryData("Sunflower Oil", "90 / L"));
            db.putData(new CustomerGroceryData("Cheese", "50 / 200gm"));
            db.putData(new CustomerGroceryData("Mustard Oil", "85 / L"));
            db.putData(new CustomerGroceryData("Sauces Soy", "50"));
            db.putData(new CustomerGroceryData("Sauces Green Chilly", "50"));
            db.putData(new CustomerGroceryData("Sauces Tomato", "50"));
            db.putData(new CustomerGroceryData("Vinegar", "45"));
            db.putData(new CustomerGroceryData("Mayonnaise", "250 / kg"));
            db.putData(new CustomerGroceryData("Jam", "45"));
            db.putData(new CustomerGroceryData("Butter", "30 / 200gm"));
            db.putData(new CustomerGroceryData("Table Salt", "50 / kg"));
            db.putData(new CustomerGroceryData("Crystal Salt", "55 / kg"));
            db.putData(new CustomerGroceryData("Sugar", "100 / kg"));
            db.putData(new CustomerGroceryData("Pepper", "120 / kg"));
            db.putData(new CustomerGroceryData("Honey", "250 / kg"));
            db.putData(new CustomerGroceryData("Essence Vanilla", "10 / 50ml"));
            db.putData(new CustomerGroceryData("Essence Rose", "10 / 50ml"));
            db.putData(new CustomerGroceryData("Essence Chocolate", "10 / 50ml"));
            db.putData(new CustomerGroceryData("Black Pepper", "45 / 100gm"));
            db.putData(new CustomerGroceryData("Cinnamon", "50 / 100gm"));
            db.putData(new CustomerGroceryData("Cardomom", "35 / 50gm"));
            db.putData(new CustomerGroceryData("Fenugreek", "30 / 50gm"));
            db.putData(new CustomerGroceryData("Cumin Seed", "30 / 50gm"));
            db.putData(new CustomerGroceryData("Coriander Seed", "28 / 50gm"));
            db.putData(new CustomerGroceryData("Turmeric", "35/100gm"));
            db.putData(new CustomerGroceryData("Pickle", "65"));
            db.putData(new CustomerGroceryData("Tamarind", "65 / 100gm"));
            db.putData(new CustomerGroceryData("Garlic", "45/100gm"));
            db.putData(new CustomerGroceryData("Ginger", "45/100gm"));
            db.putData(new CustomerGroceryData("Sesame Seed", "40/100gm"));
            db.putData(new CustomerGroceryData("Garam Masala", "40/100gm"));
            db.putData(new CustomerGroceryData("Ajwain", "30 / 100gm"));
            db.putData(new CustomerGroceryData("Red Chilli", "40/100gm"));
            db.putData(new CustomerGroceryData("Cornflakes", "45"));
            db.putData(new CustomerGroceryData("Badam", "300/kg"));
            db.putData(new CustomerGroceryData("Pista", "350/kg"));
            db.putData(new CustomerGroceryData("Cashew", "400/kg"));
            db.putData(new CustomerGroceryData("Rose Water", "250/L"));
            db.putData(new CustomerGroceryData("Dates", "85/100gm"));
            db.putData(new CustomerGroceryData("Saffron", "100/kg"));
            db.putData(new CustomerGroceryData("Peanut", "225/kg"));
            db.putData(new CustomerGroceryData("Turmeric powder", "100/kg"));
            db.putData(new CustomerGroceryData("Noodles", "55"));
            db.putData(new CustomerGroceryData("Macaroni", "65"));
            db.putData(new CustomerGroceryData("Baking Powder", "65/100gm"));
            db.putData(new CustomerGroceryData("Citric Acid", "50/100ml"));
            db.putData(new CustomerGroceryData("Cocoa Powder", "35/100gm"));
            db.putData(new CustomerGroceryData("Baking Soda", "65/100gm"));
            db.putData(new CustomerGroceryData("Coffee Powder", "65/200gm"));
            db.putData(new CustomerGroceryData("Tea Powder", "35/500gm"));
            db.putData(new CustomerGroceryData("Horlicks", "85"));
            db.putData(new CustomerGroceryData("Complan", "95"));
            db.putData(new CustomerGroceryData("Biscuits", "30"));
            db.putData(new CustomerGroceryData("Chocolates", "40"));
            db.putData(new CustomerGroceryData("Garam Masala Powder", "40/100gm"));
            db.putData(new CustomerGroceryData("Cumin Powder", "40/100gm"));
            db.putData(new CustomerGroceryData("Pepper Powder", "35/100gm"));
            db.putData(new CustomerGroceryData("Chilli Powder", "35/100gm"));
            db.putData(new CustomerGroceryData("Sambar Powder", "30/100gm"));
            db.putData(new CustomerGroceryData("Cardmom Powder", "43/100gm"));
            db.putData(new CustomerGroceryData("Maida", "150/kg"));
            db.putData(new CustomerGroceryData("Besan", "120/kg"));
            db.putData(new CustomerGroceryData("Chat Masala", "65/100gm"));
            db.putData(new CustomerGroceryData("Chicken Masala", "65/kg"));
            db.putData(new CustomerGroceryData("Tooth Paste", "50"));
            db.putData(new CustomerGroceryData("Tooth Brush", "50"));
            db.putData(new CustomerGroceryData("Oral Rinses", "65"));
            db.putData(new CustomerGroceryData("Shampoo", "80"));
            db.putData(new CustomerGroceryData("Hair Conditioner", "75"));
            db.putData(new CustomerGroceryData("Hair Oil", "60"));
            db.putData(new CustomerGroceryData("Hand Wash", "45"));
            db.putData(new CustomerGroceryData("Soap", "30"));
            db.putData(new CustomerGroceryData("Face Powder", "50"));
            db.putData(new CustomerGroceryData("Razer Blades", "40"));
            db.putData(new CustomerGroceryData("Shaving Cream", "90"));
            db.putData(new CustomerGroceryData("Baby Products Diapers", "69"));
            db.putData(new CustomerGroceryData("Sanitary Pads", "69"));
            db.putData(new CustomerGroceryData("Body Lotion", "70"));
            db.putData(new CustomerGroceryData("Naphathalene Balls", "40"));
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeRetailerGroceries() {
        GroceryDatabaseHelper db = new GroceryDatabaseHelper(context);
        try {
            db.open();
            db.clearDatabase();
            db.putData(new GroceryData("Boiled Rice", "20/kg"));
            db.putData(new GroceryData("Idli Rice", " 40 / kg"));
            db.putData(new GroceryData("Raw Rice", " 30 / kg"));
            db.putData(new GroceryData("Basmati Rice", "60 / kg"));
            db.putData(new GroceryData("Toor Dal", "100 / kg"));
            db.putData(new GroceryData("Channa Dal", " 80 / kg"));
            db.putData(new GroceryData("Kaala Channa", " 90 / kg"));
            db.putData(new GroceryData("Mung Dal", " 95 / kg"));
            db.putData(new GroceryData("Rajma", " 130 / kg"));
            db.putData(new GroceryData("Urad", " 120 / kg"));
            db.putData(new GroceryData("Masoor Dal", "130/kg"));
            db.putData(new GroceryData("Peas", " 90 / kg"));
            db.putData(new GroceryData("Wheat", " 50 / kg"));
            db.putData(new GroceryData("Corn", " 60 / kg"));
            db.putData(new GroceryData("Soya Beans", "90 / kg"));
            db.putData(new GroceryData("Vegetable Oil", "100 / L"));
            db.putData(new GroceryData("Gingely oil", "80 / L"));
            db.putData(new GroceryData("Olive Oil", "80 / L"));
            db.putData(new GroceryData("Ghee", "60 / kg"));
            db.putData(new GroceryData("Butter", "30 / 200gm"));
            db.putData(new GroceryData("CoconutOil", "100 / L"));
            db.putData(new GroceryData("Castor Oil", "120 / L"));
            db.putData(new GroceryData("Sunflower Oil", "90 / L"));
            db.putData(new GroceryData("Cheese", "50 / 200gm"));
            db.putData(new GroceryData("Mustard Oil", "85 / L"));
            db.putData(new GroceryData("Sauces Soy", "50"));
            db.putData(new GroceryData("Sauces Green Chilly", "50"));
            db.putData(new GroceryData("Sauces Tomato", "50"));
            db.putData(new GroceryData("Vinegar", "45"));
            db.putData(new GroceryData("Mayonnaise", "250 / kg"));
            db.putData(new GroceryData("Jam", "45"));
            db.putData(new GroceryData("Butter", "30 / 200gm"));
            db.putData(new GroceryData("Table Salt", "50 / kg"));
            db.putData(new GroceryData("Crystal Salt", "55 / kg"));
            db.putData(new GroceryData("Sugar", "100 / kg"));
            db.putData(new GroceryData("Pepper", "120 / kg"));
            db.putData(new GroceryData("Honey", "250 / kg"));
            db.putData(new GroceryData("Essence Vanilla", "10 / 50ml"));
            db.putData(new GroceryData("Essence Rose", "10 / 50ml"));
            db.putData(new GroceryData("Essence Chocolate", "10 / 50ml"));
            db.putData(new GroceryData("Black Pepper", "45 / 100gm"));
            db.putData(new GroceryData("Cinnamon", "50 / 100gm"));
            db.putData(new GroceryData("Cardomom", "35 / 50gm"));
            db.putData(new GroceryData("Fenugreek", "30 / 50gm"));
            db.putData(new GroceryData("Cumin Seed", "30 / 50gm"));
            db.putData(new GroceryData("Coriander Seed", "28 / 50gm"));
            db.putData(new GroceryData("Turmeric", "35/100gm"));
            db.putData(new GroceryData("Pickle", "65"));
            db.putData(new GroceryData("Tamarind", "65 / 100gm"));
            db.putData(new GroceryData("Garlic", "45/100gm"));
            db.putData(new GroceryData("Ginger", "45/100gm"));
            db.putData(new GroceryData("Sesame Seed", "40/100gm"));
            db.putData(new GroceryData("Garam Masala", "40/100gm"));
            db.putData(new GroceryData("Ajwain", "30 / 100gm"));
            db.putData(new GroceryData("Red Chilli", "40/100gm"));
            db.putData(new GroceryData("Cornflakes", "45"));
            db.putData(new GroceryData("Badam", "300/kg"));
            db.putData(new GroceryData("Pista", "350/kg"));
            db.putData(new GroceryData("Cashew", "400/kg"));
            db.putData(new GroceryData("Rose Water", "250/L"));
            db.putData(new GroceryData("Dates", "85/100gm"));
            db.putData(new GroceryData("Saffron", "100/kg"));
            db.putData(new GroceryData("Peanut", "225/kg"));
            db.putData(new GroceryData("Turmeric powder", "100/kg"));
            db.putData(new GroceryData("Noodles", "55"));
            db.putData(new GroceryData("Macaroni", "65"));
            db.putData(new GroceryData("Baking Powder", "65/100gm"));
            db.putData(new GroceryData("Citric Acid", "50/100ml"));
            db.putData(new GroceryData("Cocoa Powder", "35/100gm"));
            db.putData(new GroceryData("Baking Soda", "65/100gm"));
            db.putData(new GroceryData("Coffee Powder", "65/200gm"));
            db.putData(new GroceryData("Tea Powder", "35/500gm"));
            db.putData(new GroceryData("Horlicks", "85"));
            db.putData(new GroceryData("Complan", "95"));
            db.putData(new GroceryData("Biscuits", "30"));
            db.putData(new GroceryData("Chocolates", "40"));
            db.putData(new GroceryData("Garam Masala Powder", "40/100gm"));
            db.putData(new GroceryData("Cumin Powder", "40/100gm"));
            db.putData(new GroceryData("Pepper Powder", "35/100gm"));
            db.putData(new GroceryData("Chilli Powder", "35/100gm"));
            db.putData(new GroceryData("Sambar Powder", "30/100gm"));
            db.putData(new GroceryData("Cardmom Powder", "43/100gm"));
            db.putData(new GroceryData("Maida", "150/kg"));
            db.putData(new GroceryData("Besan", "120/kg"));
            db.putData(new GroceryData("Chat Masala", "65/100gm"));
            db.putData(new GroceryData("Chicken Masala", "65/kg"));
            db.putData(new GroceryData("Tooth Paste", "50"));
            db.putData(new GroceryData("Tooth Brush", "50"));
            db.putData(new GroceryData("Oral Rinses", "65"));
            db.putData(new GroceryData("Shampoo", "80"));
            db.putData(new GroceryData("Hair Conditioner", "75"));
            db.putData(new GroceryData("Hair Oil", "60"));
            db.putData(new GroceryData("Hand Wash", "45"));
            db.putData(new GroceryData("Soap", "30"));
            db.putData(new GroceryData("Face Powder", "50"));
            db.putData(new GroceryData("Razer Blades", "40"));
            db.putData(new GroceryData("Shaving Cream", "90"));
            db.putData(new GroceryData("Baby Products Diapers", "69"));
            db.putData(new GroceryData("Sanitary Pads", "69"));
            db.putData(new GroceryData("Body Lotion", "70"));
            db.putData(new GroceryData("Naphathalene Balls", "40"));
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
