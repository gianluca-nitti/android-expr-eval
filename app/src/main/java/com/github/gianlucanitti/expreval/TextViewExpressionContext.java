package com.github.gianlucanitti.expreval;

import android.graphics.Color;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.InteractiveExpressionContext;
import com.github.gianlucanitti.javaexpreval.NullOutputStream;

import java.io.IOException;
import java.io.StringReader;

public class TextViewExpressionContext extends InteractiveExpressionContext {

    private TextViewWriter inEchoWriter;
    private TextViewWriter outWriter;
    private TextViewWriter verboseWriter;
    private TextViewWriter errorWriter;
    boolean echoInput = true;

    public TextViewExpressionContext(TextView out){
        super();
        inEchoWriter = new TextViewWriter(out, Color.YELLOW);
        setOutputWriter(outWriter = new TextViewWriter(out, Color.GREEN), true);
        setVerboseOutputWriter(verboseWriter = new TextViewWriter(out, out.getTextColors().getDefaultColor()), true);
        setErrorOutputWriter(errorWriter = new TextViewWriter(out, Color.RED), true);
    }

    public void writeOutput(String s){
        try {
            outWriter.write(s);
            outWriter.flush();
        }catch (IOException e){}
    }

    public void setVerbose(boolean b){
        if(b)
            setVerboseOutputWriter(verboseWriter, true);
        else
            setVerboseOutputWriter(NullOutputStream.getWriter(), true);
    }

    public void setEchoInput(boolean b){
        echoInput = b;
    }

    public boolean update(String s){
        setInputReader(new StringReader(s));
        InteractiveExpressionContext.Status status = null;
        try {
            if(echoInput) {
                inEchoWriter.write("> " + s + System.getProperty("line.separator"));
                inEchoWriter.flush();
            }
            return update() == Status.EXIT;
        }catch(IOException ex){
            return false;
        }
    }

}
