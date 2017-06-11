package com.example.anand38.jobportal.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anand38.jobportal.Activity.MainActivity;
import com.example.anand38.jobportal.Activity.Particular_job;
import com.example.anand38.jobportal.Activity.Particular_job_for_already_applied;
import com.example.anand38.jobportal.Activity.Search;
import com.example.anand38.jobportal.Configurations.AppConfig;
import com.example.anand38.jobportal.Helper.Network_Check;
import com.example.anand38.jobportal.Helper.XMLMsgParser;
import com.example.anand38.jobportal.Helper.XmlSerializer;
import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.RecyclerviewComponents.DividerItemDecoration;
import com.example.anand38.jobportal.RecyclerviewComponents.JobsAdapter;
import com.example.anand38.jobportal.RecyclerviewComponents.RecyclerTouchListener;
import com.example.anand38.jobportal.SessionHandler.SessionManager;
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
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Applied_Jobs extends Fragment {

    private List<Job> jobList = new ArrayList<>();
    private RecyclerView recyclerView;
    private JobsAdapter mAdapter;
    public static ProgressDialog pd;
    View v;
    AppliedClass appliedclass;
    SessionManager session;


    public Applied_Jobs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_appplied_jobs, container, false);
        session = new SessionManager(getActivity().getApplicationContext());

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
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
                Intent i=new Intent(getActivity(),Particular_job_for_already_applied.class);
                i.putStringArrayListExtra("job_details",details);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //call async method
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        if (Network_Check.isNetworkAvailable(getActivity().getApplicationContext())) {
            appliedclass=new AppliedClass(email);
            appliedclass.execute();
        }else {
            Toast.makeText(getActivity(), "Please check network connection and retry..", Toast.LENGTH_SHORT).show();

        }

        return v;
    }
    private void callrecyclertoupdate(){
        mAdapter = new JobsAdapter(jobList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //prepareJobsData();

    }
    public class AppliedClass extends AsyncTask<Void,Void,Void>{
        String email="";
        public AppliedClass(String email){
            this.email=email;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("Fetching data\nPlease wait...");
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
                                if (appliedclass != null) {
                                    dialog.cancel();
                                    appliedclass.cancel(true);
                                    appliedclass = null;
                                }
                            }
                        });
                pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url= AppConfig.showappliedjobs;
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
                System.out.println("Sent string is:"+ XmlSerializer.createXMLforappliedjobs(email));
                httpPut.setEntity(new StringEntity(XmlSerializer.createXMLforappliedjobs(email), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();

                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();
                System.out.println("String received:"+theString);
                System.out.println("Done printing");
                jobList= XMLMsgParser.parse_jobs_data(is,theString);

                //lets check what we received by printing it either success :) or failed :(
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //hide progress dialog here
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
            if (appliedclass != null) {
                appliedclass.cancel(true);
                appliedclass = null;
            }
            System.out.println("Size is:"+jobList.size());
            if (jobList.size()==0){
                Toast.makeText(getActivity(), "Oops, You haven't applied for any jobs!!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "You have applied for these jobs", Toast.LENGTH_LONG).show();
            }
            callrecyclertoupdate();

        }
    }
}
