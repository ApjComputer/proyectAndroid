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

//Clase para adaptar la Clasificacion de los jugadores a un listView de una forma personalizada.
public class ClasificacionListAdapter extends ArrayAdapter<Jugador> {
    private int mResource;

    public ClasificacionListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Jugador> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(mResource, parent, false);
        TextView columnaPosicion = convertView.findViewById(R.id.columnaPosicion);
        TextView columnaNombre = convertView.findViewById(R.id.primeraColumnaCla);
        TextView columnaPuntos = convertView.findViewById(R.id.segundaColumnaCla);
        TextView columnaPartidosJugados = convertView.findViewById(R.id.terceraColumnaCla);
        TextView columnaPartidosGanados = convertView.findViewById(R.id.cuartaColumnaCla);
        TextView columnaPartidosPerdidos = convertView.findViewById(R.id.quintaColumnaCla);
        columnaPosicion.setText((position+1)+".");
        columnaNombre.setText(getItem(position).getNombre().toUpperCase());
        columnaPuntos.setText(getItem(position).getPuntos()+"PTOS");
        columnaPartidosJugados.setText(getItem(position).getPartidosPlay()+"PJ");
        columnaPartidosGanados.setText(getItem(position).getPartidosWin()+"PG");
        columnaPartidosPerdidos.setText(getItem(position).getPartidosLose()+"PP");
        return convertView;
    }
}