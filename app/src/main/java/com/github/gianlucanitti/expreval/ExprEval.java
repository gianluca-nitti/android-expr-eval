package com.github.gianlucanitti.expreval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.InteractiveExpressionContext;
import com.github.gianlucanitti.javaexpreval.NullInputStream;

import java.io.IOException;
import java.io.StringReader;

public class ExprEval extends AppCompatActivity implements View.OnClickListener{

    private InteractiveExpressionContext ctx;

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
        TextViewWriter outWriter = new TextViewWriter(out);
        ctx = new InteractiveExpressionContext(NullInputStream.getReader(), outWriter, outWriter, outWriter, true);
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
            case R.id.action_settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view){
        ctx.setInputReader(new StringReader(in.getText().toString()));
        try {
            ctx.update();
        }catch(IOException ex){}
        in.getText().clear();
    }

}
