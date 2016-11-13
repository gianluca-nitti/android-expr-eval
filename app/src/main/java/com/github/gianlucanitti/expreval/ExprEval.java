package com.github.gianlucanitti.expreval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.InteractiveExpressionContext;
import com.github.gianlucanitti.javaexpreval.NullInputStream;

import java.io.IOException;
import java.io.StringReader;

public class ExprEval extends AppCompatActivity implements View.OnClickListener{

    private InteractiveExpressionContext ctx;

    private EditText in;
    private TextView out;
    private Button evalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expr_eval);
        in = (EditText)findViewById(R.id.inputText);
        out = (TextView) findViewById(R.id.outputText);
        out.setMovementMethod(new ScrollingMovementMethod());
        evalBtn = (Button)findViewById(R.id.evalBtn);
        evalBtn.setOnClickListener(this);
        TextViewWriter outWriter = new TextViewWriter(out);
        ctx = new InteractiveExpressionContext(NullInputStream.getReader(), outWriter, outWriter, outWriter, true);
    }

    @Override
    public void onClick(View view){
        ctx.setInputReader(new StringReader(in.getText().toString()));
        try {
            ctx.update();
        }catch(IOException ex){}
    }

}
