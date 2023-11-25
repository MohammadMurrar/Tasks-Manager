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

public class DueTasksActivity extends AppCompatActivity {
    public static ArrayList<Task> dueTasksList;
    public static ArrayAdapter<Task> adapter;
    private ListView lvDueTasks;
    private FloatingActionButton fabBack;
    private FloatingActionButton fabAddTask2;

    private SharedPreferences prefs; //read
    private static SharedPreferences.Editor editor; //write
    public static int POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_tasks);

        setupSharedPrefs();
        Intent intent = getIntent();

        dueTasksList = loadTasksFromSP();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dueTasksList);
        adapter.notifyDataSetChanged();
        ListView listView = findViewById(R.id.lvDueTasks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = dueTasksList.get(position);
                POSITION = position;
                Intent intent = new Intent(DueTasksActivity.this, CheckTasks.class);
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
                finish();
            }
        });

        fabAddTask2 = findViewById(R.id.fabAddTask2);
        fabAddTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DueTasksActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void removeDueTask(int position){
        if(position >= 0 && position < dueTasksList.size()){
            dueTasksList.remove(position);
            adapter.notifyDataSetChanged();
            storeTasksList();
        }
    }

    private static void storeTasksList(){
        Gson gson = new Gson();
        String dueTasksAsJSON = gson.toJson(DueTasksActivity.dueTasksList);
        editor.putString(AddTaskActivity.TASKS_LIST,dueTasksAsJSON);
        editor.commit();
    }

    private void setupSharedPrefs(){
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private ArrayList<Task> loadTasksFromSP(){
        Gson gson = new Gson();
        String tasksAsJSON = prefs.getString(AddTaskActivity.TASKS_LIST,"");
        if(!tasksAsJSON.isEmpty()){
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            return gson.fromJson(tasksAsJSON, type);
        }
        return new ArrayList<>();
    }
}