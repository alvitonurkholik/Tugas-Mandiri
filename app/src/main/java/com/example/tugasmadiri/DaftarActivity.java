package com.example.tugasmadiri;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DaftarActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    DatabaseHelper databaseHelper;
    List<ModelNote> noteModelList;
    FloatingActionButton fab;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private Button saveButton;
    private EditText titleText, descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUpDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteModelList = new ArrayList<>(); //Instantiating the list.

        databaseHelper = new DatabaseHelper(this);

        noteModelList = databaseHelper.getAllNotes();

        for(ModelNote noteModel : noteModelList)
        {
            Log.d("List", "data: "+noteModel.getTitle());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, noteModelList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopUpDialog()
    {
        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup, null);
//        saveButton = view.findViewById(R.id.saveButton);

        titleText = view.findViewById(R.id.titleText);
        descriptionText = view.findViewById(R.id.descriptionText);

        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(titleText.getText().toString().isEmpty() && descriptionText.getText().toString().isEmpty())
                {
                    Snackbar.make(v, "enter title and description!", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    saveNotes(v);
                }

            }
        });

        builder.setView(view);
        dialog = builder.create(); //creating the dialong
        dialog.show(); //showing the dialog
    }

    private void saveNotes(View v)
    {

        ModelNote noteModel = new ModelNote();

        String titleData = titleText.getText().toString().trim();
        String descriptionData = descriptionText.getText().toString().trim();

        noteModel.setTitle(titleData);
        noteModel.setDescription(descriptionData);

        databaseHelper.addItem(noteModel);
        Snackbar.make(v, "Note Saved!", Snackbar.LENGTH_LONG).show();

        //Removing the dialog after inActivity;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
                startActivity(new Intent(DaftarActivity.this, DaftarActivity.class));
                finish();

            }
        }, 1200); // 1.2 seconds.

    }
}