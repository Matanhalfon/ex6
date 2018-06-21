package oop.ex6.main;

import java.util.ArrayList;

/**
 * subclass of MethodBlock. The class initialize block of condition (while or if cases)
 and implements all the checks that the condition require.
 */
public class ConditionBlock extends MethodBlock {

    /*A constant that represent OR expression*/
    private static final String OR = "\\|\\|";

    /*A constant that represent AND expression*/
    private static final String AND = "\\&&";

    /*A constant that represent the "int" type regex*/
    private static final String INTREGEX = "\\s*\\-?\\d+\\s*";

    /*A constant that represent the "double" type regex*/
    private static final String DOUBLEREGEX = "\\s*\\-?\\d*[.]\\d+\\s*";

    /*A constant that represent the "boolean" type regex*/
    private static final String BOOLEANREGEX = "\\s*(true){1}\\s*|\\s*(false){1}\\s*";

    /*A constant that represent equals*/
    private static final String EQUALS = "=";


    /**
     * A constructor that initialize block of condition
     * @param SjavaLines list of the lines of the block
     * @throws CompEx in case of compilation error
     */
    public ConditionBlock(ArrayList<String> SjavaLines) throws CompEx {
        super(SjavaLines);
        this.ismethod = false;
    }


    /**
     * A method that checks if the condition expression is legal
     * @param condition a string of the condition expression
     * @return true if it legal expression, else thrown exception
     * @throws CompEx in case of wrong condition expression
     */
    boolean CheckCondition(String condition) throws CompEx {
        if (condition == null) {
            return false;
        }
        String[] conditions = condition.split(OR);
        ArrayList<String> conArray = new ArrayList<String>();
        for (String con : conditions) {
            String[] temp = con.split(AND);
            for (String t : temp) {
                conArray.add(t);
            }
        }
        for (String var : conArray) {
            if (!conditionType(clearSpaces(var))) {
                throw new CompEx("illegal val");
            }
        }
        return true;
    }


    private boolean validCondition(Type con) throws CompEx {
        if (con != null) {
            if (con.getVar() != null) {
                if (conditionType(con.getVar())) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean conditionType(String condition) throws CompEx {
        String[] conditionVars = condition.split(EQUALS);
        Type var = IsDefinedTGlob(conditionVars[0]);
        if (condition.matches(BOOLEANREGEX)) {
            return true;
        } else if (condition.matches(INTREGEX) || condition.matches(DOUBLEREGEX)) {
            return true;
        } else if (var != null) {
            String val = var.getVar();
            if (val == null) {
                throw new CompEx("no condtion var");
            }
            return (val.matches(INTREGEX) || val.matches(BOOLEANREGEX) || val.matches(DOUBLEREGEX));
        }
        return false;
    }
}



