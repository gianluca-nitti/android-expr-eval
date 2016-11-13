package com.github.gianlucanitti.expreval;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.io.IOException;
import java.io.Writer;

public class TextViewWriter extends Writer {

    private StringBuilder buffer;
    private TextView v;

    public TextViewWriter(TextView target){
        v = target;
        buffer = new StringBuilder();
    }

    @Override
    public void close() throws IOException {
        flush();
    }

    @Override
    public void flush() throws IOException {
        v.append(buffer.toString());
        buffer = new StringBuilder();
    }

    @Override
    public void write(@NonNull char[] buf, int offset, int count) throws IOException {
        for(int i = offset; i < count; i++)
            buffer.append(buf[i]);
    }
}
