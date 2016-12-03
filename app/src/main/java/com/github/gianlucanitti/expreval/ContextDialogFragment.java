package com.github.gianlucanitti.expreval;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.github.gianlucanitti.javaexpreval.*;

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
            return (data.isReadOnly() ? "readonly " : "") + name + "=" + data.getValue();
        }
    }

    private static class FunctionContextItem extends ContextItem{
        private Function data;

        private FunctionContextItem(Function data){
            this.data = data;
        }

        @Override
        public String toString(){
            return (data.isReadOnly() ? "readonly " : "") + data.getName() + "(" + data.getArgCount() + " arguments)";
        }
    }

    private class ListItems{

        private  ArrayList<ContextItem> items = new ArrayList<>();
        private ExpressionContext ctx;

        private void update(ExpressionContext ctx){
            this.ctx = ctx;
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

        private ContextItem removeItem(int i) throws ReadonlyException{
            ContextItem item = items.get(i);
            if (item instanceof VariableContextItem) {
                ctx.delVariable(((VariableContextItem) item).name);
            } else {
                Function f = ((FunctionContextItem) item).data;
                ctx.delFunction(f.getName(), f.getArgCount());
            }
            update(ctx);
            return item;
        }
    }

    private FragmentManager fm;
    private EditVariableDialogFragment editVariable;
    private EditFunctionDialogFragment editFunction;

    private ListItems items = new ListItems();

    private void showEditVariable(String varName){
        Bundle args = new Bundle();
        args.putString("varName", varName);
        editVariable.setArguments(args);
        editVariable.show(fm, "edit_variable");
    }

    private void showEditFunction(String funName, int argCount){
        Bundle args = new Bundle();
        args.putString("funName", funName);
        args.putInt("argCount", argCount);
        editFunction.setArguments(args);
        editFunction.show(fm, "edit_function");
    }

    @Override
    public void update(Observable observable, Object data) {
        items.update((ExpressionContext)observable);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        setRetainInstance(true);
        editVariable = new EditVariableDialogFragment();
        editFunction = new EditFunctionDialogFragment();
        fm = getFragmentManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items.getStrings(), this);
        builder.setPositiveButton("New...", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, final int which) {
        final ExprEval activity = (ExprEval)getActivity();
        AlertDialog.Builder innerDialog = new AlertDialog.Builder(activity);
        if (which == DialogInterface.BUTTON_POSITIVE) {
            innerDialog.setItems(new String[]{"Add variable", "Add function"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == 0) {
                        showEditVariable("");
                    }else if (i == 1){
                        showEditFunction("", 0);
                    }
                }
            });
        } else {
            innerDialog.setMessage("Do you want to delete \"" + items.getItem(which).toString() + "\"?");
            innerDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog_inner, int which_inner) {
                    try{
                        activity.writeOutput(items.removeItem(which).toString() + " has been deleted.");
                    }catch(ExpressionException ex){
                        Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_LONG).show();
                        activity.writeOutput(ex.getMessage());
                    }
                }
            });
            innerDialog.setNegativeButton("No", null);
        }
        innerDialog.create().show();
    }
}
