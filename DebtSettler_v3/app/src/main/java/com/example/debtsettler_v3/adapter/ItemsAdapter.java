package com.example.debtsettler_v3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtsettler_v3.R;
import com.example.debtsettler_v3.model.Items;
import com.example.debtsettler_v3.model.Members;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<com.example.debtsettler_v3.adapter.ItemsAdapter.ItemsViewHolder>{

    Context context;
    List<Items> itemList;

    public ItemsAdapter(Context context, List<Items> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_row_item, parent, false);
        return new ItemsAdapter.ItemsViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemsViewHolder holder, int position) {

        holder.itemsNameView.setText(itemList.get(position).getName());
        holder.itemsOpisView.setText(itemList.get(position).getOpis());

        holder.checkBox.setChecked(itemList.get(position).isSelected());
        holder.checkBox.setTag(itemList.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String data = "";
                Items list1 = (Items)holder.checkBox.getTag();

                list1.setSelected(holder.checkBox.isChecked());

                itemList.get(position).setSelected((holder.checkBox.isChecked()));

                if (holder.checkBox.isChecked()) {
                    holder.itemRow.setBackgroundColor(context.getResources().getColor(R.color.OpacityBlue));
                }
                if (!holder.checkBox.isChecked()) {
                    holder.itemRow.setBackgroundColor(context.getResources().getColor(R.color.white));
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder{

        View itemRow;
        TextView itemsNameView;
        TextView itemsOpisView;
        CheckBox checkBox;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemRow = itemView.findViewById(R.id.itemRowView);
            itemsNameView = itemView.findViewById(R.id.itemName);
            itemsOpisView = itemView.findViewById(R.id.itemOpis);
            checkBox = itemView.findViewById(R.id.itemCheckBox);
        }
    }


    public ArrayList<String> getAllSelected() {
        ArrayList<String> data = new ArrayList<>();
        data.clear();
        for (int i=0; i<itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                data.add(itemList.get(i).getId());
            }
        }
        return data;
    }

    public void updateView() {

    }
}
