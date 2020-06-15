package com.apjcompany.apjcomputer.proyecto.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.apjcompany.apjcomputer.proyecto.R;
import com.apjcompany.apjcomputer.proyecto.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminCrudActivity extends AppCompatActivity {
    private ListView listaViewUsuario;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private EditText tvNombreUsuario,tvPasswordUsuario;
    private ArrayList<String> listadoUsuario;
    private ArrayList<Short> listadoPosicion;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crud);
        listaViewUsuario = findViewById(R.id.listaUsuario);
        listadoUsuario = new ArrayList<>();
        listadoPosicion = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Usuario");
        listarUsuarios();
    }

    // Metodo que lista a los usuarios de una BBDD Firebase (Tiempo real) y realiza los cambios
    // pertinentes segun la opcion elegida.
    private void listarUsuarios() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listadoUsuario.clear();
                listadoPosicion.clear();
                //Recorre Todos los Objetos Usuarios y añade los nombres a un ArrayList.
                for (DataSnapshot nodo : dataSnapshot.getChildren()) {
                    Usuario usuario = nodo.getValue(Usuario.class);
                    listadoPosicion.add(Short.parseShort(nodo.getKey()));
                    listadoUsuario.add(usuario.getName());
                }

                // Adapta el ArrayList de los nombres de usuario a un ListView de la interfaz grafica
                // con un listener que produce un evento cuando pulsamos sobre el.
                ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),
                        R.layout.view_adapter_text_center,R.id.tVCenter, listadoUsuario);
                listaViewUsuario.setAdapter(adaptador);
                listaViewUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (position != 0) {//No se permite editar el usuario Admin que esta en la posicion 0.
                            final short posicion=listadoPosicion.get((short)id);
                            final Usuario usuario2=dataSnapshot.child(String.valueOf(posicion))
                                    .getValue(Usuario.class);
                            ref = database.getReference("Usuario").
                                    child(String.valueOf(posicion));
                            //Creamos un alert dialogo con las opciones Editar, Borrar y Cancelar del usuario.
                            AlertDialog.Builder builder = new AlertDialog.Builder
                                    (AdminCrudActivity.this,R.style.AlertDialogStyle);
                            LayoutInflater inflater = getLayoutInflater();
                            View content=inflater.inflate(R.layout.layout_alertdiaologo_menu_crud, null);
                            builder.setView(content);
                            builder.setPositiveButton(R.string.edita_usuario, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String nombre=tvNombreUsuario.getText().toString().toUpperCase();
                                    String clave=tvPasswordUsuario.getText().toString().toUpperCase();
                                    if(!nombre.equals("") && !clave.equals("")) {
                                        if (!buscarExisteUsuario(nombre)) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("name", nombre);
                                            map.put("password", clave);
                                            ref.updateChildren(map);
                                            mostrarMensaje("USUARIO EDITADO", false);
                                        } else {
                                            mostrarMensaje("EXISTE USUARIO", true);
                                        }
                                    }
                                }
                            }).setNegativeButton(R.string.cancelar_dialogText, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).setNeutralButton(R.string.borrar_usuario, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ref.removeValue();
                                    mostrarMensaje("USUARIO BORRADO", false);
                                }
                            });
                            tvNombreUsuario=content.findViewById(R.id.tvUsernameCrud);
                            tvPasswordUsuario=content.findViewById(R.id.tvPasswordCrud);
                            tvNombreUsuario.setText(usuario2.getName());
                            tvPasswordUsuario.setText(usuario2.getPassword());
                            builder.show();
                        } else{
                            mostrarMensaje("ERROR: ADMIN ES INMUTABLE", true);
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mostrarMensaje("ERROR AL CONECTAR BBDD", true);
                Log.e("ERROR ", databaseError.getMessage());
            }
        });
    }


    //Metodo para crear Usuario en la BBDD.
    public void crearUsuario(View v) {
        //Se crea Dialogo con dos campos de texto y dos opciones a poder realizar(CREAR,CANCELAR)
        builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View content=inflater.inflate(R.layout.layout_alertdiaologo_menu_crud, null);
        builder.setView(content);

        //Boton Crear, crea el usuario en la BB si no existe en la primera posición vacía.
        builder.setPositiveButton(R.string.crear_dialogText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nombre=tvNombreUsuario.getText().toString().toUpperCase();
                String clave=tvPasswordUsuario.getText().toString().toUpperCase();

                //Los campos de texto no pueden estar vacio para la creacion de usuario.
                if (!nombre.equals("") && !clave.equals("")) {
                    if(!buscarExisteUsuario(nombre)){
                        ref = database.getReference("Usuario").
                                child(registroVacio());
                        ref.child("administrador").setValue("FALSE");
                        ref.child("name").setValue(nombre);
                        ref.child("password").setValue(clave);
                        mostrarMensaje("USUARIO CREADO", false);
                    }else{
                        mostrarMensaje("EXISTE USUARIO", true);
                    }
                } else {
                    mostrarMensaje("CAMPOS VACIO", true);
                }

            }
        })//Boton cancelar, sale del la notificacion sin ningun cambio.
                .setNegativeButton(R.string.cancelar_dialogText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        tvNombreUsuario=content.findViewById(R.id.tvUsernameCrud);
        tvPasswordUsuario=content.findViewById(R.id.tvPasswordCrud);
        builder.show();
    }


    //Metodo que devuelve si existe el usuario en la BBDD.
    private boolean buscarExisteUsuario (String nombre) {
        boolean existe=false;
        if(listadoUsuario.contains(nombre)){
            existe=true;
        }
        return existe;
    }

    //Metodo que devuelve la primera posicion vacia de la BBDD Firebase.
    private String registroVacio(){
        short cont=0;
        while(cont<listadoPosicion.size() && listadoPosicion.get(cont)==cont){
            cont++;
        }
        return String.valueOf(cont);
    }

    // Muestra un Alert Dialogo con una imagen determinada dependiendo si se ha efectuado la
    // operacion con existo o con error.
    private void mostrarMensaje(String mensaje, boolean error){
        builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
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