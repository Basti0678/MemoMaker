package com.example.florian.memomaker.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Studium on 20.02.2016.
 */
public class NeueTodoActivity extends AppCompatActivity{

    //DB
    SQLiteDatabase mydb;
    private static String DBMEMO = "memomaker.db";
    private static String TABLE = "mmdata";

    private String text;
    EditText editText;

    RadioGroup radioPrioGroup;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;

    boolean checkedA;
    boolean checkedB;
    boolean checkedC;

    public static final String MY_PREFS_NAME = "InstantSaveTodo";


    private void updateProperties(){

        editText = (EditText)findViewById(R.id.newTodo);
        text = editText.getText().toString();

        radioButtonA = (RadioButton)findViewById(R.id.radioButton1);
        checkedA = radioButtonA.isChecked();

        radioButtonB = (RadioButton)findViewById(R.id.radioButton2);
        checkedB = radioButtonB.isChecked();

        radioButtonC = (RadioButton)findViewById(R.id.radioButton3);
        checkedC = radioButtonC.isChecked();

    }//Ende updateProperties


    private void saveSettings(){
        updateProperties();

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();

        edit.putString("KEY_TEXT", text);

        edit.putBoolean("PRIO_A", checkedA);
        edit.putBoolean("PRIO_B", checkedB);
        edit.putBoolean("PRIO_C", checkedC);
        //Datum
        //edit.putString("KEY_PRIO", textPrio);
        edit.commit();
    }//Ende saveSettings


    private void loadSettings(){

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        text = shared.getString("KEY_TEXT","");

        checkedA =shared.getBoolean("PRIO_A", false);
        checkedB =shared.getBoolean("PRIO_B", false);
        checkedC =shared.getBoolean("PRIO_C", false);

        if (checkedA) {
            radioButtonA.setChecked(true);
        }
        else if (checkedB) {
            radioButtonB.setChecked(true);
        }
        else {
            radioButtonC.setChecked(true);
        }

        editText = (EditText) findViewById(R.id.newTodo);
        editText.setText(text);


    }//Ende loadSettings


    @Override
    public void onPause(){
        super.onPause();
        saveSettings();
    }


    @Override
    public void onResume(){
        super.onResume();
        loadSettings();
    }


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_new_todoitem);

        editText = (EditText) findViewById(R.id.newTodo);
        radioPrioGroup=(RadioGroup)findViewById(R.id.id_radio_group1);

        radioButtonA = (RadioButton)findViewById(R.id.radioButton1);
        radioButtonB = (RadioButton)findViewById(R.id.radioButton2);
        radioButtonC = (RadioButton)findViewById(R.id.radioButton3);

        loadSettings();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProperties();
                saveSettings();
            }
        });

    }//Ende onCreate

    public void insertIntoTable(char prio, String text) {
        try {
            mydb = openOrCreateDatabase(DBMEMO, Context.MODE_PRIVATE, null);
            mydb.execSQL("INSERT INTO " + TABLE + " (TYPE, DATEMEMO, PRIORITY, DESCRIPTION, ARCHIVE) " +
                    "VALUES('todo', '' , '" + prio + "', '" + text + "', 0)");

            mydb.close();

            Toast.makeText(getApplicationContext(), "Eintrag gespeichert", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Fehler beim Erstellen der Datenbank", Toast.LENGTH_LONG).show();
        }
    }//Ende instertIntoTable

    public void save (View v){
        saveSettings();
        char prio = ' ';

        if (radioButtonA.isChecked()) {
            prio = 'A';
        }
        else if (radioButtonB.isChecked()) {
            prio = 'B';
        }
        else {
            prio = 'C';
        }

        insertIntoTable(prio, text);
        finish();
    }

}

