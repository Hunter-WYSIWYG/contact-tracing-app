package com.example.kontaktverfolgungapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Activity_Swipe_Left extends AppCompatActivity {

//-------Gesture--------------------------------------------------------------------------------

    private float x1,x2;
    static final int MIN_DISTANCE = 50;


//-------Menu--------------------------------------------------------------------------------

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newcontactpopup_firstname, newcontactpopup_lastname;
    private Button button_cancel, button_save;

//-------List View--------------------------------------------------------------------------------

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String >> listItem;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_left);

        expandableListView = findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        initListData();
    }

    private void initListData() {
        listGroup.add(getString(R.string.group1));
        listGroup.add(getString(R.string.group2));
        listGroup.add(getString(R.string.group3));
        listGroup.add(getString(R.string.group4));
        listGroup.add(getString(R.string.group5));

        String [] array;

        List<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group1);
        for (String item: array){
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group2);
        for (String item: array){
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group3);
        for (String item: array){
            list3.add(item);
        }

        List<String> list4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group4);
        for (String item: array){
            list4.add(item);
        }

        List<String> list5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group5);
        for (String item: array){
            list5.add(item);
        }

        listItem.put(listGroup.get(0),list1);
        listItem.put(listGroup.get(1),list2);
        listItem.put(listGroup.get(2),list3);
        listItem.put(listGroup.get(3),list4);
        listItem.put(listGroup.get(4),list5);

        adapter.notifyDataSetChanged();

    }

//-------Gesture--------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Right to Left swipe action
                    if (x2 < x1)
                    {
                        Intent i = new Intent(Activity_Swipe_Left.this, MainActivity.class);
                        startActivity(i);
                    }

                }

                break;
        }
        return super.onTouchEvent(event);
    }

//-------Menu--------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1){
            createNewContactDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewContactDialog (){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        newcontactpopup_firstname = (EditText) contactPopupView.findViewById(R.id.newcontactpopup_firstname);
        newcontactpopup_lastname = (EditText)  contactPopupView.findViewById(R.id.newcontactpopup_lastname);

        button_save = (Button) contactPopupView.findViewById(R.id.saveButton);
        button_cancel = (Button) contactPopupView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        //Shared Pref Datei öffnen
        SharedPreferences mySPR = getSharedPreferences("Pref", 0);
        //gespeicherte Namen aus der Datei laden
        newcontactpopup_firstname.setText(mySPR.getString("vornameKey", ""));
        newcontactpopup_lastname.setText(mySPR.getString("nachnameKey", ""));


        button_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    String n = newcontactpopup_firstname.getText().toString();
                    String ph = newcontactpopup_lastname.getText().toString();
                    //überprüfen ob Vorname-Eingabe leer ist
                    if(newcontactpopup_firstname.length()==0){
                        newcontactpopup_firstname.setError("Vorname eingeben");
                    }
                    //überprüfen ob sonderzeichen enthaletn sind
                    else if(!n.matches("[a-zA-z]+")){

                        newcontactpopup_firstname.setError("Es sind nur Buchstaben erlaubt!");
                    }
                    //überprüfen ob Nachname-Eingabe leer ist
                    else if(newcontactpopup_lastname.length()==0){
                        newcontactpopup_lastname.setError("Nachname eingebn");
                    }
                    //überprüfen ob sonderzeichen enthaletn sind
                    else if(!ph.matches("[a-zA-z]+")){

                        newcontactpopup_lastname.setError("Es sind nur Buchstaben erlaubt!");
                    }
                    else{
                        //Shared Pref Datei öffnen
                        SharedPreferences mySPR = getSharedPreferences("Pref", 0);
                        //Editor Klasse initiaisieren
                        SharedPreferences.Editor editor = mySPR.edit();

                        editor.putString("vornameKey", n);
                        editor.putString("nachnameKey", ph);
                        //speichern
                        editor.commit();
                        //schließen
                        dialog.dismiss();}

                }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



}