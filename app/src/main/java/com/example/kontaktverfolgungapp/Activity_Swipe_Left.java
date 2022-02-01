package com.example.kontaktverfolgungapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.kontaktverfolgungapp.dbclient.ClientApp;
import com.example.kontaktverfolgungapp.dbclient.Visit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Activity_Swipe_Left extends AppCompatActivity {

//-------Menu--------------------------------------------------------------------------------

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newcontactpopup_firstname, newcontactpopup_lastname;
    private Button button_cancel, button_save;

//-------List View--------------------------------------------------------------------------------

    ExpandableListView expandableListView;
    List<Visit> places;
    HashMap<String, List<String >> metPeopleAtPlace;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_left);

        expandableListView = findViewById(R.id.expandable_listview);
        places = new ArrayList<>();
        metPeopleAtPlace = new HashMap<>();
        adapter = new MainAdapter(this, places, metPeopleAtPlace);
        expandableListView.setAdapter(adapter);
        //initTestListData();
        initListData();
    }

    private void initListData() {
        SharedPreferences mySPR = getSharedPreferences("Pref", 0);
        int UID = mySPR.getInt("UID", 0);
        ArrayList<Visit> visits = ClientApp.loadVisits(UID);
        if (visits.size() == 0) {
            Toast.makeText(Activity_Swipe_Left.this, "Es wurden keine Visits abgerufen.", Toast.LENGTH_LONG).show();
            return;
        }

        for (Visit visit : visits) {
            int PID = visit.getPID();
            String dateTime = visit.getDateTime();
            String[] metPeopleNames = ClientApp.loadMetPeople(PID, dateTime);
            metPeopleAtPlace.put(visit.getPlaceName(), Arrays.asList(metPeopleNames));
        }
    }

    private void initTestListData() {
        places.add(new Visit(0, "Cinemax", "2021-12-21 13:00:00", 1.5));
        places.add(new Visit(0, "Theater", "2021-12-21 13:00:00", 1.5));
        places.add(new Visit(0, "Mr. Pan", "2021-12-21 13:00:00", 1.5));
        places.add(new Visit(0, "MLU", "2021-12-21 13:00:00", 1.5));
        places.add(new Visit(0, "Irgendwas", "2021-12-21 13:00:00", 1.5));

        String [] placesFromStringRessources;

        List<String> list1 = new ArrayList<>();
        placesFromStringRessources = getResources().getStringArray(R.array.group1);
        for (String item: placesFromStringRessources){
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        placesFromStringRessources = getResources().getStringArray(R.array.group2);
        for (String item: placesFromStringRessources){
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        placesFromStringRessources = getResources().getStringArray(R.array.group3);
        for (String item: placesFromStringRessources){
            list3.add(item);
        }

        List<String> list4 = new ArrayList<>();
        placesFromStringRessources = getResources().getStringArray(R.array.group4);
        for (String item: placesFromStringRessources){
            list4.add(item);
        }

        List<String> list5 = new ArrayList<>();
        placesFromStringRessources = getResources().getStringArray(R.array.group5);
        for (String item: placesFromStringRessources){
            list5.add(item);
        }

        metPeopleAtPlace.put(places.get(0).getPlaceName(),list1);
        metPeopleAtPlace.put(places.get(1).getPlaceName(),list2);
        metPeopleAtPlace.put(places.get(2).getPlaceName(),list3);
        metPeopleAtPlace.put(places.get(3).getPlaceName(),list4);
        metPeopleAtPlace.put(places.get(4).getPlaceName(),list5);

        adapter.notifyDataSetChanged();

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
                        
                        // database integrated for setName
                         int UID = mySPR.getInt("UID", 0);
                        if (UID != 0) {
                            try {
                                ClientApp.setName(UID, n + ";" + ph + ";");
                            } catch (IOException e) {
                                Toast.makeText(Activity_Swipe_Left.this, "Ihr Name konnte nicht auf dem Server geändert werden.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Activity_Swipe_Left.this, "Ihre UID konnte nicht abgerufen werden.", Toast.LENGTH_LONG).show();
                        }
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