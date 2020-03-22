package com.sodapop.sodafood.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sodapop.sodafood.CarDb;
import com.sodapop.sodafood.R;
import com.sodapop.sodafood.modelo.Detallepedido;
import com.sodapop.sodafood.modelo.Productos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Adaptadorproductos extends RecyclerView.Adapter<Adaptadorproductos.AdaptadorViewHolder> {
    private Context mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile";
    private List<Productos> items;
    ArrayList<Detallepedido> detallepedido=new ArrayList<>();
    Detallepedido objdetallepedido;
    public Adaptadorproductos(List<Productos> items, Context contexto){
        this.mainContext=contexto;
        this.items=items;


    }
    static class AdaptadorViewHolder extends RecyclerView.ViewHolder{
        protected TextView productonombre;
        protected TextView idproducto;

        protected TextView cantidadpedida;
        protected ImageView productoimagen;

        protected Button mas;
        protected Button menos;

        public AdaptadorViewHolder(View v){
            super(v);
            this.idproducto=(TextView) v.findViewById(R.id.idproductop);
            this.productoimagen=(ImageView) v.findViewById(R.id.productoimagenp);
            this.productonombre=(TextView) v.findViewById(R.id.stockp);
            this.mas=(Button)v.findViewById(R.id.botonmas);
            this.menos=(Button)v.findViewById(R.id.botonmenos);
            this.cantidadpedida=(TextView) v.findViewById(R.id.cantidadpedida);
        }
    }
    @Override
    public Adaptadorproductos.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetaadicionar,viewGroup,false);
        return new Adaptadorproductos.AdaptadorViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final Adaptadorproductos.AdaptadorViewHolder viewHolder, final int position) {
        final Productos item = items.get(position);
        viewHolder.itemView.setTag(item);
        Log.d("orejitazo","siiiiii entraitemmm"+item.getNombreproducto());

        viewHolder.productonombre.setText(item.getNombreproducto());

        viewHolder.idproducto.setText(String.valueOf(item.getIdproducto()));
        // viewHolder.mas.setVisibility(View.GONE);
        //viewHolder.menos.setVisibility(View.GONE);
        /*asignar imagen desde url*/
        Log.d("orejitazo","siiiiii entra descripcion "+ item.getDescripcion());

        foto=item.getDescripcion().toString();

        Picasso.with(getApplicationContext()) .load(foto).transform(new CropCircleTransformation()).resize(100, 100)
                .into( viewHolder.productoimagen);

        viewHolder.productoimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast ImageToast = new Toast(getApplicationContext());
                LinearLayout toastLayout = new LinearLayout(getApplicationContext());
                toastLayout.setOrientation(LinearLayout.VERTICAL);

                ImageView image = new ImageView(getApplicationContext());
                TextView text = new TextView(getApplicationContext());
                foto=item.getDescripcion().toString();

                Picasso.with(getApplicationContext()) .load(foto).transform(new CropSquareTransformation())
                        .resize(350, 350)
                        .into( image);
                text.setText(item.getIngredientes());
                text.setTextColor(Color.RED);
                text.setBackgroundColor(Color.WHITE);
                text.setGravity(12);
                toastLayout.addView(image);
                toastLayout.addView(text);
                ImageToast.setView(toastLayout);
                ImageToast.setGravity (Gravity.TOP | Gravity.LEFT, 40, 40);
                ImageToast.setDuration(Toast.LENGTH_LONG);
                ImageToast.show();


                ImageToast.getView().setPadding( 20, 100, 20, 20);



            }
        });

        /*boton mas o menos cantidad*/
        viewHolder.mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad=viewHolder.cantidadpedida.getText().toString();
                int c= Integer.parseInt(cantidad);
                if(c>=0){
                    c=c+1;viewHolder.cantidadpedida.setText( String.valueOf(c));

                    // Toast.makeText(getApplicationContext(),item.getNombreproducto()+viewHolder.cantidadpedida.getText(),Toast.LENGTH_LONG).show();


                }            }
        });


        viewHolder.menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad=viewHolder.cantidadpedida.getText().toString();
                int c= Integer.parseInt(cantidad);
                if(c>0){
                    c=c-1;

                    viewHolder.cantidadpedida.setText( String.valueOf(c));
                    //   Toast.makeText(getApplicationContext(),item.getNombreproducto()+viewHolder.cantidadpedida.getText(),Toast.LENGTH_LONG).show();

                }            }
        });


        /*si esta check activo para aumentar cantidad*/







    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
