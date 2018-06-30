package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.content.ContentValues.TAG;

public class CreateContactAcitivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Button submitButton;
    private EditText nameField, numberField, addressField;
    private Spinner businessSpinner, provinceSpinner;
    private MyApplicationData appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact_acitivity);
        //Get the app wide shared variables
        appState = ((MyApplicationData) getApplicationContext());

        submitButton = (Button) findViewById(R.id.submitButton);
        nameField = (EditText) findViewById(R.id.name);
        numberField = (EditText) findViewById(R.id.businessNumber);
        addressField = (EditText) findViewById(R.id.address);

        businessSpinner = (Spinner) findViewById(R.id.primaryBusiness_spinner);
        provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        ArrayAdapter<CharSequence> businessAdapter = ArrayAdapter.createFromResource(this,
                R.array.primaryBusiness_list, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,
                R.array.province_list, android.R.layout.simple_spinner_dropdown_item);
        businessSpinner.setAdapter(businessAdapter);
        provinceSpinner.setAdapter(provinceAdapter);
        businessSpinner.setOnItemSelectedListener(this);
        provinceSpinner.setOnItemSelectedListener(this);
    }

    public void submitInfoButton(View v) {
        //each entry needs a unique ID
        String personID = appState.firebaseReference.push().getKey();
        String name = nameField.getText().toString();
        String number = numberField.getText().toString();
        String primaryBusiness = "";
        String province = "";
        if(businessSpinner.getSelectedItemPosition() != 0) {
            primaryBusiness = String.valueOf(businessSpinner.getSelectedItem());
        }
        String address = addressField.getText().toString();
        if(provinceSpinner.getSelectedItemPosition() != 0) {
            province = String.valueOf(provinceSpinner.getSelectedItem());
        }
        Contact person = new Contact(personID, name, number, primaryBusiness, address, province);

        appState.firebaseReference.child(personID).setValue(person)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Log.d(TAG, "Write Successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        String error = e.getMessage() + ": " + e.getCause();
                        Log.d(TAG, "Write Failed: " + error);
                    }
                });
        appState.firebaseReference.child(personID).setValue(person);
        finish();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
