package com.example.tasksmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TASKS_LIST = "TASKS_LIST";
    private EditText edtTaskTitle;
    private EditText edtTaskDescription;
    private EditText edtTaskLocation;
    private CalendarView calendarView_DueTask;
    private Date selectedDate;
    private Button btnAddTasks;
    private Button btnTasks;
    private FloatingActionButton fabBack;
    private SharedPreferences prefs; //read
    private SharedPreferences.Editor editor; //write

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setUpViews();
        setUpSharedPrefs();

        Intent intent = getIntent();

        DueTasksActivity.dueTasksList = loadTasksFromLS();

        btnAddTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDueTask();
                storeTasksList();
                ClearEDTS();
            }
        });
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddTaskActivity.this,DueTasksActivity.class);
                startActivity(intent1);
            }
        });
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendarView_DueTask.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar sc = Calendar.getInstance();
                sc.set(year,month,dayOfMonth);
                selectedDate = sc.getTime();
            }
        });

    }
    private void setUpViews(){
        edtTaskTitle = findViewById(R.id.edtTaskTitle);
        edtTaskDescription = findViewById(R.id.edtTaskDescription);
        edtTaskLocation = findViewById(R.id.edtTaskLocation);
        calendarView_DueTask = findViewById(R.id.calendarView_DueTask);
        btnAddTasks = findViewById(R.id.btnAddTasks);
        btnTasks = findViewById(R.id.btnTasks);
        fabBack = findViewById(R.id.fabBack);
    }

    private void ClearEDTS(){
        edtTaskTitle.setText("");
        edtTaskDescription.setText("");
        edtTaskLocation.setText("");
    }

    private void setUpSharedPrefs(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void addDueTask(){
        String title = edtTaskTitle.getText().toString();
        String description = edtTaskDescription.getText().toString();
        String location = edtTaskLocation.getText().toString();
        if(description.isEmpty()){
            description = "N/A";
        }
        if(location.isEmpty()){
            location = "N/A";
        }
        Task newDueTask = new Task(title, description, location, selectedDate);
        DueTasksActivity.dueTasksList.add(newDueTask);
    }

    private void storeTasksList(){
        Gson gson = new Gson();
        String dueTasksAsJSON = gson.toJson(DueTasksActivity.dueTasksList);
        editor.putString(TASKS_LIST,dueTasksAsJSON);
        editor.commit();
    }

    private ArrayList<Task> loadTasksFromLS(){
        Gson gson = new Gson();
        String tasksAsJSON = prefs.getString(TASKS_LIST,"");
        if(!tasksAsJSON.isEmpty()){
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            return gson.fromJson(tasksAsJSON, type);
        }
        return new ArrayList<>();
    }
}