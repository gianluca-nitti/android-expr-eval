package com.github.gianlucanitti.expreval;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.github.gianlucanitti.javaexpreval.Expression;
import com.github.gianlucanitti.javaexpreval.ExpressionContext;
import com.github.gianlucanitti.javaexpreval.ExpressionException;

public class EditVariableDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private ExpressionContext ctx;
    private String varName;

    private EditText nameText;
    private EditText valueText;
    private CheckBox readonlyCheckbox;

    private void writeOutput(String s){
        ((ExprEval) getActivity()).writeOutput(s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        setRetainInstance(true);
        varName = getArguments().getString("varName");
        ctx = ((ExprEval) getActivity()).getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_variable, null);
        nameText = (EditText) rootLayout.findViewById(R.id.nameText);
        valueText = (EditText) rootLayout.findViewById(R.id.valueText);
        readonlyCheckbox = (CheckBox) rootLayout.findViewById(R.id.readonlyCheckbox);
        nameText.setText(varName);
        builder.setView(rootLayout);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        //TODO: check if it's a valid name -> setError on edittext
        if(which == DialogInterface.BUTTON_POSITIVE){
            try {
                ctx.setVariable(varName = nameText.getText().toString(), readonlyCheckbox.isChecked(), Expression.parse(valueText.getText().toString()));
                writeOutput(varName + " is now " + ctx.getVariable(varName));
            }catch(ExpressionException ex){
                writeOutput(ex.getMessage());
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
