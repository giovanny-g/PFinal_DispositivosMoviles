package com.example.giovanny.fuelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner spinVehiculos;
    ImageView imgAdd, imgEdit, imgDelete;
    final Context context = this;
    /*Objects and variables used in custom dialog*/
    Button btn_addFuel, btnRecords, btnCancel, btnOk;
    EditText etName, etTank;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinVehiculos = (Spinner) findViewById(R.id.spinVehiculos);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgDelete = (ImageView) findViewById(R.id.imgDelete);
        btn_addFuel = (Button) findViewById(R.id.btnAddFuel);
        btnRecords = (Button) findViewById(R.id.btnRecords);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);

        btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        etName = (EditText) dialog.findViewById(R.id.txtNombre);
        etTank = (EditText) dialog.findViewById(R.id.txtTanque);
        etTank.setRawInputType(Configuration.KEYBOARD_QWERTY);

        imgAdd.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        btn_addFuel.setOnClickListener(this);
        btnRecords.setOnClickListener(this);

        LoadSpinnerData();
    }

    @Override
    public void onClick(View v) {
            int id = v.getId();
        switch (id) {

            case R.id.imgAdd:
                newVehicle();
                LoadSpinnerData();
                break;
            case R.id.imgDelete:
                DeleteVehicle();
                break;
            case R.id.imgEdit:
                EditVehicle();
                break;
            case R.id.btnAddFuel:
                InsertFuelRecord();
                break;
            case R.id.btnRecords:
                ViewRecords();
                break;
        }
    }
    public void LoadEditData() {
        DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String itemSelected = spinVehiculos.getSelectedItem().toString();

        Cursor c = db.rawQuery("SELECT name, tank FROM vehicles WHERE name='" +itemSelected+"'", null);

        while(c.moveToNext()){
            etName.setText(c.getString(0).toString());
            etTank.setText(c.getString(1).toString());
        }
    }
    /**
     * EDIT THE VEHICLE RECORD DATA
     */
    public void EditVehicle(){

        CustomDialogEdit("Editar Vehiculo");

    }
    public void Edit() {
        DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String itemSelected = spinVehiculos.getSelectedItem().toString();
        Integer id = null;
        String vName = null, vTank = null;
        Cursor c = db.rawQuery("SELECT id, name, tank FROM vehicles WHERE name='" + itemSelected + "'", null);

        while (c.moveToNext()) {
            id = Integer.parseInt(c.getString(0));
        }

        vName = etName.getText().toString();
        vTank = etTank.getText().toString();

        Cursor c2 = db.rawQuery("SELECT name FROM vehicles WHERE name='" + vName + "'", null);

        if (c2.getCount() > 0) {
            alertExist();//Validating that the record doesn't exist yet
        } else{

        ContentValues record = new ContentValues();
        record.put("name", vName);
        record.put("tank", vTank);
        int cant = db.update("vehicles", record, "id=" + id, null);
        db.close();

        if (cant == 1)
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT).show();

        //clearText();
        LoadSpinnerData();
        }
    }
    /**
     * VIEW ALL FUEL RECORDS OF A CAR IN CLASS showRecords.class
     */
    public void ViewRecords() {//Form to view all fuel records of a car
            String itemName;
            DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            itemName = spinVehiculos.getSelectedItem().toString();
            String idItem = null;
            Cursor c = db.rawQuery("SELECT rowid FROM vehicles WHERE name='" + itemName + "'", null);

        while (c.moveToNext()) {
            idItem = c.getString(0).toString();
        }
            Intent intent1 = new Intent(getApplicationContext(),
                showRecords.class).putExtra("itemPos", idItem);
            startActivity(intent1);
    }
    /**
     * INSERT A NEW FUEL RECORD IN THE DB
     */
    public void InsertFuelRecord() {
            String newItem;
            DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            newItem = spinVehiculos.getSelectedItem().toString();
            String idItem = null;
            Cursor c = db.rawQuery("SELECT rowid FROM vehicles WHERE name='" + newItem + "'", null);

        while (c.moveToNext()) {
            idItem = c.getString(0).toString();
        }
            /*Toast.makeText(getApplicationContext(), "Seleccionado: " + newItem,
                           Toast.LENGTH_SHORT).show();*/

            Intent intent1 = new Intent(getApplicationContext(),
                                        fuelRecords.class).putExtra("idItem", idItem);
            startActivity(intent1);
    }
    /**
     * INSERT A NEW VEHICLE IN THE DB
     */
    public void Insert() {
            DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT name FROM vehicles WHERE name='" + etName.getText().toString() + "'", null);

        if (c.getCount() > 0) {
            alertExist();//Validating that the record doesn't exist yet
        } else {

        if (etName.getText().toString().trim().length() == 0 ||
            etTank.getText().toString().trim().length() == 0) {

                alertOk("Campos vacios");

                //return;
        } else {

            String vName = etName.getText().toString();
            Integer vTank = Integer.parseInt(etTank.getText().toString());

        if (vTank < 10 || vTank > 100) {//Validating the maximun and minimun tank capacity
                    alertOk("El valor de tanque no es correcto");
        } else {
            ContentValues record = new ContentValues();
            record.put("name", vName);
            record.put("tank", vTank);
            db.insert("vehicles", null, record);
            db.close();
            clearText();

            LoadSpinnerData();

            Toast.makeText(getApplicationContext(), "Vehiculo agregado!",
                           Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /**
     * FILL SPINNER WITH ALL RECORDS IN THE DB
     */
    private void LoadSpinnerData() {
            DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            List<String> lables = new ArrayList<String>();
            Cursor c = db.rawQuery("SELECT name FROM vehicles", null);

        while (c.moveToNext()) {

            lables.add(c.getString(0));
        }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                                 R.layout.spinner_style, lables);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinVehiculos.setAdapter(dataAdapter);
    }
    /**
     * DELETE A VEHICLE/ITEM SELECTED IN THE SPINNER
     */
    private void DeleteVehicle() {
            String deleteItem;
        if (spinVehiculos.getSelectedItem().toString().trim().equals("")) {
            alertOk("Sin registros para eliminar");

        } else{
            deleteItem = spinVehiculos.getSelectedItem().toString();
            alertPosNeg("ELIMINAR el registro: [ "+deleteItem+" ] ?", deleteItem);//I sent the message and item to delete
        }
    }
    public void Delete(String item){
            DatabaseHandler admin = new DatabaseHandler(this, "vehiclesDB2", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT id FROM vehicles WHERE name='" +item+ "'", null);

        while(c.moveToNext()){

            db.execSQL("DELETE FROM fuel_records WHERE vid='" + c.getString(0) + "'");
            Toast.makeText(getApplicationContext(), "Registro eliminado",
                           Toast.LENGTH_SHORT).show();

        }
            db.execSQL("DELETE FROM vehicles WHERE name='" +item+ "'");
            LoadSpinnerData();
    }
    /**
     *CALLING THE DIALOG FORM TO INSERT A NEW VEHICLE
     **/
    public void newVehicle () {

       CustomDialogInsert("Nuevo vehiculo");

    }
    public void CustomDialogInsert(String title){
            dialog.setTitle("Añadir vehiculo");

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Insert();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearText();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    public void CustomDialogEdit(String title){

        dialog.setTitle(title);
        LoadEditData();//Loading data from spinner item selected

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Edit(); //Updating data of the record to edit
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    /**
     * ALERT MESSAGES
     */
    public void alertPosNeg(String message, String item){//Alert Positive/Negative
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            final String itemA = item;

            builder1.setMessage(message);
            builder1.setCancelable(true);
            builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Delete(itemA);
                    }
                });
            builder1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
            AlertDialog alert11 = builder1.create();
            alert11.show();
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
        public void alertExist () {//Alert to verify if a record already exist
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("El auto ya existe");
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
        public void clearText () {

            etName.setText("");
            etTank.setText("");
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
