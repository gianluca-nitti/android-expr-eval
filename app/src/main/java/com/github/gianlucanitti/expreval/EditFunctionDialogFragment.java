package com.github.gianlucanitti.expreval;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.github.gianlucanitti.javaexpreval.*;

import java.util.ArrayList;

public class EditFunctionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private ExpressionContext ctx;
    private String funName;
    private ArrayList<String> argNames;

    private EditText nameText;
    private EditText exprText;
    private EditText newArgText;
    private ListView argsList;
    private CheckBox readonlyCheckbox;

    private void writeOutput(String s) {
        ((ExprEval) getActivity()).writeOutput(s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        funName = getArguments().getString("funName"); //TODO: remove
        ctx = ((ExprEval) getActivity()).getContext();
        argNames = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_function, null);
        nameText = (EditText) rootLayout.findViewById(R.id.nameText);
        exprText = (EditText) rootLayout.findViewById(R.id.exprText);
        newArgText = (EditText) rootLayout.findViewById(R.id.newArgText);
        argsList = (ListView) rootLayout.findViewById(R.id.argsList);
        readonlyCheckbox = (CheckBox) rootLayout.findViewById(R.id.readonlyCheckbox);
        nameText.setText(funName);
        final ArrayAdapter<String> argsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, argNames);
        argsList.setAdapter(argsArrayAdapter);
        argsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                argNames.remove(i);
                argsArrayAdapter.notifyDataSetChanged();
            }
        });
        rootLayout.findViewById(R.id.add_argument).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NamedSymbolExpression.isValidSymbolName(newArgText.getText().toString())) {
                    argNames.add(newArgText.getText().toString());
                    argsArrayAdapter.notifyDataSetChanged();
                    newArgText.getText().clear();
                } else {
                    newArgText.setError("Invalid argument name");
                }
            }
        });
        builder.setView(rootLayout);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            try {
                ctx.setFunction(nameText.getText().toString(), Expression.parse(exprText.getText().toString()), readonlyCheckbox.isChecked(), argNames.toArray(new String[argNames.size()]));
                writeOutput(ctx.getFunction(nameText.getText().toString(), argNames.size()).toString() + " is now defined as " + exprText.getText().toString() + ".");
            } catch (ExpressionException ex) {
                writeOutput(ex.getMessage());
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}