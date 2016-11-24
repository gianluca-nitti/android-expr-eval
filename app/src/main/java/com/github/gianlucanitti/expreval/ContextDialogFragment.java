package com.github.gianlucanitti.expreval;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.Function;
import com.github.gianlucanitti.javaexpreval.VariableExpression;

import java.util.*;

public class ContextDialogFragment extends DialogFragment implements Observer, DialogInterface.OnClickListener{

    private static abstract class ContextItem{
        @Override
        public abstract String toString();
    }

    private static class VariableContextItem extends ContextItem{
        private String name;
        private ExpressionContext.VariableValue data;

        private VariableContextItem(String name, ExpressionContext.VariableValue data){
            this.name = name;
            this.data = data;
        }

        @Override
        public String toString(){
            return name + "=" + data.toString(); //TODO
        }
    }

    private static class FunctionContextItem extends ContextItem{
        private Function data;

        private FunctionContextItem(Function data){
            this.data = data;
        }

        @Override
        public String toString(){
            return data.toString(); //TODO
        }
    }

    private class ListItems{
        private  ArrayList<ContextItem> items = new ArrayList<>();

        private void update(ExpressionContext ctx){
            items.clear();
            for(Map.Entry<String, ExpressionContext.VariableValue> v: ctx.getVariables().entrySet())
                items.add(new VariableContextItem(v.getKey(), v.getValue()));
            for(Function f: ctx.getFunctions())
                items.add(new FunctionContextItem(f));
        }

        private String[] getStrings(){
            String[] result = new String[items.size()];
            for(int i = 0; i < result.length; i++)
                result[i] = items.get(i).toString();
            return result;
        }

        private ContextItem getItem(int i){
            return items.get(i);
        }
    }

    private FragmentManager fm;
    private EditVariableDialogFragment editVariable;

    private ListItems items = new ListItems();

    private void showEditVariable(String varName){
        Bundle args = new Bundle();
        args.putString("varName", varName);
        editVariable.setArguments(args);
        editVariable.show(fm, "edit_variable");
    }

    @Override
    public void update(Observable observable, Object data) {
        items.update((ExpressionContext)observable);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        setRetainInstance(true);
        editVariable = new EditVariableDialogFragment();
        fm = getFragmentManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items.getStrings(), this);
        builder.setPositiveButton("New...", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                AlertDialog.Builder typeSelector = new AlertDialog.Builder(getActivity());
                typeSelector.setItems(new String[]{"Add variable", "Add function"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            showEditVariable("");
                        }else if (i == 1){

                        }
                    }
                });
                typeSelector.create().show();
            } else {
                ContextItem ci = items.getItem(which);
                Log.d("CONTEXTDIALOGFRAGMENT", "selected " + (ci instanceof VariableContextItem ? "variable" : "function") + " " + ci.toString());
            }
    }
}
