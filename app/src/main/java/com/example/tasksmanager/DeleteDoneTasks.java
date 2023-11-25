package com.example.tasksmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeleteDoneTasks extends AppCompatActivity {
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String LOCATION = "location";
    public static Date DATE;
    private TextView txtTitleDone;
    private TextView txtDesDone;
    private TextView txtLocDone;
    private TextView txtDateDone;
    private FloatingActionButton fabDelete;
    private CheckBox chkDelete;
    private int taskPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_done_tasks);

        setUpViews();
        getTheTaskFromLV();

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkDelete.isChecked()){
                    DoneTasks.removeDoneTask(taskPosition);
                    finish();
                }
            }
        });

    }
    private void getTheTaskFromLV(){
        Intent intent = getIntent();
        TITLE = intent.getStringExtra("title");
        DESCRIPTION = intent.getStringExtra("description");
        LOCATION = intent.getStringExtra("location");
        DATE = (Date) intent.getSerializableExtra("due");
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = date.format(DATE);
        txtTitleDone.setText("Title: "+ TITLE);
        txtDesDone.setText("Description: " + DESCRIPTION);
        txtLocDone.setText("Location: "+ LOCATION);
        txtDateDone.setText("Due: " + dateString);
        taskPosition = intent.getIntExtra("position",-1);
    }

    private void setUpViews(){
        txtTitleDone = findViewById(R.id.txtTitleDone);
        txtDesDone = findViewById(R.id.txtDesDone);
        txtLocDone = findViewById(R.id.txtLocDone);
        txtDateDone = findViewById(R.id.txtDateDone);
        chkDelete = findViewById(R.id.chkDelete);
        fabDelete = findViewById(R.id.fabDelete);
    }
}