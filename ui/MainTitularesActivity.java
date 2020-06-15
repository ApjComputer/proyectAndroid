package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.apjcompany.apjcomputer.proyecto.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainTitularesActivity extends AppCompatActivity {
    private ListView lista;
    private String[] listaJugadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_titulares);
        lista = findViewById(R.id.listaViewTitulares);
        String url = getResources().getString(R.string.url_titulares);
        CargarJsonTask tarea = new CargarJsonTask();
        tarea.execute(url);
    }

    //Tarea Asíncrona para cargar un JSON en segundo plano
    public class CargarJsonTask extends AsyncTask<String, Integer, Boolean> {
        private JSONArray jsonResponse;

        protected Boolean doInBackground(String... params) {
            boolean resul = false;
            String StringResponse;
            JSONObject principal;
            try {
                StringResponse = get(params[0]);
                principal = (new JSONObject(StringResponse).getJSONObject("permanents"));
                jsonResponse = principal.getJSONArray("2019-2020 Lunes");
                resul = true;

            } catch (JSONException e) {
                Log.e("JSONError", e.getLocalizedMessage());
            }
            return resul;
        }

        private String get(String url) {
            InputStream inputStream = null;
            String result = "";
            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient(); // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    // receive response as inputStream
                    if (entity != null) {
                        inputStream = httpResponse.getEntity().getContent(); // convert inputstream to string
                        if (inputStream != null)
                            result = convertInputStreamToString(inputStream);
                        else
                            result = "Fallo al leer iostream!";
                    } else
                        result = "Fallo al leer entity";
                } else
                    result = "Fallo: Error " + httpResponse.getStatusLine().getStatusCode();
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) result += line;
            inputStream.close();
            return result;
        }

        // Metodo dentro de la tarea asincrona que añade los jugadores titulares de la BBDD a una lista.
        protected void onPostExecute(Boolean resul) {
            listaJugadores = new String[jsonResponse.length()];
            try {
                for (byte i = 0; i < jsonResponse.length(); i++) {
                    listaJugadores[i] = jsonResponse.get(i).toString().toUpperCase();
                }
                // Adapta el ArrayList de la lista de jugadores titulares a un ListView de la interfaz
                // grafica con un layout concreto.
                ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),
                        R.layout.view_adapter_text_center,R.id.tVCenter, listaJugadores);
                lista.setAdapter(adaptador);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Hay un error, perdone las molestias", Toast.LENGTH_LONG).show();
                Log.e("Excepcion: ", e.toString());
            }

        }
    }

}