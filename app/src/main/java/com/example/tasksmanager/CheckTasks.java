package com.example.tasksmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CheckTasks extends AppCompatActivity {
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String LOCATION = "location";
    public static Date DATE;
    private TextView txtTitleCheck;
    private TextView txtDesCheck;
    private TextView txtLocCheck;
    private TextView txtDateCheck;
    private FloatingActionButton fbaDone;
    private SharedPreferences prefs; //read
    private SharedPreferences.Editor editor; //write
    private CheckBox chkDone;
    private int taskPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_tasks);

        setUpViews();
        setupSharedPrefs();
        getTheTaskFromLV();

        DoneTasks.doneTasksList = loadTasksFromLS();

        fbaDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkDone.isChecked()){
                    addDoneTasks();
                    storeTasksList();
                    DueTasksActivity.removeDueTask(taskPosition);
                }
                finish();
            }
        });
    }

    private void setUpViews(){
        txtTitleCheck = findViewById(R.id.txtTitleCheck);
        txtDesCheck = findViewById(R.id.txtDesCheck);
        txtLocCheck = findViewById(R.id.txtLocCheck);
        txtDateCheck = findViewById(R.id.txtDateCheck);
        chkDone = findViewById(R.id.chkDone);
        fbaDone = findViewById(R.id.fabDone);
    }

    private void setupSharedPrefs(){
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void getTheTaskFromLV(){
        Intent intent = getIntent();
        TITLE = intent.getStringExtra("title");
        DESCRIPTION = intent.getStringExtra("description");
        LOCATION = intent.getStringExtra("location");
        DATE = (Date) intent.getSerializableExtra("due");
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        String dateString = date.format(DATE);
        txtTitleCheck.setText("Title: "+ TITLE);
        txtDesCheck.setText("Description: " + DESCRIPTION);
        txtLocCheck.setText("Location: "+ LOCATION);
        txtDateCheck.setText("Due: " + dateString);
        taskPosition = intent.getIntExtra("position",-1);
    }

    private void addDoneTasks(){
        Task doneTask = new Task(TITLE,DESCRIPTION,LOCATION,DATE);
        DoneTasks.doneTasksList.add(doneTask);
    }

    private void storeTasksList(){
        Gson gson = new Gson();
        String doneTasksAsJSON = gson.toJson(DoneTasks.doneTasksList);
        editor.putString(DoneTasks.DONE_TASKS_LIST,doneTasksAsJSON);
        editor.commit();
    }

    public ArrayList<Task> loadTasksFromLS(){
        Gson gson = new Gson();
        String doneTasksAsJSON = prefs.getString(DoneTasks.DONE_TASKS_LIST,"");
        if(!doneTasksAsJSON.isEmpty()){
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            return gson.fromJson(doneTasksAsJSON, type);
        }
        return new ArrayList<>();
    }
}