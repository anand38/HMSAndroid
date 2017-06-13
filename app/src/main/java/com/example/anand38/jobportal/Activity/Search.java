package com.example.anand38.jobportal.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anand38.jobportal.Configurations.AppConfig;
import com.example.anand38.jobportal.Helper.Network_Check;
import com.example.anand38.jobportal.Helper.XMLMsgParser;
import com.example.anand38.jobportal.Helper.XmlSerializer;
import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.RecyclerviewComponents.DividerItemDecoration;
import com.example.anand38.jobportal.RecyclerviewComponents.JobsAdapter;
import com.example.anand38.jobportal.RecyclerviewComponents.RecyclerTouchListener;
import com.example.anand38.jobportal.bean.Job;

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
import java.util.List;

public class Search extends AppCompatActivity {
    private List<Job> jobList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JobsAdapter mAdapter;
    ImageView close;
    EditText query;
    Button go;
    public static ProgressDialog pd;
    DataClass dc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#3F51B5"));
        }

        ////////////
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ///////////

        //This code prevents entry of new line code adjustments :)
        query= (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
        /*
         * The loop is in reverse for a purpose,
         * each replace or delete call on the Editable will cause
         * the afterTextChanged method to be called again.
         * Hence the return statement after the first removal.
         * http://developer.android.com/reference/android/text/TextWatcher.html#afterTextChanged(android.text.Editable)
         */
                for(int i = s.length()-1; i >= 0; i--){
                    if(s.charAt(i) == '\n'){
                        s.delete(i, i + 1);
                        return;
                    }
                }

            }
        });
        //////////////////
        go= (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (Network_Check.isNetworkAvailable(getApplicationContext())) {
                   View view = Search.this.getCurrentFocus();
                   //hide keyboard
                   if (view != null) {
                       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                   }
                    String s = query.getText().toString().trim();
                    //call webservice for jobs list
                    dc = new DataClass(s);
                    dc.execute();
                }else {
                    Toast.makeText(Search.this, "Please check network connection and retry..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /////////////////
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Job job = jobList.get(position);
                //Toast.makeText(getApplicationContext(), job.getPosition() + " is selected!", Toast.LENGTH_SHORT).show();
                ArrayList<String> details=new ArrayList<String>();
                details.add(job.getPosition());
                details.add(job.getSalary());
                details.add(job.getLocation());
                details.add(job.getOpenings());
                details.add(job.getDescription());
                details.add(job.getEligibility());
                details.add(job.getPosted_on());
                details.add(job.getJob_id());
                System.out.println("Job id is:"+job.getJob_id());
                Intent i=new Intent(Search.this,Particular_job.class);
                i.putStringArrayListExtra("job_details",details);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        }
    private void callrecyclertoupdate(){
        mAdapter = new JobsAdapter(jobList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //prepareJobsData();

    }
    private void prepareJobsData() {

        Job job=new Job("Software Engineer","30000","Mumbai");
        jobList.add(job);

        job=new Job("Hadoop Engineer","40000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","30000","Mumbai");
        jobList.add(job);

        job=new Job("Database Engineer","35000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","40000","Mumbai");
        jobList.add(job);

        job=new Job("Software Engineer","40000","Delhi");
        jobList.add(job);
        job=new Job("Hadoop Engineer","40000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","30000","Mumbai");
        jobList.add(job);

        job=new Job("Database Engineer","35000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","40000","Mumbai");
        jobList.add(job);

        job=new Job("Software Engineer","40000","Delhi");
        jobList.add(job);
        job=new Job("Hadoop Engineer","40000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","30000","Mumbai");
        jobList.add(job);

        job=new Job("Database Engineer","35000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","40000","Mumbai");
        jobList.add(job);

        job=new Job("Software Engineer","40000","Delhi");
        jobList.add(job);
        job=new Job("Hadoop Engineer","40000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","30000","Mumbai");
        jobList.add(job);

        job=new Job("Database Engineer","35000","Delhi");
        jobList.add(job);

        job=new Job("System Engineer","40000","Mumbai");
        jobList.add(job);

        job=new Job("Software Engineer","40000","Delhi");
        jobList.add(job);
        mAdapter.notifyDataSetChanged();
    }


    public class DataClass extends AsyncTask<Void,Void,String>{
        String q;
        public DataClass(String q){
            this.q=q;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(Search.this);
                pd.setMessage("Hold on..searching jobs..");
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
                                if (dc != null) {
                                    dialog.cancel();
                                    dc.cancel(true);
                                    dc = null;
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
            String url= AppConfig.get_job_data_url;
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
                System.out.println("Sent string is:"+ XmlSerializer.createqueryXML(q));
                httpPut.setEntity(new StringEntity(XmlSerializer.createqueryXML(q), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();



                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();

                jobList=XMLMsgParser.parse_jobs_data(is,theString);
                //System.out.println("Size of my list:"+list.size());

                return theString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
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
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
            if (jobList.size()==0){
                Toast.makeText(Search.this, "No Result found. Try searching for some other keywords eg. engineer etc.", Toast.LENGTH_LONG).show();
            }            System.out.println("Size is:"+jobList.size());
            callrecyclertoupdate();
        }
    }

}
