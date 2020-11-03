package com.jstechnologies.androidpostman.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jstechnologies.androidpostman.R;

public class HeaderItem extends LinearLayout {
    Context context;
    TextView key,Value;
    public HeaderItem(Context context) {
        super(context);
        this.context=context;
        Init();
    }

    public HeaderItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        Init();
    }

    public HeaderItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        Init();
    }
    void Init()
    {
        inflate(context, R.layout.item_header_response,this);
        key=findViewById(R.id.key);
        Value=findViewById(R.id.value);

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
