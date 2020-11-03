package com.jstechnologies.androidpostman.ui.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jstechnologies.androidpostman.Helpers.AnimationManager;
import com.jstechnologies.androidpostman.R;
import com.jstechnologies.androidpostman.Views.HeaderItem;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ResponseScreen extends AppCompatActivity {

    LinearLayout table,expandLayout1,expandLayout2;
    RelativeLayout click1,click2;
    ImageView arrow1,arrow2;
    TextView response,scode,time;
    boolean isexpanded1=true;
    boolean isexpanded2=true;
    HashMap<String,String>headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_screen);
        getSupportActionBar().setTitle("Response");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        response=findViewById(R.id.response);
        arrow1=findViewById(R.id.arrow);
        arrow2=findViewById(R.id.arrow2);
        click1=findViewById(R.id.click);
        scode=findViewById(R.id.scode);
        time=findViewById(R.id.time);
        click2=findViewById(R.id.click2);
        expandLayout1=findViewById(R.id.expand_view);
        expandLayout2=findViewById(R.id.expand_view2);
        table=findViewById(R.id.table);
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        String body=getIntent().getStringExtra("BODY");
        headers= (HashMap<String, String>) getIntent().getSerializableExtra("HEADERS");
        scode.setText(getIntent().getIntExtra("STATUSCODE",0)+" ");
        time.setText(getIntent().getLongExtra("TIME",0)+" ms");
        try {
            JsonElement jsonElement = new JsonParser().parse(body);
            response.setText(gson.toJson(jsonElement));
        }catch (Exception e)
        {
            response.setText("Error encountered parsing the response.\n"+e.getMessage());
        }


        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isexpanded1)
                    Collapse1();
                else
                    Expand1();
            }
        });
        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isexpanded2)
                    Collapse2();
                else
                    Expand2();
            }
        });
        UpdateView();
    }
    void UpdateView()
    {


        for(HashMap.Entry<String,String>entry:headers.entrySet())
        {
            View row= new HeaderItem(this);
            TextView key=row.findViewById(R.id.key);
            TextView val=row.findViewById(R.id.value);
            key.setText(entry.getKey());
            val.setText(": "+entry.getValue());
            table.addView(row);
        }
    }
    public void Expand1()
    {
        AnimationManager.Expand(expandLayout1);
        isexpanded1=true;
        arrow1.setImageResource(R.drawable.ic_arrow_down);
    }
    public void Collapse1()
    {
        AnimationManager.Collapse(expandLayout1);
        isexpanded1=false;
        arrow1.setImageResource(R.drawable.ic_arrow_right);
    }
    public void Expand2()
    {
        AnimationManager.Expand(expandLayout2);
        isexpanded2=true;
        arrow2.setImageResource(R.drawable.ic_arrow_down);
    }
    public void Collapse2()
    {
        AnimationManager.Collapse(expandLayout2);
        isexpanded2=false;
        arrow2.setImageResource(R.drawable.ic_arrow_right);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return true;
    }
}