package shantanu.housemate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import shantanu.housemate.Data.GenerateListData;
import shantanu.housemate.R;

/**
 * Created by SHAAN on 18-04-17.
 */
public class OrderedListAdapter extends ArrayAdapter<GenerateListData> {

    public OrderedListAdapter(Context context, int resource, List<GenerateListData> groceryDatas) {
        super(context, resource, groceryDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ordered_list_item_layout, parent, false);
        }

        final GenerateListData groceryData = getItem(position);

        TextView tvItemName = (TextView) listItemView.findViewById(R.id.tvItemName);
        TextView tvItemPrice = (TextView) listItemView.findViewById(R.id.tvItemPrice);
        TextView tvQuantity = (TextView) listItemView.findViewById(R.id.tvQuantity);
//        final CheckBox checkBox = (CheckBox) listItemView.findViewById(R.id.checkBox);

        tvItemName.setText(groceryData.getItemName());
        tvItemPrice.setText("" + groceryData.getItemPrice());
        tvQuantity.setText(groceryData.getQuantity());
//        checkBox.setChecked(Boolean.parseBoolean(groceryData.getSelected()));

        return listItemView;
    }
}

