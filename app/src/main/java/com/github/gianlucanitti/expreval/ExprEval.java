package com.github.gianlucanitti.expreval;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;

public class ExprEval extends AppCompatActivity implements View.OnClickListener{

    private TextViewExpressionContext ctx;

    private EditText in;
    private TextView out;
    private Button evalBtn;

    private ContextDialogFragment ctxDialog;

    public ExpressionContext getContext(){
        return ctx;
    }

    public void writeOutput(String s){
        out.append(s + System.getProperty("line.separator"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expr_eval);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        in = (EditText) findViewById(R.id.inputText);
        out = (TextView) findViewById(R.id.outputText);
        out.setMovementMethod(new ScrollingMovementMethod());
        evalBtn = (Button)findViewById(R.id.evalBtn);
        evalBtn.setOnClickListener(this);
        ctxDialog = new ContextDialogFragment();
        ctx = new TextViewExpressionContext(out);
        ctx.addObserver(ctxDialog);
        ctxDialog.update(ctx, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_context:
                ctxDialog.show(getFragmentManager(), "context_dialog");
                return true;
            case R.id.action_clearctx:
                AlertDialog.Builder prompt = new AlertDialog.Builder(this);
                prompt.setMessage(R.string.clearContextPrompt);
                prompt.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ctx.clear();
                        ctx.writeOutput(getString(R.string.contextCleared));
                    }
                });
                prompt.setNegativeButton(android.R.string.no, null);
                prompt.show();
                return true;
            case R.id.action_clearout:
                out.setText("");
                return true;
            case R.id.action_verbose:
                item.setChecked(!item.isChecked());
                ctx.setVerbose(item.isChecked());
                return true;
            case R.id.action_input:
                item.setChecked(!item.isChecked());
                ctx.setEchoInput(item.isChecked());
                return true;
            case R.id.action_help:
                new AlertDialog.Builder(this).setMessage(Html.fromHtml(getString(R.string.helpMessage))).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view){
        if(ctx.update(in.getText().toString()) == TextViewExpressionContext.Status.EXIT)
            finish();
        in.getText().clear();
    }

}
