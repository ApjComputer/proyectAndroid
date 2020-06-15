package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.MensajeDialog;

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

public class LoginActivity extends AppCompatActivity {
    private EditText nameET,passwordET;
    private MensajeDialog miMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameET=findViewById(R.id.tvUsername);
        passwordET=findViewById(R.id.tvPassword);
    }

    // Metodo que prueba si los campos rellenados por el usuario de la app estan vacios o no, para
    // poder continuar.
    public void comprobarUsuario(View vista){
        if(!nameET.getText().toString().equals("") || !passwordET.getText().toString().equals("") ){
            String url = getResources().getString(R.string.url_usuario);
            CargarJsonTaskUser tarea = new CargarJsonTaskUser();
            tarea.execute(url);
        } else {
            mostrarMensaje("CAMPO VACIO",true);
        }
    }

    //Muestra un mensaje en pantalla de Operacion Erronea o Exitosa dependediendo del parametro.
    private void mostrarMensaje(String mensaje, boolean error){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.this,R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View content=inflater.inflate(R.layout.layout_alertadialogo_ok_error, null);
        builder.setView(content);
        ImageView imag=content.findViewById(R.id.imageViewError);
        if(!error){
            imag.setImageResource(R.drawable.image_perfect);
        }
        TextView tvMensajeAlert=content.findViewById(R.id.tvMensajeError);
        tvMensajeAlert.setText(mensaje);
        builder.setNegativeButton(R.string.ok_dialogText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //Tarea Asíncrona para cargar un JSON en segundo plano
    private class CargarJsonTaskUser extends AsyncTask<String, Integer, Boolean> {
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

        // Metodo dentro de la tarea asincrona que busca la existencia de usuario, si existe ejecuta
        // otra activity.
        protected void onPostExecute(Boolean resul) {
            JSONObject estado=null;
            String nameTv,passwordTV;
            boolean encontradoNombre,errorNameOrPassword,encontrado;
            int cont;
            try {
                errorNameOrPassword=false;
                encontradoNombre=false;
                encontrado=false;
                cont=0;
                nameTv=nameET.getText().toString().toUpperCase();
                passwordTV=passwordET.getText().toString().toUpperCase();

                //Busca si existe el usuario y el tipo de usuario que es.
                while(cont< jsonResponse.length() && !encontradoNombre) {
                    estado = new JSONObject(jsonResponse.get(cont).toString());
                    if(estado.getString("name").equals(nameTv)){
                        encontradoNombre=true;
                        errorNameOrPassword=true;
                        if(estado.getString("password").equals(passwordTV)){
                            encontrado=true;
                        }
                    }
                    cont++;
                }

                //Sentencia que comprueba si se ha encontrado el usuario o no!
                //Si no lo encuentra muestra un mensaje de error.
                if(encontrado && encontradoNombre){
                    if(estado.getBoolean("administrador")){
                        startActivity(new Intent(getApplicationContext(), AdminMainActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }else{
                        mostrarMensaje("APP EN CONSTRUCION:USUARIO EXISTE", false);
                    }
                }else {
                    if (errorNameOrPassword) {
                        mostrarMensaje("ERROR EN LA CLAVE", true);
                    } else {
                        mostrarMensaje("USUARIO NO EXISTE", true);
                    }
                }

            } catch (Exception e) {
                mostrarMensaje("ERROR AL CONECTAR CON LA API", true);
                Log.e("Excepcion: ", e.toString());
            }
        }

        // Muestra un Alert Dialogo con una imagen determinada dependiendo si se ha efectuado la
        // operacion con existo o con error.
        private void mostrarMensaje(String mensaje, boolean error){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(LoginActivity.this,R.style.AlertDialogStyle);
            LayoutInflater inflater = getLayoutInflater();
            View content=inflater.inflate(R.layout.layout_alertadialogo_ok_error, null);
            builder.setView(content);
            ImageView imag=content.findViewById(R.id.imageViewError);
            if(!error){
                imag.setImageResource(R.drawable.image_perfect);
            }
            TextView tvMensajeAlert=content.findViewById(R.id.tvMensajeError);
            tvMensajeAlert.setText(mensaje);
            builder.setNegativeButton(R.string.ok_dialogText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

    }
}