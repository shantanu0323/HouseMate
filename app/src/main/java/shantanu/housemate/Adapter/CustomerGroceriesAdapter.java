package shantanu.housemate.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import shantanu.housemate.Data.CustomerGroceryData;
import shantanu.housemate.R;

/**
 * Created by SHAAN on 18-04-17.
 */
public class CustomerGroceriesAdapter extends ArrayAdapter<CustomerGroceryData> {

    public CustomerGroceriesAdapter(Context context, int resource, List<CustomerGroceryData> groceryDatas) {
        super(context, resource, groceryDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.customer_groceries_item_layout, parent, false);
        }

        final CustomerGroceryData groceryData = getItem(position);

        TextView tvItemName = (TextView) listItemView.findViewById(R.id.tvItemName);
        TextView tvItemPrice = (TextView) listItemView.findViewById(R.id.tvItemPrice);
        final CheckBox checkBox = (CheckBox) listItemView.findViewById(R.id.checkBox);

        tvItemName.setText(groceryData.getItemName());
        tvItemPrice.setText("" + groceryData.getItemPrice());
        checkBox.setChecked(Boolean.parseBoolean(groceryData.getSelected()));

        return listItemView;
    }
}

