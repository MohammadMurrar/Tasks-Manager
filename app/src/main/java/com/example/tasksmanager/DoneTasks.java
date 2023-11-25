package com.example.tasksmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class DoneTasks extends AppCompatActivity {
    public static final String DONE_TASKS_LIST = "DONE_TASKS_LIST";
    public static ArrayList<Task> doneTasksList;
    public static ArrayAdapter<Task> doneAdapter;
    private ListView lvDoneTasks;
    private FloatingActionButton fabBack;
    private SharedPreferences prefs; //read
    private static SharedPreferences.Editor editor; //write
    public static int POSITION2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_tasks);

        setupSharedPrefs();

        Intent intent = getIntent();

        doneTasksList = loadTasksFromLS();


        doneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doneTasksList);
        doneAdapter.notifyDataSetChanged();

        lvDoneTasks = findViewById(R.id.lvDoneTasks);
        ListView listView = findViewById(R.id.lvDoneTasks);
        listView.setAdapter(doneAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = doneTasksList.get(position);
                POSITION2 = position;
                Intent intent = new Intent(DoneTasks.this, DeleteDoneTasks.class);
                intent.putExtra("title",selectedTask.getTitle());
                intent.putExtra("description",selectedTask.getDescription());
                intent.putExtra("location",selectedTask.getLocation());
                intent.putExtra("due",selectedTask.getDue());
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });

        fabBack = findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoneTasks.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setupSharedPrefs(){
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public static void removeDoneTask(int position){
        if(position >= 0 && position < doneTasksList.size()){
            doneTasksList.remove(position);
            doneAdapter.notifyDataSetChanged();
            storeTasksList();
        }
    }

    private static void storeTasksList(){
        Gson gson = new Gson();
        String tasksAsJSON = gson.toJson(doneTasksList);
        editor.putString(DONE_TASKS_LIST,tasksAsJSON);
        editor.commit();
    }

    public ArrayList<Task> loadTasksFromLS(){
        Gson gson = new Gson();
        String tasksAsJSON = prefs.getString(DONE_TASKS_LIST,"");
        if(!tasksAsJSON.isEmpty()){
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            return gson.fromJson(tasksAsJSON, type);
        }
        return new ArrayList<>();
    }
}