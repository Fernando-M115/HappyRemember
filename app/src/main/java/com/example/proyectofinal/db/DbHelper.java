package com.example.proyectofinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {

    //VARIABLES LOCALES

    //VERSION DE LA BASE DE DATOS
    private static final int DATABASE_VERSION = 3;
    //NOMBRE DE LA BASE DE DATOS
    private static final String DATABASE_NOMBRE = "cumpleaños.db";
    //NOMBRE DE LA TABLA.
    private static final String TABLE_CUMPLE = "t_cumpleaños";

    //contexto
    Context context;
    //Constructor que nos regresa el contexto
    public DbHelper(Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.context = context;
    }

    //Metodo para crear la base de datos.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CUMPLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "cumple TEXT NOT NULL," +
                "telefono TEXT NOT NULL)");
    }

    //Metodo para dropear la tabla.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CUMPLE);
        onCreate(sqLiteDatabase);
    }

    //Metodo para insertar el cumpleaniero obteniendo el nombre, telefono, y la fecha en string
    public long insertarCumpleañero(String nombre, String telefono, String cumple){
        long result = 0;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombre",nombre);
            values.put("cumple", cumple);
            values.put("telefono",telefono);
            result = db.insert(TABLE_CUMPLE, null, values);
        return result;
    }
}
