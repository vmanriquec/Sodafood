package com.firesoda.sodafood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firesoda.sodafood.R;
import com.firesoda.sodafood.modelo.Detallepedido;
import com.firesoda.sodafood.modelo.Productos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class Adaptadorproductos extends RecyclerView.Adapter<Adaptadorproductos.AdaptadorViewHolder> {
    private Context mainContext;
    String foto;
    SharedPreferences prefs;
    String FileName ="myfile",productocabecera;
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
        protected TextView precio;
        protected TextView cantidadpedida;




protected ImageView imagen;
        protected TextView productoingredientes, inventario;

        protected Button mas;
        protected Button menos;

        public AdaptadorViewHolder(View v){
            super(v);
            this.idproducto=(TextView) v.findViewById(R.id.idproductop2);
            this.productonombre=(TextView) v.findViewById(R.id.nombre);
            this.mas=(Button)v.findViewById(R.id.botonmas1);
            this.menos=(Button)v.findViewById(R.id.botonmenos);
            this.cantidadpedida=(TextView) v.findViewById(R.id.cuantova);
            this.precio=(TextView) v.findViewById(R.id.precio);
            this.productoingredientes=(TextView)v.findViewById(R.id.ingredientes);
            this.inventario=(TextView)v.findViewById(R.id.inventario);
            this.imagen=(ImageView)v.findViewById(R.id.imagen);
            this.inventario=(TextView)v.findViewById(R.id.inventario);
        }
    }
    @Override
    public Adaptadorproductos.AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjetamuestraproducto,viewGroup,false);
        return new Adaptadorproductos.AdaptadorViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final Adaptadorproductos.AdaptadorViewHolder viewHolder, final int position) {
        productocabecera="";
        viewHolder.productonombre.setText("");
        final Productos item = items.get(position);
        viewHolder.itemView.setTag(item);

        viewHolder.productonombre.setText(item.getNombreproducto());
        productocabecera=item.getNombreproducto();
        viewHolder.idproducto.setText(String.valueOf(item.getIdproducto()));
viewHolder.productoingredientes.setText(String.valueOf(item.getIngredientes()));
        viewHolder.precio.setText("S/. " + String.valueOf(item.getPrecventa()));
        viewHolder.inventario.setText((String.valueOf(item.getEstadoproducto())));
        foto = item.getDescripcion().toString();


        Picasso.get().load(foto).transform(new CropSquareTransformation()).resize(80, 80)
                .into(viewHolder.imagen);


        // viewHolder.mas.setVisibility(View.GONE);
        //viewHolder.menos.setVisibility(View.GONE);
        /*asignar imagen desde url*/
        Log.d("orejitazo","siiiiii entra descripcion "+ item.getDescripcion());
        /*boton mas o menos cantidad*/
        viewHolder.mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mainContext)
                        .setTitle(productocabecera);
                builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                builder.setHeaderView(R.layout.dialog_header_layout);

               // builder.setMessage("Select the topping for your pizza!!!");


                builder.setMultiChoiceItems(new String[]{"Spinach", "Red & Yellow pepper", "Baby corn", "Olives", "Chicken"}, new boolean[]{false, false, false, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index, boolean b) {
                        Toast.makeText(mainContext, "Row:"+index+" "+(b? "Selected":"Unselected"), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.addButton("Listo", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();




// Show the alert


               // String cantidad=viewHolder.cantidadpedida.getText().toString();
                //int c= Integer.parseInt(cantidad);
                //if(c>=0){
                  ///  c=c+1;viewHolder.cantidadpedida.setText( String.valueOf(c));

                    // Toast.makeText(getApplicationContext(),item.getNombreproducto()+viewHolder.cantidadpedida.getText(),Toast.LENGTH_LONG).show();


       //         }
        }
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
