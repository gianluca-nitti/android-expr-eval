package com.github.gianlucanitti.expreval;

import android.content.Context;
import static com.github.gianlucanitti.javaexpreval.LocalizationHelper.*;

public class LibraryLocalizer {

    private static void setMessageResource(Context c, Message m, int id){
        setMessage(m, c.getString(id));
    }

    public static void localize(Context c){
        setMessageResource(c, Message.CONTEXT_CLEARED, R.string.contextCleared);
        setMessageResource(c, Message.EMPTY_EXPR, R.string.emptyExpr);
        setMessageResource(c, Message.EMPTY_SYM_NAME, R.string.emptySymName);
        setMessageResource(c, Message.ERROR_PREFIX, R.string.errorPrefix);
        setMessageResource(c, Message.EVAL_STEP, R.string.evalStep);
        setMessageResource(c, Message.EXPR_END_REACHED, R.string.exprEndReached);
        setMessageResource(c, Message.FAILED_STORE_RESULT, R.string.failedStoreResult);
        setMessageResource(c, Message.FUNC_ASSIGNED, R.string.functionNewDef);
        setMessageResource(c, Message.INCORRECT_DELETE, R.string.incorrectDelete);
        setMessageResource(c, Message.INTERACTIVE_HELP, R.string.interactiveHelp);
        setMessageResource(c, Message.INVALID_OPERATOR, R.string.invalidOperator);
        setMessageResource(c, Message.INVALID_SYM_NAME, R.string.invalidSymName);
        setMessageResource(c, Message.ONLY_ONE_EQUALITY, R.string.onlyOneEquality);
        setMessageResource(c, Message.OPERATOR_EXPECTED, R.string.operatorExpected);
        setMessageResource(c, Message.OPERATOR_FOUND, R.string.operatorFound);
        setMessageResource(c, Message.PARENTHESIS_MISMATCH, R.string.parenthesisMismatch);
        setMessageResource(c, Message.READONLY_FUNC, R.string.readonlyFunc);
        setMessageResource(c, Message.READONLY_VAR, R.string.readonlyVar);
        setMessageResource(c, Message.RESERVED_WORD, R.string.reservedWord);
        setMessageResource(c, Message.REWRITE_STEP, R.string.rewriteStep);
        setMessageResource(c, Message.UNDEFINED_FUNC, R.string.undefinedFunc);
        setMessageResource(c, Message.UNDEFINED_VAR, R.string.undefinedVar);
        setMessageResource(c, Message.UNKNOWN_CHAR, R.string.unknownChar);
        setMessageResource(c, Message.VAR_ASSIGNED, R.string.varNewValue);
        setMessageResource(c, Message.VAR_DELETED, R.string.contextItemDeleted);
    }

}
