package com.example.anand38.jobportal.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand38.jobportal.Configurations.AppConfig;
import com.example.anand38.jobportal.SessionHandler.SessionManager;
import com.example.anand38.jobportal.Helper.FileJob;
import com.example.anand38.jobportal.Helper.Network_Check;
import com.example.anand38.jobportal.Helper.XMLMsgParser;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button submit;
    TextView register;
    Myclass myclass;
    GainDetail gainDetail;
    static String name;
    public static ProgressDialog pd;
    public static ProgressDialog pd1;

    SessionManager manager;
    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        findViewsbyID();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = MainActivity.this.getCurrentFocus();
                //hide keyboard
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                String e=email.getText().toString().trim();
                String p=password.getText().toString().trim();
                if (Network_Check.isNetworkAvailable(getApplicationContext())){
                    myclass=new Myclass(e,p);
                    myclass.execute();

                    manager=new SessionManager(getApplicationContext());
                    manager.createLoginSession(e);

                }else {
                    Toast.makeText(MainActivity.this, "Please check network connection and retry..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("http://env-4841395.j.layershift.co.uk/Registration/"));
                startActivity(i);
            }
        });
    }

    private void findViewsbyID(){
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        submit= (Button) findViewById(R.id.submit);
        register= (TextView) findViewById(R.id.register);
    }

    public class Myclass extends AsyncTask<Void,Void,String>{
        String email="",password="";
        public Myclass(String email,String password){
            this.email=email;
            this.password=password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Logging in\nPlease wait...");
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
                                if (myclass != null) {
                                    dialog.cancel();
                                    myclass.cancel(true);
                                    myclass = null;
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
            Candidate candidate=new Candidate();
            candidate.setEmail(email);
            candidate.setPassword(password);
            String url= AppConfig.login_check_url;
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
                System.out.println("Sent string is:"+XmlSerializer.createCandidateXML(candidate));
                httpPut.setEntity(new StringEntity(XmlSerializer.createCandidateXML(candidate), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();
                //After receiving response message we convert it into String
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();
                //lets check what we received by printing it either success :) or failed :(
                System.out.println("String received:"+theString);
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
            if (s.equalsIgnoreCase("success")){
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                gainDetail=new GainDetail(email); //call to webservice method done in this async call
                gainDetail.execute();


            }else if (s.equalsIgnoreCase("failed")){
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
            if (myclass != null) {
                myclass.cancel(true);
                myclass = null;
            }



        }
    }


    public class GainDetail extends AsyncTask<Void, Void, String> {
        String email="";
        public GainDetail(String email){
            this.email=email;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd1 = new ProgressDialog(MainActivity.this);
                pd1.setMessage("Setting you up\nPlease wait...");
                pd1.setCanceledOnTouchOutside(false);
                pd1.setCancelable(false);
                pd1.setIndeterminate(true);
                pd1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String url= AppConfig.getnameurl;
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
                System.out.println("Sent string is:"+ XmlSerializer.create_xml_from_email_only(email));
                httpPut.setEntity(new StringEntity(XmlSerializer.create_xml_from_email_only(email), HTTP.UTF_8));
                //get the response
                response = httpClient.execute(httpPut);
                //get entities from response
                HttpEntity entity = response.getEntity();
                //get the data in streams
                is = entity.getContent();

                HashMap<String,String> map= XMLMsgParser.parse_id_name(is);
                System.out.println("AnandID:"+map.get("id"));
                System.out.println("AnandName:"+map.get("name"));
                System.out.println("AnandEmail:"+email);
                //writing to file
                FileJob.save_to_file(map.get("id"),map.get("name"),email,getApplicationContext());
                //reading from file
                FileJob.read_from_file(getApplicationContext());


            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (gainDetail != null) {
                gainDetail.cancel(true);
                gainDetail = null;
            }
            //hide progress dialog here
            if (pd1 != null && pd1.isShowing()) {
                pd1.dismiss();
                pd1 = null;
            }
            Intent i= new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
        }
    }

}