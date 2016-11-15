package com.github.gianlucanitti.expreval;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.Function;

import java.util.*;

public class ContextDialogFragment extends DialogFragment implements Observer, DialogInterface.OnClickListener{

    List<String> items = new ArrayList<String>();

    @Override
    public void update(Observable observable, Object data) {
        ExpressionContext ctx = (ExpressionContext)observable;
        items.clear();
        for(Map.Entry<String, ExpressionContext.VariableValue> v: ctx.getVariables().entrySet())
            items.add(v.getKey() + " = " + v.getValue().toString());
        for(Function f: ctx.getFunctions())
            items.add(f.toString());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items.toArray(new String[0]), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
