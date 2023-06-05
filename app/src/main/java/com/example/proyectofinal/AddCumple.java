package com.example.proyectofinal;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectofinal.db.DbHelper;
import java.text.ParseException;
import java.util.Calendar;

public class AddCumple extends AppCompatActivity {

    //Variables declaradas para usarlas despues
    private EditText mostrarFecha, nombre, telefono, fechaObtener;
    int dia,mes,year,bandera = 0,id;

    Button botonEditar;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cumple);
        //Asignar los correspondientes ids a las variables
        mostrarFecha = findViewById(R.id.txtFecha);
        nombre = findViewById(R.id.txtNombreCompleto);
        telefono = findViewById(R.id.txtTelefono);
        fechaObtener = findViewById(R.id.txtFecha);
        botonEditar = findViewById(R.id.btnAdd);
        //cambiar el texto del boton a aniadir
        botonEditar.setText("AÑADIR");

        //Si recibimos un intent para el update
        //carga los datos del bundle
        if (getIntent().getBundleExtra("data")!=null){
            Bundle bundle = getIntent().getBundleExtra("data");
            id = bundle.getInt("id");
            nombre.setText(bundle.getString("nombrecompleto"));
            telefono.setText(bundle.getString("telefono"));
            mostrarFecha.setText(bundle.getString("fecha"));
            //y solo cambia el boton a editar y activa una bandera
            botonEditar.setText("EDITAR");
            bandera=1;
        }
    }

    //Metodo para obtener la fecha que selecciona en el calendario
    public void Fecha(View view) {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mostrarFecha.setText(day+"/"+(month+1)+"/"+year);
            }
        },year,mes,dia);
        datePickerDialog.show();
    }

    //Metodo del boton para aniadir a la base de datos
    public void subir_Cumple(View view) throws ParseException {
        //Obtenemos la base de datos
        dbHelper = new DbHelper(this);
        //Obtenemos los correspondientes strings de los edittext.
        String nombre = this.nombre.getText().toString();
        String telefono = this.telefono.getText().toString();
        String fecha = this.fechaObtener.getText().toString();
        //Try
        try{
            //If para validar los campos
            if(telefono.equals("")  || fecha.equals("")){
                //Si estan vacios lanza la excepcion
                throw new ErrorCamposException();
            }else{
                //Si los campos estan validados y la bandera esta activa
                if(bandera == 1){
                    //Metemos los valores a un content values
                    ContentValues cv = new ContentValues();
                    cv.put("nombre",nombre);
                    cv.put("telefono",telefono);
                    cv.put("cumple",fecha);

                    //LLamamos al update a la base de datos y le manamos los content values
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    long update = sqLiteDatabase.update("t_cumpleaños",cv,"id="+id,null);
                    if(update!=-1){
                        //Toast para saber si se edito
                        Toast.makeText(this, "Editado Correctamente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Si la bandera no es 1 solamente lo aniade a la base de datos como nuevo registro.
                    long result = dbHelper.insertarCumpleañero(nombre,telefono,fecha);
                    if(result > 0){
                        Toast.makeText(this, "SE AGREGO CORRECTAMENTE A LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                    else{
                        Toast.makeText(this, "ERROR AL GUARDAR", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }catch (ErrorCamposException e) {
            if(nombre.equals("")){
                this.nombre.setError("Nombre Invalido");
                this.nombre.requestFocus();
            }
            if(telefono.equals("")){
                //Si estan vacios lanza la excepcion
                this.telefono.setError("Telefono Invalido");
                this.telefono.requestFocus();
            }
            if(fecha.equals("")){
                mostrarFecha.setError("Debe seleccionar una fecha");
                mostrarFecha.requestFocus();
            }
        }
    }

    /**
     * Metodo para limpiar los campos
     */
    private void limpiarCampos() {
        nombre.setText("");
        telefono.setText("");
        fechaObtener.setText("");
    }
}
