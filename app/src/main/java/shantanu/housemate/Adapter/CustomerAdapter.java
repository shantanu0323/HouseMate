package shantanu.housemate.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import shantanu.housemate.Data.CustomerData;
import shantanu.housemate.R;

/**
 * Created by SHAAN on 17-04-17.
 */
public class CustomerAdapter extends ArrayAdapter<CustomerData> {

    public CustomerAdapter(Context context, int resource, List<CustomerData> customerDatas) {
        super(context, resource, customerDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.customer_item_layout, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        CustomerData customerData = getItem(position);

        TextView tvFirstName = (TextView) listItemView.findViewById(R.id.tvFirstName);
        TextView tvLastName = (TextView) listItemView.findViewById(R.id.tvLastName);
        TextView tvUsername = (TextView) listItemView.findViewById(R.id.tvUsername);
        TextView tvEmail = (TextView) listItemView.findViewById(R.id.tvEmail);
        TextView tvPhoneNo = (TextView) listItemView.findViewById(R.id.tvPhoneNo);
        TextView tvCity = (TextView) listItemView.findViewById(R.id.tvCity);
        TextView tvAddress = (TextView) listItemView.findViewById(R.id.tvAddress);
        TextView tvPincode = (TextView) listItemView.findViewById(R.id.tvPincode);

        tvFirstName.setText(customerData.getFirstName());
        tvLastName.setText(customerData.getLastName());
        tvUsername.setText(customerData.getUsername());
        tvEmail.setText(customerData.getEmail());
        tvAddress.setText(customerData.getAddress());
        tvPhoneNo.setText(customerData.getPhoneNo());
        tvPincode.setText(customerData.getPinCode());
        tvCity.setText(customerData.getCity());

        return listItemView;
    }
}

