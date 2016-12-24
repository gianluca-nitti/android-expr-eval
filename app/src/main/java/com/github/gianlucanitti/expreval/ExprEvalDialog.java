package com.github.gianlucanitti.expreval;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.UndefinedException;

public class ExprEvalDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expr_eval_dialog);
        //String expr = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        String expr = getIntent().getCharSequenceExtra("expression").toString();
        ((TextView)findViewById(R.id.evalDialogResult)).setText(expr);
        TextViewExpressionContext ctx = new TextViewExpressionContext((TextView)findViewById(R.id.evalDialogLog));
        ctx.update(expr);
        try {
            ((TextView)findViewById(R.id.evalDialogResult)).setText(Double.toString(ctx.getVariable("ans")));
        }catch(UndefinedException e){}
    }

}
