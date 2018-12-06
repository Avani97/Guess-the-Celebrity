package com.avani.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celeburl=new ArrayList<String>();
    ArrayList<String> celeb=new ArrayList<String>();
    int chosenceleb=0;
    int chosenbutton=0;
    ImageView image;
    int check;
    public void click(View view)
    {
        Button b=(Button)view;

        String str=b.getText().toString();
        if(str.equals(celeb.get(chosenceleb)))
        {
            Toast.makeText(MainActivity.this,"CORRECT!",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this,"WRONG! It is actually "+celeb.get(chosenceleb),Toast.LENGTH_LONG).show();

        }
        int index=chooserandom();
        Random rn=new Random();
        chosenbutton=rn.nextInt(4);
        check=chosenbutton;
        String values[]=new String[4];
        for(int i=0;i<4;i++)
        {
            if(i==chosenbutton)
            {
                values[i]=celeb.get(index);
            }
            else
            {
                Random r=new Random();
                int myrand=r.nextInt(celeb.size());
                if(myrand==index)
                {
                    myrand=r.nextInt(celeb.size());
                }
                values[i]=celeb.get(myrand);
            }
        }
        Button b1=(Button)findViewById(R.id.button6);
        Button b2=(Button)findViewById(R.id.button7);
        Button b3=(Button)findViewById(R.id.button8);
        Button b4=(Button)findViewById(R.id.button9);
        b1.setText(values[0]);
        b2.setText(values[1]);
        b3.setText(values[2]);
        b4.setText(values[3]);


    }
public int chooserandom() {
    Random rand = new Random();
    chosenceleb = rand.nextInt(celeburl.size());
    DownloadImage task1 = new DownloadImage();
    Bitmap celeb;
    try {
        celeb = task1.execute(celeburl.get(chosenceleb)).get();
        image.setImageBitmap(celeb);
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
    return chosenceleb;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=(ImageView)findViewById(R.id.imageView);
        DownloadContent task=new DownloadContent();
        String mainstring="";
        try {
           mainstring=task.execute("http://www.posh24.se/kandisar").get();
            String[] afterfinal=mainstring.split("<div class=\"sidebarContainer\">");
            Pattern p=Pattern.compile("img src=\"(.*?)\" alt");
            Matcher m=p.matcher(afterfinal[0]);
            while(m.find())
            {
              celeburl.add(m.group(1));

            }
            p=Pattern.compile("alt=\"(.*?)\"/>");
            m=p.matcher(afterfinal[0]);
            while(m.find())
            {
               celeb.add(m.group(1));

            }
            int index=chooserandom();
            Random rn=new Random();
           chosenbutton=rn.nextInt(4);
            check=chosenbutton;
            String values[]=new String[4];
            for(int i=0;i<4;i++)
            {
                if(i==chosenbutton)
                {
                    values[i]=celeb.get(index);
                }
                else
                {
                    Random r=new Random();
                    int myrand=r.nextInt(celeb.size());
                    if(myrand==index)
                    {
                        myrand=r.nextInt(celeb.size());
                    }
                    values[i]=celeb.get(myrand);
                }
            }
            Button b1=(Button)findViewById(R.id.button6);
            Button b2=(Button)findViewById(R.id.button7);
            Button b3=(Button)findViewById(R.id.button8);
            Button b4=(Button)findViewById(R.id.button9);
            b1.setText(values[0]);
            b2.setText(values[1]);
            b3.setText(values[2]);
            b4.setText(values[3]);




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }

    public class DownloadContent extends AsyncTask<String,Void,String>
    {
        protected String doInBackground(String... urls)
        {
            String result="";
            URL url;
            HttpURLConnection connection=null;
            try {
                url=new URL(urls[0]);
                connection=(HttpURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();

                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";


        }
    }
    public class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... image) {
            URL url;
            HttpURLConnection connection;
            Bitmap retval;
            try {
                url=new URL(image[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream=connection.getInputStream();
                retval= BitmapFactory.decodeStream(stream);
                return retval;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }
}
