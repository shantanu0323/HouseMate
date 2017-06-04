package shantanu.housemate.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import shantanu.housemate.Data.GroceryData;
import shantanu.housemate.R;

/**
 * Created by SHAAN on 18-04-17.
 */
public class RetailerGroceriesAdapter extends ArrayAdapter<GroceryData> {

    public RetailerGroceriesAdapter(Context context, int resource, List<GroceryData> groceryDatas) {
        super(context, resource, groceryDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.retailer_groceries_item_layout, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        GroceryData groceryData = getItem(position);

        TextView tvItemName = (TextView) listItemView.findViewById(R.id.tvItemName);
        TextView tvItemPrice = (TextView) listItemView.findViewById(R.id.tvItemPrice);

        tvItemName.setText(groceryData.getItemName());
        tvItemPrice.setText("" + groceryData.getItemPrice());
        Log.e("POSITION", "" + position);
        Log.e("NAME", groceryData.getItemName());
        Log.e("PRICE", "" + groceryData.getItemPrice());

        return listItemView;
    }
}

