package com.example.debtsettler_v3.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtsettler_v3.R;
import com.example.debtsettler_v3.model.Nakupi;

import java.util.List;

public class ZgodovinaAdapter extends RecyclerView.Adapter<com.example.debtsettler_v3.adapter.ZgodovinaAdapter.ZgodovinaViewHolder> {
    Context context;
    List<Nakupi> zgodovinaNakupovList;

    public ZgodovinaAdapter(Context context, List<Nakupi> zgodovinaNakupovList) {
        this.context = context;
        this.zgodovinaNakupovList = zgodovinaNakupovList;
    }

    @NonNull
    @Override
    public ZgodovinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zgodovina_row_item, parent, false);
        return new ZgodovinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZgodovinaViewHolder holder, int position) {
        holder.zgodovinaimeTrgovineView.setText(zgodovinaNakupovList.get(position).getImeTrgovine());
        String cena;
        if (zgodovinaNakupovList.get(position).getCenaNakupa().length() <3)
            cena = "0"+zgodovinaNakupovList.get(position).getCenaNakupa().substring(0,zgodovinaNakupovList.get(position).getCenaNakupa().length()-2)+","+zgodovinaNakupovList.get(position).getCenaNakupa().substring(zgodovinaNakupovList.get(position).getCenaNakupa().length()-2)+"€";
        else
            cena = zgodovinaNakupovList.get(position).getCenaNakupa().substring(0,zgodovinaNakupovList.get(position).getCenaNakupa().length()-2)+","+zgodovinaNakupovList.get(position).getCenaNakupa().substring(zgodovinaNakupovList.get(position).getCenaNakupa().length()-2)+"€";
        holder.zgodovinaCenaView.setText(cena);
        holder.zgodovinaOpisNakupaView.setText(zgodovinaNakupovList.get(position).getOpisNakupa());
        holder.zgodovinaDanView.setText(zgodovinaNakupovList.get(position).getDatumNakupa().substring(8,10));
        holder.zgodovinaMesecView.setText(zgodovinaNakupovList.get(position).getDatumNakupa().substring(4,8));
        holder.uporabnikColorCircle.setColorFilter(Color.parseColor("#"+zgodovinaNakupovList.get(position).getBarvaUp()),android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        return zgodovinaNakupovList.size();
    }

    public static class ZgodovinaViewHolder extends RecyclerView.ViewHolder{

        TextView zgodovinaimeTrgovineView;
        TextView zgodovinaCenaView;
        TextView zgodovinaOpisNakupaView;
        TextView zgodovinaDanView;
        TextView zgodovinaMesecView;
        ImageView uporabnikColorCircle;

        public ZgodovinaViewHolder(@NonNull View itemView) {
            super(itemView);

            zgodovinaimeTrgovineView = itemView.findViewById(R.id.zgodovinaImeTrgovine);
            zgodovinaCenaView = itemView.findViewById(R.id.zgodovinaCena);
            zgodovinaOpisNakupaView = itemView.findViewById(R.id.zgodovinaOpisNakupa);
            zgodovinaDanView = itemView.findViewById(R.id.zgodovinaDan);
            zgodovinaMesecView = itemView.findViewById(R.id.zgodovinaMesec);
            uporabnikColorCircle = itemView.findViewById(R.id.zgodovinaAvtor);
        }
    }
}
