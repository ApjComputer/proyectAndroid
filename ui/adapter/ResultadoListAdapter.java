package com.apjcompany.apjcomputer.proyecto.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.ResultadoPartido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

//Clase para adaptar le resultado y los jugadores de cada partido a un listView de una forma personalizada.
public class ResultadoListAdapter extends ArrayAdapter<ResultadoPartido> {
    private int mResource;

    public ResultadoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ResultadoPartido> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(getContext());
        convertView=inflater.inflate(mResource, parent, false);
        TextView columnaEquipoAzul=convertView.findViewById(R.id.columnaEquAzul);
        TextView columnaEquipoBlanco=convertView.findViewById(R.id.columnaEquBlanco);
        columnaEquipoAzul.setText(getItem(position).getEquipoAzul());
        columnaEquipoBlanco.setText(getItem(position).getEquipoBlanco());
        return convertView;
    }
}
