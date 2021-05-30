package com.example.debtsettler_v3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtsettler_v3.R;
import com.example.debtsettler_v3.model.Members;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<com.example.debtsettler_v3.adapter.MembersAdapter.MembersViewHolder> {

    Context context;
    List<Members> membersList;

    public MembersAdapter(Context context, List<Members> membersList) {
        this.context = context;
        this.membersList = membersList;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.members_row_item, parent, false);
        return new MembersViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {

        holder.membersImageView.setImageResource(membersList.get(position).getImageUrl());
        holder.membersImageView.setColorFilter(Color.parseColor("#"+membersList.get(position).getBarvaUp()),android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.membersNameView.setText(membersList.get(position).getName());
        Double stanje = membersList.get(position).getMoney();
        holder.membersMoneyView.setText(membersList.get(position).getMoney().toString().replace('.', ','));
        if (stanje >= 0.0) {
            holder.moneyImageView.setColorFilter(context.getResources().getColor(R.color.MoneyGreen));
        } else {
            holder.moneyImageView.setColorFilter(context.getResources().getColor(R.color.MoneyRed));
        }


    }

    @Override
    public int getItemCount() {

        return membersList.size();
    }

    public static class MembersViewHolder extends RecyclerView.ViewHolder{

        ImageView membersImageView;
        ImageView moneyImageView;
        TextView membersNameView;
        TextView membersMoneyView;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);

            membersImageView = itemView.findViewById(R.id.memberImage);
            membersNameView = itemView.findViewById(R.id.memberName);
            membersMoneyView = itemView.findViewById(R.id.memberMoney);
            moneyImageView = itemView.findViewById(R.id.imageMoney);
        }
    }


}
