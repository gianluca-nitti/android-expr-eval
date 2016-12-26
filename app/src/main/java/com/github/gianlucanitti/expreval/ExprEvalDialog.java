package com.github.gianlucanitti.expreval;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.UndefinedException;

public class ExprEvalDialog extends AppCompatActivity implements View.OnClickListener{

    private double result = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expr_eval_dialog);
        findViewById(R.id.evalDialogOK).setOnClickListener(this);
        findViewById(R.id.evalDialogCancel).setOnClickListener(this);
        //String expr = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        String expr = getIntent().getCharSequenceExtra("expression").toString();
        ((TextView)findViewById(R.id.evalDialogExpr)).append(expr);
        TextView evalDialogLog = (TextView)findViewById(R.id.evalDialogLog);
        evalDialogLog.setMovementMethod(new ScrollingMovementMethod());
        evalDialogLog.append(System.getProperty("line.separator"));
        TextViewExpressionContext ctx = new TextViewExpressionContext(evalDialogLog);
        ctx.update(expr); //TODO: set failOnError
        try {
            result = ctx.getVariable("ans");
        }catch(UndefinedException e){}
        ((TextView)findViewById(R.id.evalDialogResult)).append(Double.toString(result));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.evalDialogOK)
            setResult(RESULT_OK, new Intent().putExtra("result", result));
        else
            setResult(RESULT_CANCELED);
        finish();
    }

}
