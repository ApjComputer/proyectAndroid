package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.apjcompany.apjcomputer.proyecto.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mostrar(View v) {
        switch(v.getId()) {
            case R.id.imageButtonClasificacion:
                startActivity(new Intent(this, MainClasificacionActivity.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;

            case R.id.imageButtonTitulares:
                startActivity(new Intent(this, MainTitularesActivity.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;

            case R.id.imageButtonResultados:
                startActivity(new Intent(this, MainResultadoActivity.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;

            case R.id.imageButtonGoleadores:
                this.startActivity(new Intent(this, MainGoleadorActivity.class));
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
                break;

            case R.id.imageLogin:
                this.startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }
}
