package com.example.giovanny.fuelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class fuelRecords extends Activity implements View.OnClickListener {

    EditText etDate, etOdometro, etTotalPrice, etLiters, etLiterPrice, etNotes;
    Button btnOk, btnCancel;
    final Context context = this;
    String currentDateTimeString;
    Bundle b;
    String itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_records);

        etDate = (EditText) findViewById(R.id.txtDate);
        etOdometro = (EditText) findViewById(R.id.txtOdometro);
        etOdometro.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etTotalPrice  = (EditText) findViewById(R.id.txttotalPrice);
        etTotalPrice.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etLiters  = (EditText) findViewById(R.id.txtLiters);
        etLiters.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etLiterPrice  = (EditText) findViewById(R.id.txtliterPrice);
        etLiterPrice.setRawInputType(Configuration.KEYBOARD_QWERTY);
        etNotes = (EditText) findViewById(R.id.txtNote);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        etDate.setText(currentDateTimeString);

        Intent i = getIntent();
        b = i.getExtras();
        itemPos = b.getString("idItem");

        //main = new MainActivity();

        /*Toast.makeText(getApplicationContext(), "Seleccionado: "+position,
            Toast.LENGTH_SHORT).show();*/
    }

    /*Insert a new car record in the DB*/
    public void insertRecord() {
        DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        if (etOdometro.getText().toString().trim().length() == 0 ||
            etTotalPrice.getText().toString().trim().length() == 0 ||
            etLiters.getText().toString().trim().length() == 0 ||
            etLiterPrice.getText().toString().trim().length() == 0) {

            alertOk("Campos vacios!");

        } else {

            Integer odometro = Integer.parseInt(etOdometro.getText().toString());
            Integer totalPrice = Integer.parseInt(etTotalPrice.getText().toString());
            Integer liters = Integer.parseInt(etLiters.getText().toString());
            Integer literPrice = Integer.parseInt(etLiterPrice.getText().toString());
            String notes = etNotes.getText().toString();

        if (odometro < 0 || odometro > 200000) {//Validating the maximun and minimun tank capacity
                alertOk("Dato odometro invalido");
        }
        else if(totalPrice < 20 || totalPrice > 1000 ){
                alertOk("Dato precio total invalido");

        }
        else if(liters < 1 || liters > 70 ){
                alertOk("Dato cantidad de litros invalido");

        }
        else if(literPrice < 9 || literPrice > 15 ){
                alertOk("Dato precio por litro invalido");

        }
        else {

            ContentValues record = new ContentValues();
            record.put("vid", itemPos);
            record.put("date", currentDateTimeString);
            record.put("odometro", odometro);
            record.put("totalPrice", totalPrice);
            record.put("liters", liters);
            record.put("literprice", literPrice);
            record.put("notes", notes);
            db.insert("fuel_records", null, record);
            db.close();
            clearText();

                Toast.makeText(getApplicationContext(), "Registro agregado!",
                        Toast.LENGTH_SHORT).show();
                clearText();
            }
        }
    }
    public void viewRecord(){
        DatabaseHandler admin = new DatabaseHandler(this,"vehiclesDB2", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String idItem = null;
        Cursor c = db.rawQuery("SELECT date, odometro FROM vehicles WHERE rowid="+itemPos, null);

        while(c.moveToNext())
        {
            idItem = c.getString(0).toString();
        }
        Toast.makeText(getApplicationContext(), "Seleccionado: "+idItem,
                       Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fuel_records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
    switch(id) {
        case R.id.btnOk:
            insertRecord();
            break;
        case R.id.btnCancel:

            //Toast.makeText(getApplicationContext(), "Seleccionado: "+itemPos,
                //Toast.LENGTH_SHORT).show();

            //viewRecord();
            this.finish();
            break;
        }
    }
    public void clearText(){
        etOdometro.setText("");
        etTotalPrice.setText("");
        etLiters.setText("");
        etLiterPrice.setText("");
        etNotes.setText("");
    }
    public void alertOk (String message) { //Alert only with Ok answer
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
