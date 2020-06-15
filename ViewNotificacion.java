package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apjcompany.apjcomputer.proyecto.R;

public class ViewNotificacion extends AppCompatActivity {
    private TextView tituloTv,mensajeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notificacion);
        tituloTv=findViewById(R.id.tVTituloNotificacion);
        mensajeTv=findViewById(R.id.tVMensajeNotificacion);
        Intent intent=getIntent();
        tituloTv.setText(intent.getStringExtra("Titulo"));
        mensajeTv.setText(intent.getStringExtra("Mensaje"));
    }

    public void irMenuPrinci(View v){
        startActivity(new Intent(this, MainActivity.class).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }
}