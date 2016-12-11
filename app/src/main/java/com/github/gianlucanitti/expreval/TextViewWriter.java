package com.github.gianlucanitti.expreval;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.io.IOException;
import java.io.Writer;

public class TextViewWriter extends Writer {

    private StringBuilder buffer;
    private TextView v;
    private int color;

    public TextViewWriter(TextView target, int c){
        v = target;
        color = c;
        buffer = new StringBuilder();
    }

    @Override
    public void close() throws IOException {
        flush();
    }

    @Override
    public void flush() throws IOException {
        SpannableString s = new SpannableString(buffer.toString());
        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        v.append(s);
        buffer = new StringBuilder();
    }

    @Override
    public void write(@NonNull char[] buf, int offset, int count) throws IOException {
        for(int i = offset; i < count; i++)
            buffer.append(buf[i]);
    }
}
