package com.sodapop.sodafood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sodapop.sodafood.adapter.Adaptadorpedidos;
import com.sodapop.sodafood.adapter.Adaptadorproductos;
import com.sodapop.sodafood.modelo.Almacen;
import com.sodapop.sodafood.modelo.Pedido;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.layout.simple_spinner_item;
import static com.sodapop.sodafood.MainActivity.CONNECTION_TIMEOUT;
import static com.sodapop.sodafood.MainActivity.READ_TIMEOUT;

public class intentIniciodeldia extends AppCompatActivity {
    String session, nombreususrio, almacenactivo, idalmacenactivo;
    String FileName = "myfile";
    SharedPreferences prefs;
    private String[] strArrData = {"No Suggestions"};
    private String[] strArrDataventas = {"No Suggestions"};
    private String[] strArrDataproducto = {"No Suggestions"};
    private String[] strArrDataproductopedido = {"No Suggestions"};
    private String[] strArrDatarecibe = {"No Suggestions"};
    private String[] strArrDatamovimientos = {"No Suggestions"};


    private RecyclerView recycler;
    private RecyclerView.Adapter adapterproducto;
    private RecyclerView.LayoutManager lManager;


    private RecyclerView.Adapter adapterventas;
    private RecyclerView.Adapter adapterproductoventas;
    private RecyclerView recyclerproducto, recyclerventas;
    ArrayList<Tipomovimiento> peopletipomovimiento = new ArrayList<>();
    ArrayList<Usuarios> people = new ArrayList<>();
    ArrayList<Usuarios> people2 = new ArrayList<>();
    ArrayList<Productos> peopleproducto = new ArrayList<>();
    ArrayList<Productos> peoplepventas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlincial);
        TextView fechadehoy = (TextView) findViewById(R.id.fechaactual);
        TextView usuario = (TextView) findViewById(R.id.usuarioactivo);

        TextView almacen = (TextView) findViewById(R.id.almacenactivo);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String currentDateandTime = sdf.format(new Date());
        fechadehoy.setText(currentDateandTime);
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("sessionid", "");
        usuario.setText(nombreususrio = sharedPreferences.getString("nombreusuariof", ""));
        almacen.setText(sharedPreferences.getString("almacenactivosf", ""));

        idalmacenactivo = sharedPreferences.getString("idalmacenactivosf", "");
// Obtener el Recycler PRODUCTOS
        recyclerproducto = (RecyclerView) findViewById(R.id.recyclerlistado);


        recyclerproducto.setHasFixedSize(true);
      recyclerproducto.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        int numberOfColumns = 6;
        recyclerproducto.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        recyclerproducto.setLayoutManager(lManager);
        recyclerventas = (RecyclerView) findViewById(R.id.recyclerlistado);
        int numberOfColumnss = 6;
        recyclerventas.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        recyclerventas.setLayoutManager(lManager);

        //administradires de chancay
        int e = Integer.valueOf(idalmacenactivo);
        new traertiposdemovimientos().execute("b");



        if (e == 1) {

//admin de chancay
            new administradoresdelocal().execute(idalmacenactivo, "3");
            new trabajadoresdelocal().execute(idalmacenactivo, "5");


        } else if (e == 2) {


            new administradoresdelocal().execute(idalmacenactivo, "2");
            new trabajadoresdelocal().execute(idalmacenactivo, "4");
        }
        //traer productos para inicio de dia
        //traer

        Spinner spinmovio = (Spinner) findViewById(R.id.spinmovimiento);


        spinmovio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        new traerpedidos().execute(idalmacenactivo, currentDateandTime);

                        break;
                    case 1:
                        Toast.makeText(parent.getContext(), "traspasos", Toast.LENGTH_SHORT).show();



                        break;
                    case 2:
                        Toast.makeText(parent.getContext(), "Salidas", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(parent.getContext(), "entradas", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(parent.getContext(), "pedido", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        new traerproductos().execute(idalmacenactivo);
                        break;
                    case 6:
                        Toast.makeText(parent.getContext(), "final del dia", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });







    }
    private class administradoresdelocal extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Usuarios> listaalmaceno = new ArrayList<Usuarios>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/administradoresdealmacenes.php");
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
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idalmacen", params[0])

                        .appendQueryParameter("idcargo", params[1]);
                String query = builder.build().getEncodedQuery();
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
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<String> dataListo = new ArrayList<String>();
            Usuarios meso1;
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data1 = jArray.optJSONObject(i);
                        meso1 = new Usuarios(json_data1.getInt("idusuario"), json_data1.getString("nombreusuario"), "", "", "");
                        people.add(meso1);
                    }
                    strArrData = dataListo.toArray(new String[dataListo.size()]);
                    Spinner spinentrega = (Spinner) findViewById(R.id.spinnerentrega);
                    ArrayAdapter<Usuarios> adaptadorl = new ArrayAdapter<Usuarios>(intentIniciodeldia.this, android.R.layout.simple_spinner_item, people);
                    spinentrega.setAdapter(adaptadorl);
                } catch (JSONException e) {

                }

            }

        }

    }

    private class trabajadoresdelocal extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Usuarios> listaalmaceno = new ArrayList<Usuarios>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/administradoresdealmacenes.php");
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
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idalmacen", params[0])

                        .appendQueryParameter("idcargo", params[1]);
                String query = builder.build().getEncodedQuery();
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
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result2) {
            ArrayList<String> dataList2 = new ArrayList<String>();
            Usuarios meso2;
            if (result2.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result2);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data2 = jArray.optJSONObject(i);
                        meso2 = new Usuarios(json_data2.getInt("idusuario"), json_data2.getString("nombreusuario"), "", "", "");
                        people2.add(meso2);
                    }
                    strArrDatarecibe = dataList2.toArray(new String[dataList2.size()]);
                    Spinner spnrecibe = (Spinner) findViewById(R.id.spinrecibe);
                    ArrayAdapter<Usuarios> adaptadorl2 = new ArrayAdapter<Usuarios>(intentIniciodeldia.this, android.R.layout.simple_spinner_item, people2);
                    spnrecibe.setAdapter(adaptadorl2);
                } catch (JSONException e) {

                }

            }

        }

    }

    private class traertiposdemovimientos extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Tipomovimiento> listaalmacenomovimiento = new ArrayList<Tipomovimiento>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/apitraertodoslosmovimientos.php");
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
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idcargo", params[0]);
                String query = builder.build().getEncodedQuery();
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
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<String> dataListmovimiento = new ArrayList<String>();
            Tipomovimiento mesomovimiento;
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_datamovimiento = jArray.optJSONObject(i);
                        mesomovimiento = new Tipomovimiento(json_datamovimiento.getInt("idtipomovimiento"), json_datamovimiento.getString("nombre"));
                        peopletipomovimiento.add(mesomovimiento);
                    }
                    strArrDatamovimientos = dataListmovimiento.toArray(new String[dataListmovimiento.size()]);
                    Spinner spinmovi = (Spinner) findViewById(R.id.spinmovimiento);
                    ArrayAdapter<Tipomovimiento> adaptadorlmovi = new ArrayAdapter<Tipomovimiento>(intentIniciodeldia.this, android.R.layout.simple_spinner_item, peopletipomovimiento);
                    spinmovi.setAdapter(adaptadorlmovi);
                } catch (JSONException e) {

                }

            }

        }

    }

    private class traerproductos extends AsyncTask<String, String, String> {
        HttpURLConnection conne;
        URL url = null;
        ArrayList<Productos> listaalmaceno = new ArrayList<Productos>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sodapop.pe/sugest/apitraerproductosiniciodeldia.php");
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

                        .appendQueryParameter("idalmacen", params[0]);

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
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();

                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            peopleproducto.clear();
            ArrayList<String> dataListproducto = new ArrayList<String>();
            Productos mesoproducto;
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.optJSONObject(i);
                        mesoproducto = new Productos(json_data.getInt("idproducto"), json_data.getString("nombreproducto"), "", "", null, json_data.getString("descripcion"));
                        peopleproducto.add(mesoproducto);
                    }
                    strArrDataproducto = dataListproducto.toArray(new String[dataListproducto.size()]);
                    adapterproducto = new Adaptadorproductos(peopleproducto, getApplicationContext());
                    recyclerventas.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    recyclerventas.setAdapter(adapterproducto);
                } catch (JSONException e) {
                }
            }
        }
    }

    private class traerpedidos extends AsyncTask<String, String, String> {
        ArrayList<Ventas> peopleventas = new ArrayList<>();
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
                        .appendQueryParameter("fechapedido", params[1]);
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
                    return ("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conne.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            peopleventas.clear();

            ArrayList<String> dataListventitas = new ArrayList<String>();
            Ventas vp;
            if (result.equals("no rows")) {
            } else {
                try {
                    JSONArray jArray1 = new JSONArray(result);
                    for (int i = 0; i < jArray1.length(); i++) {

                        JSONObject json_data2 = jArray1.optJSONObject(i);

                        vp = new Ventas(json_data2.getInt("numeromesa"), json_data2.getDouble("totalpedido"));

                        peopleventas.add(vp);
                    }

                    strArrDataventas = dataListventitas.toArray(new String[dataListventitas.size()]);
                    adapterventas = new Adaptadorpedidos(peopleventas, getApplicationContext());
                    recyclerproducto.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    recyclerproducto.setAdapter(adapterventas);
                } catch (JSONException e) {
                }
            }
        }
    }

    private class VIEW_TYPES
    { public static final int Header = 1;
    public static final int Normal = 2;
    public static final int Footer = 3; }

}
