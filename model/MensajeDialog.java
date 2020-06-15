package com.apjcompany.apjcomputer.proyecto.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.apjcompany.apjcomputer.proyecto.R;

public class MensajeDialog extends AlertDialog {
    private boolean error;
    private String mensajeError;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private LayoutInflater inflater;
    public MensajeDialog(Context context, Boolean error, String mensajeError) {
        super(context);
        this.error=error;
        this.mensajeError=mensajeError;
        prepararMensaje();
    }

    private void prepararMensaje(){
        builder = new androidx.appcompat.app.AlertDialog.Builder(super.getContext(), R.style.AlertDialogStyle);
        this.inflater = (LayoutInflater)super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = getLayoutInflater();
        View content=inflater.inflate(R.layout.layout_alertadialogo_ok_error, null);
        builder.setView(content);
        ImageView imag=content.findViewById(R.id.imageViewError);
        if(!error){
            imag.setImageResource(R.drawable.image_perfect);
        }
        TextView tvMensajeAlert=content.findViewById(R.id.tvMensajeError);
        tvMensajeAlert.setText(this.mensajeError);
        builder.setNegativeButton(R.string.ok_dialogText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
    }

    public void mostrarMensaje(){
        builder.show();
    }
}
