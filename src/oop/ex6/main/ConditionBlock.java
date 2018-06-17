package oop.ex6.main;

import java.util.ArrayList;

public class ConditionBlock extends Block {

    private static final String OR = "\\||";
    private static final String AND = "\\&&";
    private static final String INT = "\\d+";
    private static final String DOUBLE = "\\d*[.]\\d+";
    private static final String BOOLEAN = "[true,false]";
    private static final String STRING = ".+";
    private static final String CHAR = ".{1}";
    private static final String EQUALS = "=";

    /**
     * create a methode that run over the internal lines and throw if needed
     *
     * @param SjavaLines
     */
    public ConditionBlock(String[] SjavaLines) {
        super(SjavaLines);
    }

    private void checkBlock(String[] lines) throws CompEx{
        String first = lines[0];
        checkEnd(first, "{");
        // cut the condition
        String condition = first.substring(first.indexOf("("), first.lastIndexOf(")"));
        CheckCondition(condition);
        for (int i = 1; i < lines.length; i++){
            CheckLine(lines[i]);
        }
    }


    public void AddVars(ArrayList<Type> globalvars) {
        this.DEFINED_VAR.addAll(globalvars);
    }

    private boolean CheckCondition(String condition) throws CompEx {
        String[] conditions = condition.split(OR);
        ArrayList<String> conArry = new ArrayList<String>();
        for (String con : conditions) {
            String[] temp = con.split(AND);
            for (String t : temp) {
                conArry.add(t);
            }
        }
        for (String var : conArry) {
            if (conditionType(var)){
                return true;
            }
            String[] conInner = var.split("==");
            Type con = IsDefinedT(conInner[0]);
            if (con != null && con.getVar()!=null){
                return true;
            }else {
                throw new CompEx();
            }
        }
        return true;
    }

    private boolean conditionType(String condition) throws CompEx {
        String[] conditionVars = condition.split(EQUALS);
        Type var = IsDefinedT(conditionVars[0]);
        if (condition.matches(BOOLEAN)) {
            return true;
        } else if (condition.matches(INT) || condition.matches(DOUBLE)) {
            return true;
        } else if (var != null) {
            if (var.getType().equals(TypesEnm.BOOLEAN) || var.getType().equals(TypesEnm.INT) ||
                    var.getType().equals(TypesEnm.DOUBLE)) {
                if (var.getVar() == null) {
                    throw new CompEx();
                }
                return true;
            }
            return false;
        }
        return false;
    }
}


