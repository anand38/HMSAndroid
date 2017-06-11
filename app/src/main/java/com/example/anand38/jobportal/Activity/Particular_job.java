package com.example.anand38.jobportal.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand38.jobportal.Configurations.AppConfig;
import com.example.anand38.jobportal.SessionHandler.SessionManager;
import com.example.anand38.jobportal.Helper.XmlSerializer;
import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.bean.Candidate;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Particular_job extends AppCompatActivity {
    TextView position_content,salary_content,location_content,openings_content,description_content,eligibility_content,date_of_post_content;
    Button apply;
    public static ProgressDialog pd;
    ApplyClass applyclass;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_job);
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

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Applying for job");
                System.out.println("Email is:"+Candidate.getEmail());

                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(SessionManager.KEY_EMAIL);

                applyclass=new ApplyClass(email,list.get(7));
                applyclass.execute();

            }
        });
    }

    private void findViewsByID() {
        position_content= (TextView) findViewById(R.id.position_content);
        salary_content= (TextView) findViewById(R.id.salary_content);
        location_content= (TextView) findViewById(R.id.location_content);
        openings_content= (TextView) findViewById(R.id.openings_content);
        description_content= (TextView) findViewById(R.id.description_content);
        eligibility_content= (TextView) findViewById(R.id.eligibility_content);
        date_of_post_content= (TextView) findViewById(R.id.date_of_post_content);
        apply= (Button) findViewById(R.id.apply);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pd != null)
            pd.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public class ApplyClass extends AsyncTask<Void,Void,String>{
            String email="",jobid="";
        public ApplyClass(String email, String jobid) {
            this.email=email;
            this.jobid=jobid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(Particular_job.this);
                pd.setMessage("Applying\nPlease wait...");
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                // on cancel event cancel currently running AsyncTask
                pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (applyclass != null) {
                                    dialog.cancel();
                                    applyclass.cancel(true);
                                    applyclass = null;
                                }
                            }
                        });
                pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String url= AppConfig.applyjob;
            //initialize response object
            HttpResponse response = null;
            //initialize stream object
            InputStream is = null;

            try {
                //initialize client to connect with the url with respective method
                HttpClient httpClient = new DefaultHttpClient();
                //initialize the request method
                HttpPut httpPut = new HttpPut(url);
                //get the response
                httpPut.addHeader("Content-Type", "application/xml");
                //set the form entity
                System.out.println("Sent string is:"+ XmlSerializer.createXMLforjobapply(email,jobid));
                httpPut.setEntity(new StringEntity(XmlSerializer.createXMLforjobapply(email,jobid), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                System.out.println("Got something");

                //After receiving response message we convert it into String
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();
                //lets check what we received by printing it either success :) or failed :(
                System.out.println("String received here is:"+theString);
                return theString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null)
                            is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //hide progress dialog here
                pd.dismiss();
                pd = null;

            if (s.equals("success")){
                Toast.makeText(Particular_job.this, "Successfully applied for this job!!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(Particular_job.this, "You have already applied for this job", Toast.LENGTH_LONG).show();
            }
            if (applyclass != null) {
                applyclass.cancel(true);
                applyclass = null;
            }
        }
    }
}
