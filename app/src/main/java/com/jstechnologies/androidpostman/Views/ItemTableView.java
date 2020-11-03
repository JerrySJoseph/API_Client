package com.jstechnologies.androidpostman.Views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jstechnologies.androidpostman.R;

public class ItemTableView extends LinearLayout {
    Context context;
    TextView key,Value;
    ImageView check;
    LinearLayout layout;
    ClickListener mClickListener;
    public interface ClickListener{
        void onRemove(String key, String value);
        void onEdit(String oldKey,String oldValue);
    }

    public ItemTableView(Context context) {
        super(context);
        this.context=context;
        Init();
    }


    public void setmClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public ItemTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        Init();
    }

    public ItemTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        Init();
    }
    void Init()
    {
        inflate(context, R.layout.view_itemtable,this);
        key=findViewById(R.id.key);
        Value=findViewById(R.id.value);
        check=findViewById(R.id.check);
        layout=findViewById(R.id.layout);
        key.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEdit(getKey(),getValue());
            }
        });
        Value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEdit(getKey(),getValue());
            }
        });
        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onRemove(getKey(),getValue());
            }
        });

    }
    public void SetKey(String _key)
    {
        key.setText(_key);
    }
    public void SetValue(String _val)
    {
        Value.setText(_val);
    }
    public String getKey()
    {
        return key.getText().toString().trim();
    }
    public String getValue()
    {
        return Value.getText().toString().trim();
    }
}
