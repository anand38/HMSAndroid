package com.example.anand38.jobportal.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.SessionHandler.SessionManager;

import java.util.ArrayList;

public class Particular_job_for_already_applied extends AppCompatActivity {
    TextView position_content,salary_content,location_content,openings_content,description_content,eligibility_content,date_of_post_content;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_job_for_already_applied);
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewsByID();
        final ArrayList<String> list=getIntent().getStringArrayListExtra("job_details");
        position_content.setText(list.get(0));
        salary_content.setText(list.get(1));
        location_content.setText(list.get(2));
        openings_content.setText(list.get(3));
        description_content.setText(list.get(4));
        eligibility_content.setText(list.get(5));
        date_of_post_content.setText(list.get(6));
    }

    private void findViewsByID() {
        position_content= (TextView) findViewById(R.id.position_content);
        salary_content= (TextView) findViewById(R.id.salary_content);
        location_content= (TextView) findViewById(R.id.location_content);
        openings_content= (TextView) findViewById(R.id.openings_content);
        description_content= (TextView) findViewById(R.id.description_content);
        eligibility_content= (TextView) findViewById(R.id.eligibility_content);
        date_of_post_content= (TextView) findViewById(R.id.date_of_post_content);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}
