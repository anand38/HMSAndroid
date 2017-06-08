package com.example.anand38.jobportal.Helper;

import android.content.Context;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anand38 on 8/6/17.
 */

public class FileJob {
    final static int READ_BLOCK_SIZE=10;
    public static void save_to_file(String id, String name, String email, Context c){

        try
        {
            //---Opens a file for writing ---
            FileOutputStream fOut =
                    c.openFileOutput("users.txt", MODE_PRIVATE);

            OutputStreamWriter osw = new
                    OutputStreamWriter(fOut);

            //---write the string to the file---
            osw.write(id+","+name+","+email);
            osw.flush();
            osw.close();
            System.out.println("File successfully saved");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void read_from_file(Context c){
        try
        {
            FileInputStream fIn = c.openFileInput("users.txt");
            InputStreamReader isr = new
                    InputStreamReader(fIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = isr.read(inputBuffer))>0)
            {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[READ_BLOCK_SIZE];
            }
            String x[]=s.split(",");
            System.out.println("Anand ID:"+x[0]);
            System.out.println("Anand Name:"+x[1]);
            System.out.println("Anand Email:"+x[2]);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static String getName(Context c){
        try
        {
            FileInputStream fIn = c.openFileInput("users.txt");
            InputStreamReader isr = new
                    InputStreamReader(fIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = isr.read(inputBuffer))>0)
            {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[READ_BLOCK_SIZE];
            }
             String x[]=s.split(",");
            return x[1];
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }

    public static String getEmail(Context c) {

        try
        {
            FileInputStream fIn = c.openFileInput("users.txt");
            InputStreamReader isr = new
                    InputStreamReader(fIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = isr.read(inputBuffer))>0)
            {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[READ_BLOCK_SIZE];
            }
            String x[]=s.split(",");
            return x[2];
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
}
