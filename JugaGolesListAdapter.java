package com.apjcompany.apjcomputer.proyecto.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.Jugador;
import java.util.ArrayList;

//Clase para adaptar la lista de los goles de los jugadores a un listView de una forma personalizada.
public class JugaGolesListAdapter extends ArrayAdapter<Jugador> {
    private int mResource;

    public JugaGolesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Jugador> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(getContext());
        convertView=inflater.inflate(mResource, parent, false);
        TextView columnaNombre=convertView.findViewById(R.id.primeraColumna);
        TextView columnaGoles=convertView.findViewById(R.id.segundaColumna);
        TextView columnaLetrasGoles=convertView.findViewById(R.id.terceraColumna);
        columnaNombre.setText(getItem(position).getNombre().toUpperCase());
        columnaGoles.setText(String.valueOf(getItem(position).getGoles()));
        columnaLetrasGoles.setText("GOLES");
        return convertView;
    }


}
