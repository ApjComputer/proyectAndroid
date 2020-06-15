package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.apjcompany.apjcomputer.proyecto.R;


public class AdminMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }
    public void irNotificacion(View v){
        startActivity(new Intent(this, AdminNotificacionActivity.class));
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    public void irCrud(View v){
        startActivity(new Intent(this, AdminCrudActivity.class));
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    public void irMenuPrincipal(View v){
        startActivity(new Intent(this, MainActivity.class).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }
}
