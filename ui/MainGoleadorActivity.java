package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.Jugador;
import com.apjcompany.apjcomputer.proyecto.ui.adapter.JugaGolesListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainGoleadorActivity extends AppCompatActivity {
    private ArrayList<Jugador> listaJugadores;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_goleadores);
        lista=findViewById(R.id.listaViewGoleadores);
        String url = getResources().getString(R.string.url_goleadores);
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
                StringResponse = get(params[0]);
                jsonResponse = new JSONArray(StringResponse);
                resul = true;

            } catch (JSONException e) {
                Log.e("JSONError", e.getLocalizedMessage());
            }
            return resul;
        }

        private String get(String urlString) {
            InputStream inputStream = null;
            String result = "";
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();  // creamos HttpClient
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String jsonInputString = "{\"season\": \"2019-2020 Lunes\"}";
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
        // Metodo dentro de la tarea asincrona que añade los goleadores de la BBDD a una lista.
        protected void onPostExecute(Boolean resul) {
            try {
                listaJugadores = new ArrayList<>();
                for (int i = 0; i < jsonResponse.length(); i++) {
                    // Clase con dos metodos sobreescritos: toString() para mostrar los atributos
                    // especifico por la pantalla y compareTo() para ordenar por un parametro
                    // distinto al original de la clase (en este caso Goles).
                    Jugador juga = new Jugador(){
                        @Override
                        public String toString() {
                            byte size=(byte)(40-super.getNombre().length());
                            String formato = String.format("%-"+size+"s",super.getNombre());
                            return formato + super.goles;
                        }

                        @Override
                        public int compareTo(Jugador o) {
                            return o.getGoles()-super.getGoles();
                        }
                    };
                    JSONObject estado = new JSONObject(jsonResponse.get(i).toString());
                    if (estado.has("name"))
                        juga.setNombre(estado.getString("name"));
                    if (estado.has("scores"))
                        juga.setGoles(Short.valueOf(estado.getString("scores")));
                    listaJugadores.add(juga);
                }

                Collections.sort(listaJugadores);//Ordena la lista de Jugadores por Goles
                // Adapta el ArrayList de la clase Jugadores un ListView de la interfaz grafica
                JugaGolesListAdapter adapter=new JugaGolesListAdapter(getApplicationContext(),R.layout.view_adapter_goleadores,listaJugadores);
                lista.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Hay un error, perdone las molestias", Toast.LENGTH_SHORT).show();
                Log.e("Excepcion: ", e.toString());
            }
        }
    }
}
