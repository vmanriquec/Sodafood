package com.sodapop.sodafood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.sodapop.sodafood.adapter.Adaptadorpedidos;
import com.sodapop.sodafood.modelo.Productos;
import com.sodapop.sodafood.modelo.Tipomovimiento;
import com.sodapop.sodafood.modelo.Usuarios;
import com.sodapop.sodafood.modelo.Ventas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.sodapop.sodafood.MainActivity.CONNECTION_TIMEOUT;
import static com.sodapop.sodafood.MainActivity.READ_TIMEOUT;

public class Mainventas extends AppCompatActivity {

    private String[] strArrDataventas = {"No Suggestions"};
    private RecyclerView recycler;
    private RecyclerView.Adapter adapterproducto;
    private RecyclerView.LayoutManager lManager;



    private RecyclerView.Adapter adapterventas;
    private RecyclerView recyclerproducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainventas);
        String session,nombreususrio,almacenactivo,idalmacenactivo;
        String FileName ="myfile";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences sharedPreferences=getSharedPreferences(FileName, Context.MODE_PRIVATE);
        session=sharedPreferences.getString("sessionid","");
        idalmacenactivo = sharedPreferences.getString("idalmacenactivosf","");
        recyclerproducto = (RecyclerView) findViewById(R.id.recyventas);
        new traerpedidos().execute(idalmacenactivo,currentDateandTime);
    }


    private class traerpedidos extends AsyncTask<String, String, String> {

        HttpURLConnection conne;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/apitraerpedidoscobrados.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                conne = (HttpURLConnection) url.openConnection();
                conne.setReadTimeout(READ_TIMEOUT);
                conne.setConnectTimeout(CONNECTION_TIMEOUT);
                conne.setRequestMethod("POST");
                conne.setDoInput(true);
                conne.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idalmacen", params[0])
                        .appendQueryParameter("fechapedido", params[1])      ;
                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conne.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conne.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conne.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conne.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (
                            result.toString()
                    );
                } else {
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace()                ;
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {


            ArrayList<String> dataListventitas = new ArrayList<String>();
            Ventas  vp;
            ArrayList<Ventas> peopleventas=new ArrayList<>();

            if(result.equals("no rows")) {
            }else{
                try {
                    JSONArray jArray1 = new JSONArray(result);
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject json_data2 = jArray1.optJSONObject(i);

                        int m=json_data2.getInt("idmesa");
                        Double t=json_data2.getDouble("totalpedido");
                        vp = new Ventas(m,t);
                        int g =vp.getIdmesa();
                        peopleventas.add(vp);
                    }

                    strArrDataventas = dataListventitas.toArray(new String[dataListventitas.size()]);
                    adapterventas = new Adaptadorpedidos(peopleventas,getApplicationContext());
                    recyclerproducto.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    recyclerproducto.setAdapter(adapterventas);
                } catch (JSONException e) {
                    Log.d("orejaerror",e.toString());
                }
            }
        }
    }


}
