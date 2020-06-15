package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.Jugador;
import com.apjcompany.apjcomputer.proyecto.ui.adapter.ClasificacionListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainClasificacionActivity extends AppCompatActivity {
    private ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clasificacion);
        lista=findViewById(R.id.listaViewClasificacion);
        String url = getResources().getString(R.string.url_clasificacion);
        CargarJsonTaskGoleadores tarea = new CargarJsonTaskGoleadores();
        tarea.execute(url);
    }

    //Tarea Asíncrona para cargar un JSON en segundo plano
    private class CargarJsonTaskGoleadores extends AsyncTask<String, Integer, Boolean> {
        private JSONArray jsonResponse;

        protected Boolean doInBackground(String... params) {
            boolean resul = false;
            String StringResponse;
            try {
                StringResponse = getJsonString(params[0]);
                jsonResponse = new JSONArray(StringResponse);
                resul = true;

            } catch (JSONException e) {
                Log.e("JSONError", e.getLocalizedMessage());
            }
            return resul;
        }

        private String getJsonString(String urlString) {
            String result = "";
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();  // creamos HttpClient
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String jsonInputString = getResources().getString(R.string.json_peticion_2020_Clasificacion);;
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    result = response.toString();
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        // Metodo dentro de la tarea asincrona que añade los jugadores de la BBDD a una lista.
        protected void onPostExecute(Boolean resul) {
            ArrayList<Jugador> listaJugadores;
            try {
                listaJugadores = new ArrayList<>();
                for (int i = 0; i < jsonResponse.length(); i++) {
                    Jugador juga = new Jugador();
                    JSONObject estado = new JSONObject(jsonResponse.get(i).toString());
                    if (estado.has("name"))
                        juga.setNombre(estado.getString("name"));
                    if (estado.has("points"))
                        juga.setPuntos(Short.valueOf(estado.getString("points")));
                    if (estado.has("matches"))
                        juga.setPartidosPlay(Byte.valueOf(estado.getString("matches")));
                    if (estado.has("wins"))
                        juga.setPartidosWin(Byte.valueOf(estado.getString("wins")));
                    if (estado.has("loses"))
                        juga.setPartidosLose(Byte.valueOf(estado.getString("loses")));
                    if (estado.has("goalsFor"))
                        juga.setGoles(Short.valueOf(estado.getString("goalsFor")));
                    listaJugadores.add(juga);
                }

                Collections.sort(listaJugadores);//ORDENA LISTA DE JUGADORES POR PUNTOS.
                // Adapta el ArrayList de la clase Jugadores un ListView de la interfaz grafica
                ClasificacionListAdapter adapter=new ClasificacionListAdapter(MainClasificacionActivity.this,R.layout.view_adapter_clasificacion,listaJugadores);
                lista.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Hay un error, perdone las molestias", Toast.LENGTH_SHORT).show();
                Log.e("Excepcion: ", e.toString());
            }
        }
    }
}