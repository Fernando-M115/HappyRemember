package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.proyectofinal.db.DbHelper;

public class CumpleActivity extends AppCompatActivity {

    /**
     * VARIABLES DECLARADAS PARA UTILIZARLAS DESPUES
     */
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;

    /**
     * ARREGLOS DE STRINGS PARA UTILIZARLOS PARA GUARDAR
     */
    int [] tam_id;
    String [] nombre, telefono, fecha;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cumpleanios_activity);
        //Asignar la variable al id
        listView = findViewById(R.id.listViewCumple);
        //Llamar al metodo mostrar informacion
        mostrarInformacion();
    }

    //Metodos para cuando regrese a la pantalla volver a actualizar la lista de cumpleaños
    @Override
    protected void onRestart() {
        super.onRestart();
        mostrarInformacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarInformacion();
    }

    /**
     * Intent para el boton de abrir la pantalla de añadir un cumpleaños
     */
    public void activity_Nuevo(View view) {
        Intent intent = new Intent(this, AddCumple.class);
        startActivity(intent);
    }

    /**
     * Metodo para mostrar la informacion de la base de datos en el list view
     */
    private void mostrarInformacion(){
        //LLAMAMOS A LA BASE DE DATOS Y HACEMOS LA CONSULTA
        dbHelper = new DbHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        //OBTENEMOS LOS VALORES EN EL CURSOR
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select *from t_cumpleaños", null);
        if(cursor.getCount()>0){
            //INICIAMOS LOS ARREGLOS AL TAMANIO
            tam_id = new int[cursor.getCount()];
            nombre = new String[cursor.getCount()];
            telefono = new String[cursor.getCount()];
            fecha = new String[cursor.getCount()];

            //VARIABLE LOCAL CONTADOR
            int contador = 0;

            //ANIADIMOS LOS VALORES DEL CURSOR A LOS ARREGLOS CORRESPONDIENTES
            while(cursor.moveToNext()){
                tam_id[contador] = cursor.getInt(0);
                nombre[contador] = cursor.getString(1);
                fecha[contador] = cursor.getString(2);
                telefono[contador] = cursor.getString(3);
                //AUMENTAMOS EL CONTADOR
                contador++;
            }
            //CREAMOS EL ADAPTER Y LO ANIADIMOS AL LIST VIEW
            AdaptorCumple myAdapter = new AdaptorCumple();
            listView.setAdapter(myAdapter);
        }
    }

    //CLASE ADAPTOR CUMPLE LOCAL
    //EXTIENDE DE BASEADAPTER
    private class AdaptorCumple extends BaseAdapter {
        @Override
        public int getCount() {
            return nombre.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        /**
         * METODO PARA OBTENER EL CARDVIEW Y TODOS LOS VALORES
         * PONER LOS VALORES DE LOS ARREGLOS EN EL CARDVIEW
         * Y UTILIZARLOS PARA EL RECICLERVIEW
         */
        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View convertview, ViewGroup parent) {
            TextView txtNombre, txtTelefono, txtFecha;
            CardView cardView;
            ImageView imageupdate, imageborrar;
            //Utilizar el convertview y el inflater
            //asignar a las variables locales los campos del cardview
            convertview = LayoutInflater.from(CumpleActivity.this).inflate(R.layout.single_data,parent,false);
            txtNombre = convertview.findViewById(R.id.editNombreCompleto);
            txtTelefono = convertview.findViewById(R.id.editTelefono);
            txtFecha = convertview.findViewById(R.id.editFecha);
            imageborrar = convertview.findViewById(R.id.imgBorrar);
            imageupdate = convertview.findViewById(R.id.imgUpdate);
            cardView = convertview.findViewById(R.id.cardview);

            //SetOnClick para la imagen del update
            imageupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Obtenemos los datos de la persona que deseamos hacer el update
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",tam_id[i]);
                    bundle.putString("nombrecompleto",nombre[i]);
                    bundle.putString("telefono",telefono[i]);
                    bundle.putString("fecha",fecha[i]);
                    //Intent para llamar la actividad de AddCumple pasando los bundles
                        Intent intent = new Intent(CumpleActivity.this, AddCumple.class);
                        intent.putExtra("data",bundle);
                        startActivity(intent);
                }
            });

            //SetOnclick para la imagen de borrar
            imageborrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Obtenemos la base de datos
                    sqLiteDatabase = dbHelper.getReadableDatabase();
                    //Hacemos la consulta a la base de datos para elminarlo y enviar el id correspondiente
                    long respuesta = sqLiteDatabase.delete("t_cumpleaños","id=" +tam_id[i],null);
                    if(respuesta != -1){
                        //Toast y actualizar la tabla
                        Toast.makeText(CumpleActivity.this, "Ha sido eliminado correctamente", Toast.LENGTH_SHORT).show();
                        mostrarInformacion();
                    }
                }
            });

            //Poner los valores a los txt.
            txtNombre.setText(nombre[i]);
            txtTelefono.setText(telefono[i]);
            txtFecha.setText(fecha[i]);
            return convertview;
        }
    }
}
