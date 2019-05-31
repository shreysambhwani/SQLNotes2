package com.example.mycontactapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE ="com.example.mycontactapp.MESSAGE";  ;
    DatabaseHelper myDb;
    EditText editName;
    EditText editAddress;
    EditText editPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editAddress = findViewById(R.id.editText_address);
        editPhone = findViewById(R.id.editText_phone);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated myDb");
    }

    public void addData(View view){
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editAddress.getText().toString(), editPhone.getText().toString());

        if (isInserted == true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view){
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor");

        if (res.getCount() == 0){
            showMessage("Error", "No data found in database");
            Log.d("MyContactApp", "MainActivity: viewData: no data in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){

            for (int i = 0; i < 4; i++){
                buffer.append(res.getString(i));
                buffer.append("\n");
            }
            buffer.append("\n");
            Log.d("MyContactApp", "MainActivity: viewData: created StringBuffer");
            //Append res column 0,1,2,3 to the buffer - see Stringbuffer and Cursor's api's
            //Delimit each of the "appends" with line feed "\n"
            //four lines
        }
        showMessage("Data", buffer.toString());
    }

    private void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showMessage: assembling AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void searchRecord(View view) {
        Log.d("MyContactApp", "MainActivity: launching search");
        Cursor curs = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        //Intent intent = new Intent(this, SearchActivity.class);
        if (editName.getText().toString().isEmpty() && editPhone.getText().toString().isEmpty()
                && editAddress.getText().toString().isEmpty()) {
            showMessage("Error", "Nothing to search for!");
            return;
        }

        while (curs.moveToNext()){
            if ((editName.getText().toString().isEmpty() || editName.getText().toString().equals(curs.getString(1)))
                    && (editPhone.getText().toString().isEmpty() || editPhone.getText().toString().equals(curs.getString(2)))
                    && (editAddress.getText().toString().isEmpty() || editAddress.getText().toString().equals(curs.getString(3))))
            {
                buffer.append("ID: " + curs.getString(0) + "\n" +
                        "Name: " + curs.getString(1) + "\n" +
                        "Phone number: " + curs.getString(2) + "\n" +
                        "Home address: " + curs.getString(3) + "\n\n");
            }
        }
        if (buffer.toString().isEmpty()) {
            showMessage("Error", "No matches found");
            return;
        }
        showMessage("Search results", buffer.toString());
    }
    public void clearContacts(View view) {
        myDb.deleteData();
    }
}







