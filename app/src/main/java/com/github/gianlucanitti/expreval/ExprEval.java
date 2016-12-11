package com.github.gianlucanitti.expreval;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.github.gianlucanitti.javaexpreval.InteractiveExpressionContext;
import com.github.gianlucanitti.javaexpreval.NullInputStream;
import com.github.gianlucanitti.javaexpreval.NullOutputStream;

import java.io.IOException;
import java.io.StringReader;

public class ExprEval extends AppCompatActivity implements View.OnClickListener{

    private InteractiveExpressionContext ctx;

    private EditText in;
    private TextView out;
    private Button evalBtn;

    private TextViewWriter inEchoWriter;
    private TextViewWriter outWriter;
    private TextViewWriter verboseWriter;
    private TextViewWriter errorWriter;

    private ContextDialogFragment ctxDialog;
    private boolean echoInput = true;

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
        inEchoWriter = new TextViewWriter(out, Color.YELLOW);
        outWriter = new TextViewWriter(out, Color.GREEN);
        verboseWriter = new TextViewWriter(out, out.getTextColors().getDefaultColor());
        errorWriter = new TextViewWriter(out, Color.RED);
        ctx = new InteractiveExpressionContext(NullInputStream.getReader(), outWriter, verboseWriter, errorWriter, true);
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
                prompt.setMessage("Do you want to delete all non-readonly functions and variables?");
                prompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ctx.clear();
                        try {
                            outWriter.write("All non-readonly variables and functions have been cleared.");
                            outWriter.flush();
                        }catch (IOException ex){}
                    }
                });
                prompt.setNegativeButton("No", null);
                prompt.show();
                return true;
            case R.id.action_clearout:
                out.setText("");
                return true;
            case R.id.action_verbose:
                item.setChecked(!item.isChecked());
                if(item.isChecked())
                    ctx.setVerboseOutputWriter(verboseWriter, true);
                else
                    ctx.setVerboseOutputWriter(NullOutputStream.getWriter(), true);
                return true;
            case R.id.action_input:
                item.setChecked(!item.isChecked());
                echoInput = item.isChecked();
                return true;
            case R.id.action_help:
                new AlertDialog.Builder(this).setMessage(Html.fromHtml("In the input box you can type expressions, variable or function assignments (like \"a=5\" or \"log(x,b)=log(x)/log(b)\") and commands. " +
                    "The available commands are \"help\", \"context\", \"clear\" and \"exit\". Type \"help\" in the input box for more detailed instructions. In the output box, results are shown in <font color=\"green\">green</font> and errors in <font color=\"red\">red</font>. " +
                    "Evaluation steps are shown in default color, and the input is echoed in <font color=\"yellow\">yellow</font> (both can be disabled from the menu).")).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view){
        ctx.setInputReader(new StringReader(in.getText().toString()));
        InteractiveExpressionContext.Status status = null;
        try {
            if(echoInput) {
                inEchoWriter.write("> " + in.getText().toString() + System.getProperty("line.separator"));
                inEchoWriter.flush();
            }
            status = ctx.update();
        }catch(IOException ex){}
        in.getText().clear();
        if(status == InteractiveExpressionContext.Status.EXIT)
            finish();
    }

}
