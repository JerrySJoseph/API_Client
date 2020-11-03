package com.jstechnologies.androidpostman.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jstechnologies.androidpostman.Helpers.AnimationManager;
import com.jstechnologies.androidpostman.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropDownView extends LinearLayout {
    Context context;
    RelativeLayout click;
    LinearLayout expandlayout,table;
    ImageView arrow;
    Button add;
    List<ItemTableView>tablerow= new ArrayList<>();
    TextView title;
    String displayTitle="Display Title";
    boolean isExpanded=true;
    boolean btndisabled=false;
    Map<String,String> Data= new HashMap<>();
    public DropDownView(Context context) {
        super(context);
        this.context=context;
        Init();
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
        UpdateView();
    }

    public DropDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        Init();
    }

    public Map<String, String> getData() {
        return Data;
    }

    public void AddEntry(String key, String value)
    {
        if(Data!=null)
            Data.put(key,value);
        PopTable();
    }
    public DropDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        Init();
    }
    void Init()
    {
         inflate(context, R.layout.view_dropdown,this);
         click=findViewById(R.id.click);
         expandlayout=findViewById(R.id.expand_view);
         arrow=findViewById(R.id.arrow);
         table=findViewById(R.id.table);
         add=findViewById(R.id.add);
         title=findViewById(R.id.displayTitle);
         click.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 ChangeView();
             }
         });
         add.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 showCustomDialog("","",false);
             }
         });
         UpdateView();
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setBtndisabled(boolean btndisabled) {
        this.btndisabled = btndisabled;
        UpdateView();
    }

    void UpdateView()
    {
        title.setText(displayTitle);
        ChangeView();
        PopTable();
        if(btndisabled)
            add.setVisibility(GONE);
        else
            add.setVisibility(VISIBLE);
    }
    void PopTable()
    {
        table.removeAllViews();
        tablerow.clear();
        Object[] array=Data.entrySet().toArray();
       /* for(int i=0;i<array.length;i++)
        {
            Map.Entry<String,String>entry=(Map.Entry<String,String>)array[i];
            int k=0;
        }*/
        for(Map.Entry<String,String>entry:Data.entrySet())
        {
            addItemToTable(entry.getKey(),entry.getValue());
        }
    }
    void ChangeView()
    {
        if(!isExpanded)
        {
            AnimationManager.Expand(expandlayout);
            isExpanded=true;
            arrow.setImageResource(R.drawable.ic_arrow_down);
        }
        else {
            AnimationManager.Collapse(expandlayout);
            isExpanded=false;
            arrow.setImageResource(R.drawable.ic_arrow_right);
        }
    }
    public void Expand()
    {
        AnimationManager.Expand(expandlayout);
        isExpanded=true;
        arrow.setImageResource(R.drawable.ic_arrow_down);
    }
    public void Collapse()
    {
        AnimationManager.Collapse(expandlayout);
        isExpanded=false;
        arrow.setImageResource(R.drawable.ic_arrow_right);
    }
    public void removeEntry(String key)
    {
        Data.remove(key);
        UpdateView();
    }
    public void ClearAll()
    {
        Data.clear();
        UpdateView();
    }

    void addItemToTable(String key, String value)
    {
        final ItemTableView row=new ItemTableView(context);
        row.SetKey(key);
        row.SetValue(value);
        table.addView(row);
        tablerow.add(row);
        row.setmClickListener(new ItemTableView.ClickListener() {
            @Override
            public void onRemove(String key, String value) {
                Data.remove(key);
                PopTable();
            }

            @Override
            public void onEdit(String oldKey, String oldValue) {
                showCustomDialog(oldKey,oldValue,true);
            }
        });

    }

    private void showCustomDialog(final String keytext, String valuetext, final boolean isEdit) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.view_inputdialog, viewGroup, false);
        Button save=dialogView.findViewById(R.id.save);
        Button clear=dialogView.findViewById(R.id.clear);
        final EditText key=dialogView.findViewById(R.id.key);
        final EditText value=dialogView.findViewById(R.id.value);
        final TextView status=dialogView.findViewById(R.id.status);
        key.setText(keytext);
        value.setText(valuetext);
        status.setVisibility(GONE);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                key.setText("");
                value.setText("");
            }
        });
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setVisibility(VISIBLE);
                if(key.getText().toString().isEmpty()||value.getText().toString().isEmpty())
                    status.setText(" Empty Fields");
                else
                {
                    if(isEdit)
                        Data.remove(keytext);
                    status.setVisibility(GONE);
                    Data.put(key.getText().toString().trim(),value.getText().toString().trim());
                    alertDialog.dismiss();
                    PopTable();
                }

            }
        });
    }
}
