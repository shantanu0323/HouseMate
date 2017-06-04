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

import java.util.ArrayList;
import java.util.List;

import shantanu.housemate.Data.RetailerData;
import shantanu.housemate.R;
import shantanu.housemate.ViewRetailers;

/**
 * Created by SHAAN on 18-04-17.
 */
public class RetailerAdapter extends ArrayAdapter<RetailerData> {

    public RetailerAdapter(Context context, int resource, List<RetailerData> retailerDatas) {
        super(context, resource, retailerDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.retailer_item_layout, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        RetailerData retailerData = getItem(position);

        TextView tvShopName = (TextView) listItemView.findViewById(R.id.tvShopName);
        TextView tvUsername = (TextView) listItemView.findViewById(R.id.tvUsername);
        TextView tvEmail = (TextView) listItemView.findViewById(R.id.tvEmail);
        TextView tvPhoneNo = (TextView) listItemView.findViewById(R.id.tvPhoneNo);
        TextView tvCity = (TextView) listItemView.findViewById(R.id.tvCity);
        TextView tvAddress = (TextView) listItemView.findViewById(R.id.tvAddress);
        TextView tvPincode = (TextView) listItemView.findViewById(R.id.tvPincode);

        tvShopName.setText(retailerData.getShopName());
        tvUsername.setText(retailerData.getUsername());
        tvEmail.setText(retailerData.getEmail());
        tvAddress.setText(retailerData.getAddress());
        tvPhoneNo.setText(retailerData.getPhoneNo());
        tvPincode.setText(retailerData.getPinCode());
        tvCity.setText(retailerData.getCity());

        return listItemView;
    }
}

