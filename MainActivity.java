package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Note> adp;
    private Integer sel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button edit = findViewById(R.id.but_edit);
        Button delete = findViewById(R.id.but_delete);

        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1);

        ListView lst = findViewById(R.id.lst_notes);
        lst.setAdapter(adp);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                sel = position; //запись позиции нажатия на элемент
                edit.setEnabled(true);
                delete.setEnabled(true);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (data != null && resultCode == RESULT_OK) { //получение переданных данных
            int pos = data.getIntExtra("my-note-index", -1);
            String title = data.getStringExtra("my-note-title");
            String content = data.getStringExtra("my-note-content");

            if (pos == -1) {                                                                        //для новой записи
                Note n = new Note();
                n.title = title;
                n.content = content;
                adp.add(n);
            } else {
                com.example.notes.Note n = adp.getItem(pos);
                n.title = title;
                n.content = content;
            }
            adp.notifyDataSetChanged();                                                             //обновить list box
        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    public void on_new_click(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);                         //создание намерения перехода на 2ю activity

    startActivityForResult(i,12345);

    }
    public void on_edit_click(View view){
        Intent i = new Intent(getApplicationContext(), com.example.notes.MainActivity2.class);
        i.putExtra("my-note-index", sel/*передача индакса выбранного элемента*/); //share note data with new activity
        i.putExtra("my-note-title", adp.getItem(sel).title);
        i.putExtra("my-note-content", adp.getItem(sel).content);

        startActivityForResult(i, 12345); //show note editing activity

    }
    public void on_delete_click(View view){
        createDialog().show();
    }
    //Метод для вывода DialogWindow
    @NonNull
    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete the note?")
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        adp.remove(adp.getItem(sel));
                        adp.notifyDataSetChanged(); //update list box
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}