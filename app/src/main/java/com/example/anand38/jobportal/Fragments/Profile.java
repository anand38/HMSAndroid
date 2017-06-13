package com.example.anand38.jobportal.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anand38.jobportal.Activity.MainActivity;
import com.example.anand38.jobportal.Configurations.AppConfig;
import com.example.anand38.jobportal.Helper.XMLMsgParser;
import com.example.anand38.jobportal.Helper.XmlSerializer;
import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.SessionHandler.SessionManager;

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
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    View v;
    TextView name,email,gender,dob,contact,course,college_name,specialization,yop;
    ProfileGet profileGet;
    SessionManager session;
    HashMap<String,String> map=new HashMap<>();

    public static ProgressDialog pd;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        name= (TextView) v.findViewById(R.id.name);
        email= (TextView) v.findViewById(R.id.email);
        gender= (TextView) v.findViewById(R.id.gender);
        dob= (TextView) v.findViewById(R.id.dob);
        contact= (TextView) v.findViewById(R.id.contact);
        course= (TextView) v.findViewById(R.id.course);
        college_name= (TextView) v.findViewById(R.id.college_name);
        specialization= (TextView) v.findViewById(R.id.specialization);
        yop= (TextView) v.findViewById(R.id.yop);

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        profileGet=new ProfileGet(email);
        profileGet.execute();
        return v;
    }
    public class ProfileGet extends AsyncTask<Void,Void,Void>{
        String email="";
        public ProfileGet(String email){
            this.email=email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(getActivity());
                pd.setMessage("getting profile information\nPlease wait...");
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
                                if (profileGet != null) {
                                    dialog.cancel();
                                    profileGet.cancel(true);
                                    profileGet = null;
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

            String url= AppConfig.getprofiledata;
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
                System.out.println("Sent string is:"+ XmlSerializer.createCandidateemailXML(email));
                httpPut.setEntity(new StringEntity(XmlSerializer.createCandidateemailXML(email), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                map= XMLMsgParser.parse_profile_data(is);

                //After receiving response message we convert it into String
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();
                //lets check what we received by printing it either success :) or failed :(
                System.out.println("String received:"+theString);




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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //hide progress dialog here
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            } if (profileGet != null) {
                profileGet.cancel(true);
                profileGet = null;
            }
            setData();
        }
    }
    private void setData(){
        name.setText(map.get("name"));
        email.setText(map.get("email"));
        gender.setText(map.get("gender"));
        dob.setText(map.get("dob"));
        contact.setText(map.get("contact"));
        course.setText(map.get("course"));
        college_name.setText(map.get("college_name"));
        specialization.setText(map.get("specialization"));
        yop.setText(map.get("yop"));
    }
}