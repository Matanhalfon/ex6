package oop.ex6.main;

import java.util.ArrayList;

public class ConditionBlock extends MethodBlock {

    private static final String OR = "\\|\\|";
    private static final String AND = "\\&&";
    private static final String INTRegex = "\\s*\\-?\\d+\\s*";
    private static final String DOUBLERegex = "\\s*\\-?\\d*[.]\\d+\\s*";
    private static final String BOOLEANRegex = "\\s*(true){1}\\s*|\\s*(false){1}\\s*";
    private static final String EQUALS = "=";
    private static final String DOUBLEEQLS = "==";
    private static final String INT = "int";
    private static final String BOOLEAN = "boolean";
    private static final String DOUBLE = "double";


    public ConditionBlock(ArrayList<String> SjavaLines) throws CompEx {
        super(SjavaLines);
        this.ismethod = false;
    }


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
//            } else {
//                String[] conInner = var.split(DOUBLEEQLS);
//                Type con = IsDefinedT(conInner[0]);
//                if (!validCondition(con)) {
//                    throw new CompEx("illegal val");
//                }
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
        Type var = IsDefinedT(conditionVars[0]);
        if (condition.matches(BOOLEANRegex)) {
            return true;
        } else if (condition.matches(INTRegex) || condition.matches(DOUBLERegex)) {
            return true;
        } else if (var != null) {
            String val = var.getVar();
            if (val == null) {
                throw new CompEx("no condtion var");
            }
            return (val.matches(INTRegex) || val.matches(BOOLEANRegex) || val.matches(DOUBLERegex));
        }
        return false;
    }
}



