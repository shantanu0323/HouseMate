package shantanu.housemate;



import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.DatabaseHelper.RetailerDatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RetailerLogin extends Fragment {

    //  Assigning the elements to global variables
    public Button bSubmit, bForgot;
    public EditText etUsername, etPassword;
    Context context;

    public RetailerLogin() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity(); //use the instance variable
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_retailer_login, container, false);
        // Initializing the variables
        bSubmit = (Button) view.findViewById(R.id.bSubmit);
        bForgot = (Button) view.findViewById(R.id.bForgot);
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        // Setting up the custom font
        Typeface font2 = null, font3 = null;
        try {
            font2 = Typeface.createFromAsset(getActivity().getAssets(), "good_dog.otf");
            font3 = Typeface.createFromAsset(getActivity().getAssets(), "remachine.ttf");
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }

        // Customizing the layout
        if (font3 != null) {
            bSubmit.setTypeface(font3);
            bForgot.setTypeface(font2);
        }

        // Setting the onClickListeners
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString();
                ArrayList<RetailerData> retailerDatas = new ArrayList<RetailerData>();
                RetailerDatabaseHelper db = new RetailerDatabaseHelper(context);
                try {
                    db.open();
                    retailerDatas = db.getAllData();
                    db.close();
                } catch (SQLException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                RetailerData currentRetailer;
                int i;
                for (i = 0; i < retailerDatas.size(); i++) {
                    currentRetailer = retailerDatas.get(i);
                    if (currentRetailer.getUsername().equals(username)) {
                        if (currentRetailer.getPassword().equals(password)) {
                            Snackbar.make(getActivity().getCurrentFocus(), "Login Successfull!!!",
                                    Snackbar
                                            .LENGTH_LONG).show();

                            Intent intent = new Intent(getActivity(), RetailerProfile.class);
                            intent.putExtra("retailerUsername", username);
                            startActivity(intent);
                            break;
                        } else {
                            Snackbar.make(getActivity().getCurrentFocus(), "Incorrect Password!!!",
                                    Snackbar.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                if (i == retailerDatas.size()){
                    Snackbar.make(getActivity().getCurrentFocus(), "Invalid Username!!!", Snackbar
                            .LENGTH_LONG).show();
                }
            }
        });
        bForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgotPassword.class);
                intent.putExtra("choice", "retailer");
                startActivity(intent);
            }
        });

        return view;
    }

}
