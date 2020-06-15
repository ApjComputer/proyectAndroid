package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.ResultadoPartido;
import com.apjcompany.apjcomputer.proyecto.ui.adapter.ResultadoListAdapter;
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

public class MainResultadoActivity extends AppCompatActivity {
    private long numItemSelected=0;
    private ListView listaResultado;
    private  Spinner miSpinner;
    private TextView tvResultadoBlanco,tvResultadoAzul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_resultado);
        miSpinner = findViewById(R.id.spinner);
        tvResultadoBlanco=findViewById(R.id.tvEquipoBlanco);
        tvResultadoAzul=findViewById(R.id.tvEquipoAzul);
        listaResultado=findViewById(R.id.listaViewResultado);
        String url = getResources().getString(R.string.url_jornadas);
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

        // Metodo dentro de la tarea asincrona que añade los equipos y el resultado de la BBDD a una lista.
        protected void onPostExecute(Boolean resul) {
            ArrayList<ResultadoPartido> listaObjetoResultados;
            JSONArray jsonResponse2;
            String resultadoEquipoAzul,resultadoEquipoBlanco;
            try {
                listaObjetoResultados = new ArrayList<>();
                //Regoge el objeto json de la jornada seleccionada.
                jsonResponse2=jsonResponse.getJSONObject((int)numItemSelected).getJSONArray("data");
                //Añade los jugadores a los respectivos Equipos.
                for (int i = 0; i < jsonResponse2.length(); i++) {
                    ResultadoPartido result=new ResultadoPartido();
                    JSONObject estado = new JSONObject(jsonResponse2.get(i).toString());
                    if (estado.has("blue")){
                        result.setEquipoBlanco(estado.getString("blue").toUpperCase());
                    }
                    if (estado.has("white")) {
                        result.setEquipoAzul(estado.getString("white").toUpperCase());
                    }
                    listaObjetoResultados.add(result);
                }

                //Recoge el resultado del partido.
                resultadoEquipoBlanco=jsonResponse.getJSONObject((int)numItemSelected).
                        getString("scoreWhites").replace(".0","");
                resultadoEquipoAzul=jsonResponse.getJSONObject((int)numItemSelected).
                        getString("scoreBlues").replace(".0","");

                //Adapta los Arraylist de los jugadores al ListView de la activity.
                ResultadoListAdapter adapter=new ResultadoListAdapter(MainResultadoActivity.this,
                        R.layout.view_adapter_resultado,listaObjetoResultados);
                listaResultado.setAdapter(adapter);
                tvResultadoAzul.setText(resultadoEquipoAzul);
                tvResultadoBlanco.setText(resultadoEquipoBlanco);
                //Spinner donde muesta la jornada con un Listener para cuando elege una jordana en contreto.
                miSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Cuando pulsa una jornada, ejecutamos el metodo otra vez con la id de la jornada.
                        numItemSelected= parent.getSelectedItemId();
                        onPostExecute(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Hay un error, perdone las molestias", Toast.LENGTH_SHORT).show();
                Log.e("Excepcion: ", e.toString());
            }
        }
    }
}