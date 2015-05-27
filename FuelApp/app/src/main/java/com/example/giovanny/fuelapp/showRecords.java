package com.example.giovanny.fuelapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class showRecords extends ActionBarActivity {
    ListView lvRecords;
    Bundle b;
    String itemPos;
    String lblFecha, lblOdometro = "ODOMETRO: ",
           lblTotaPrice = "PRECIO TOTAL: $", lblLitros = "LITROS: ",
           lblPrecioLitro = "PRECIOLITRO: $", lblPrecio = "NOTAS: ",
           lblKML = "KM/L: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        lvRecords = (ListView) findViewById(R.id.lvRecords);

        Intent i = getIntent();
        b = i.getExtras();
        itemPos = b.getString("itemPos");

        allFuelRecords();

        lblFecha = getResources().getString(R.string.Date);

    }
    //Method (View All) to fill the spinner with vehicle names
    private void allFuelRecords(){
        DatabaseHandler admin = new DatabaseHandler(this,"vehiclesDB2", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        List<String> lables = new ArrayList<String>();
        int i = 1;

        Cursor c = db.rawQuery("SELECT * FROM fuel_records WHERE vid='"+itemPos+"'", null);
        while(c.moveToNext()){

        int[] odometro = new int[15];
        int[] litros = new int[15];
        odometro[i] = c.getInt(3);
        litros[i] = c.getInt(5);
        float rendimiento = (odometro[i]-5000)/(litros[i]); //500 <---> debe ser odometro [i-1]

        Toast.makeText(getApplicationContext(), "Rendimiento: "+String.valueOf(rendimiento),
                            Toast.LENGTH_SHORT).show();

       lables.add(lblFecha+ c.getString(2) +" || "+lblOdometro+  c.getString(3)+
                  " || "+lblTotaPrice+c.getString(4) +" || "+lblLitros+c.getString(5) +" || "+
                  lblPrecioLitro+c.getString(6) +" || "+lblPrecio+c.getString(7) +" || "+lblKML+rendimiento);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                           R.layout.lv_item_text, lables);
                            lvRecords.setAdapter(adapter);

        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //startActivity(new Intent(getApplicationContext(), recordDetails.class).putExtra("recordID", position));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_records, menu);
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
}
