package com.firesoda.sodafood.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firesoda.sodafood.R;
import com.firesoda.sodafood.modelo.Ventas;


import java.util.ArrayList;
import java.util.List;

public class Adaptadorpedidos extends RecyclerView.Adapter<Adaptadorpedidos.AdaptadorViewHolder> {
    private Context mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile";
    private List<Ventas> items;
    public Adaptadorpedidos(ArrayList<Ventas> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;


    }
    static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        protected TextView mesapedido;
        protected TextView totalpedido;
        protected TextView clientepedido;
        protected TextView hora;


        public AdaptadorViewHolder(View v){
            super(v);
            this.mesapedido=(TextView) v.findViewById(R.id.txtmesa);
            this.totalpedido=(TextView) v.findViewById(R.id.txttotalpedido);

        }
    }


    @Override
    public Adaptadorpedidos.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetaproductos,viewGroup,false);
        return new Adaptadorpedidos.AdaptadorViewHolder(v);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final Adaptadorpedidos.AdaptadorViewHolder viewHolder, final int position) {
        final Ventas item = items.get(position);
        viewHolder.itemView.setTag(item);



    viewHolder.mesapedido.setText(String.valueOf(item.getIdmesa()));
    viewHolder.totalpedido.setText(String.valueOf(item.getTotalpedido()));








    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

