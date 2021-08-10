package com.example.quanlysieuthi;


import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {
    public GradientTextView(@NonNull Context context) {
        super(context);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor (TextView txt, int color1, int color2)
    {
        TextPaint paint = txt.getPaint();
        float width = paint.measureText(txt.getText().toString());

        Shader shader = new LinearGradient(0,0,width,txt.getTextSize(),color1,color2,Shader.TileMode.CLAMP);
        txt.getPaint().setShader(shader);
        txt.setTextColor(color1);
    }
}
