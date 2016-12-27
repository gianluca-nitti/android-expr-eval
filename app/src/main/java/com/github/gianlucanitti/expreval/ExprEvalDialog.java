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

    private boolean actionIsProcessText;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expr_eval_dialog);
        View okButton = findViewById(R.id.evalDialogOK);
        View cancelButton = findViewById(R.id.evalDialogCancel);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        actionIsProcessText = getIntent().getAction().equals(Intent.ACTION_PROCESS_TEXT);
        String expr = getIntent().getStringExtra(actionIsProcessText ? Intent.EXTRA_PROCESS_TEXT : "expression");
        if(actionIsProcessText && getIntent().getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)){
            okButton.setVisibility(View.INVISIBLE);
            ((Button)cancelButton).setText("Close");
            findViewById(R.id.evalDialogOkToReplace).setVisibility(View.GONE);
        }
        ((TextView)findViewById(R.id.evalDialogExpr)).append(expr);
        TextView evalDialogLog = (TextView)findViewById(R.id.evalDialogLog);
        evalDialogLog.setMovementMethod(new ScrollingMovementMethod());
        evalDialogLog.append(System.getProperty("line.separator"));
        TextViewExpressionContext ctx = new TextViewExpressionContext(evalDialogLog);
        ctx.setStopOnError(true);
        boolean failed = ctx.update(expr) == TextViewExpressionContext.Status.ERROR;
        okButton.setEnabled(!failed);
        if(failed)
            result = " evaluation failed.";
        else
            try {
                result = Double.toString(ctx.getVariable("ans"));
            }catch(UndefinedException e){
                result = " evaluation failed.";
            }
        ((TextView)findViewById(R.id.evalDialogResult)).append(result);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.evalDialogOK)
            setResult(RESULT_OK, new Intent().putExtra(actionIsProcessText ? Intent.EXTRA_PROCESS_TEXT : "result", result));
        else
            setResult(RESULT_CANCELED);
        finish();
    }

}
