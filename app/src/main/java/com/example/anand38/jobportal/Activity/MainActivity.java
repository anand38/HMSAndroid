package com.example.anand38.jobportal.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button submit;
    Myclass myclass;
    public static ProgressDialog pd;
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
                String e=email.getText().toString().trim();
                String p=password.getText().toString().trim();
                myclass=new Myclass(e,p);
                myclass.execute();
            }
        });
    }

    private void findViewsbyID(){
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        submit= (Button) findViewById(R.id.submit);
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
            String url="http://env-7697253.j.layershift.co.uk/rest/login_service/getloginstatus";
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
                //parse the data
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String theString = writer.toString();
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
            if (s.equalsIgnoreCase("success")){
                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                Intent i= new Intent(MainActivity.this, Home.class);
                startActivity(i);
                finish();
            }else if (s.equalsIgnoreCase("failed")){
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
            if (myclass != null) {
                myclass.cancel(true);
                myclass = null;
            }
            //hide progress dialog here
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
        }
    }
}
