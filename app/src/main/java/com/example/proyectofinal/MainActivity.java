package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinal.db.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    int [] tam_id;
    String [] nombre, telefono, fecha;
    String numero, nombrepersona;
    Date fechaFormatear;
    TextView mostrarCumple;
    ImageView llamada, whats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Primeramente asignamos los recursos del xml a variables creadas
        mostrarCumple = findViewById(R.id.txtMostrarCumpleaños);
        llamada = findViewById(R.id.imgCall);
        whats = findViewById(R.id.imgSMS);
        //Ocultamos los botones primeramente por si alguien no cumple años
        llamada.setVisibility(View.INVISIBLE);
        whats.setVisibility(View.INVISIBLE);
        //Creamos nuestras instancias a la base de datos, la obtenemos.
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Si la base de datos no esta creada la crea, y si ya esta creada solamente la carga y muestra un toast
            //Obtenemos los valores de la base de datos, de la tabla cumple con un cursor.
            //Los metemos a arreglos de Strings para manipularlos mas facilmente
            //dbHelper = new DbHelper(this);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select *from t_cumpleaños", null);
            if (cursor.getCount() > 0) {
                //Asignamos el largo de los arreglos a la cantidad de la base de datos.
                tam_id = new int[cursor.getCount()];
                nombre = new String[cursor.getCount()];
                telefono = new String[cursor.getCount()];
                fecha = new String[cursor.getCount()];
                int contador = 0;

                //Metemos los valores de la base de datos a los Strings en la posicion correspondiente
                while (cursor.moveToNext()) {
                    tam_id[contador] = cursor.getInt(0);
                    nombre[contador] = cursor.getString(1);
                    fecha[contador] = cursor.getString(2);
                    telefono[contador] = cursor.getString(3);
                    //Aumentamos el contador
                    contador++;
                }
                    //Aqui se obtiene la fecha del dispositivo
                    //Aqui se crea un formater para solo tomar el dia y mes de la fecha, ya que el año no lo necesitamos
                    //Para la comparación.
                    String date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());

                    //Recorremos el arreglo de la fecha para comparar si algun cumple coincide con la fecha de hoy.
                    for(int i=0;i<fecha.length;i++){
                        //SimpleDateFormat para poder formatear y luego poder convertirla.
                        SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            //la formateamos
                            fechaFormatear = entrada.parse(fecha[i]);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        //La cambiamos a solo el dia y el mes.
                        //Luego la asignamos a un string.
                        SimpleDateFormat formater = new SimpleDateFormat("dd/MM");
                        String fechanueva = formater.format(fechaFormatear);

                        //Aqui hacemos la comparacion de si la fecha es igual
                        if(date.equals(fechanueva)){
                            //cambiamos el TextView a que si existe alguien y quien es
                            mostrarCumple.setText("Hoy es el cumpleaños de: " +nombre[i] + "\nDedicale un lindo mensaje :)\nLlamale a esa persona especial ;)");
                            //Hacemos visibles los botones
                            llamada.setVisibility(View.VISIBLE);
                            whats.setVisibility(View.VISIBLE);
                            //Asignamos el numero y el  nombre de la persona a variables para usarlas despues
                            numero = telefono[i];
                            nombrepersona = nombre[i];
                            //Rompemos el ciclo
                            break;
                        }
                    }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        // Primeramente asignamos los recursos del xml a variables creadas
        mostrarCumple = findViewById(R.id.txtMostrarCumpleaños);
        llamada = findViewById(R.id.imgCall);
        whats = findViewById(R.id.imgSMS);


        //Ocultamos los botones primeramente por si alguien no cumple años
        llamada.setVisibility(View.INVISIBLE);
        whats.setVisibility(View.INVISIBLE);

        //Creamos nuestras instancias a la base de datos, la obtenemos.
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Si la base de datos no esta creada la crea, y si ya esta creada solamente la carga y muestra un toast
        //Obtenemos los valores de la base de datos, de la tabla cumple con un cursor.
        //Los metemos a arreglos de Strings para manipularlos mas facilmente
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select *from t_cumpleaños", null);
        if (cursor.getCount() > 0) {
            //Asignamos el largo de los arreglos a la cantidad de la base de datos.
            tam_id = new int[cursor.getCount()];
            nombre = new String[cursor.getCount()];
            telefono = new String[cursor.getCount()];
            fecha = new String[cursor.getCount()];
            int contador = 0;

            //Metemos los valores de la base de datos a los Strings en la posicion correspondiente
            while (cursor.moveToNext()) {
                tam_id[contador] = cursor.getInt(0);
                nombre[contador] = cursor.getString(1);
                fecha[contador] = cursor.getString(2);
                telefono[contador] = cursor.getString(3);
                //Aumentamos el contador
                contador++;
            }
                //Aqui se obtiene la fecha del dispositivo
                //Aqui se crea un formater para solo tomar el dia y mes de la fecha, ya que el año no lo necesitamos
                //Para la comparación.
                String date = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date());

                //Recorremos el arreglo de la fecha para comparar si algun cumple coincide con la fecha de hoy.
                for(int i=0;i<fecha.length;i++){
                    //SimpleDateFormat para poder formatear y luego poder convertirla.
                    SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        //la formateamos
                        fechaFormatear = entrada.parse(fecha[i]);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //La cambiamos a solo el dia y el mes.
                    //Luego la asignamos a un string.
                    SimpleDateFormat formater = new SimpleDateFormat("dd/MM");
                    String fechanueva = formater.format(fechaFormatear);

                    //Aqui hacemos la comparacion de si la fecha es igual
                    if(date.equals(fechanueva)){
                        //cambiamos el TextView a que si existe alguien y quien es
                        mostrarCumple.setText("Hoy es el cumpleaños de: " +nombre[i] + "\nDedicale un lindo mensaje :)\nLlamale a esa persona especial ;)");
                        //Hacemos visibles los botones
                        llamada.setVisibility(View.VISIBLE);
                        whats.setVisibility(View.VISIBLE);
                        //Asignamos el numero y el  nombre de la persona a variables para usarlas despues
                        numero = telefono[i];
                        nombrepersona = nombre[i];
                        //Rompemos el ciclo
                        break;
                    }
                }
        }
    }

    /**
     * Intent para abrir el activity siguiente con el boton.
     * @param view
     */
    public void listarCumple(View view) {
        Intent intent = new Intent(this, CumpleActivity.class);
        startActivity(intent);
    }

    /**
     * Intent para hacer la llamada con el numero de la persona que cumple anios
     * @param view
     */
    public void hacerLlamada(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numero));
        startActivity(intent);
    }


    /**
     * Intent para mandar el mensaje de whatsap predefinido.
     * @param view
     */
    public void mensajeSMS(View view) {
        String mensaje = "Hola "+ nombrepersona +"Te deseo un feliz cumpleaños que te lo pases de lo mejor. :)";
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.setData (Uri.parse ("sms:"+ numero));
        intent.putExtra ("sms_body",mensaje);
        startActivity (intent);
    }
}