package com.sodapop.sodafood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class intentIniciodeldia extends AppCompatActivity {
    String session,nombreususrio,almacenactivo;
    String FileName ="myfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlincial);
       TextView fechadehoy =(TextView)findViewById(R.id.fechaactual);
        TextView usuario =(TextView)findViewById(R.id.usuarioactivo);

        TextView almacen =(TextView)findViewById(R.id.almacenactivo);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());
        fechadehoy.setText(currentDateandTime);
        SharedPreferences sharedPreferences=getSharedPreferences(FileName, Context.MODE_PRIVATE);
        session=sharedPreferences.getString("sessionid","");
        usuario.setText( nombreususrio=sharedPreferences.getString("nombreusuariof",""));
        almacen.setText(sharedPreferences.getString("almacenactivosf",""));
}
}
