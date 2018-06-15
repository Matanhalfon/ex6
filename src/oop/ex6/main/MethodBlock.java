package oop.ex6.main;

import java.util.*;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodBlock extends Block {

    private static final String goodName = "^[a-zA-Z]";
    protected ArrayList<Type> DEFINED_VAR;
    private Method method;
    /*
    need to support getting a balck of lines and then run over and throw if needed
     */
    public MethodBlock(String[] SjavaLines) throws CompEx{
        super(SjavaLines);
        AddVars(Block.DEFINED_VAR);
        CheckBlock(lines);

    }

    private void AddVars(ArrayList<Type> globalvars) {
        this.DEFINED_VAR.addAll(globalvars);
    }

    private void CheckBlock(String[] lines) throws CompEx{
        //this.method = buildMethod(lines[0]);
        for (int i = 1; i<lines.length; i++){
            this.CheckLine(lines[i]);
        }
    }
    private Method buildMethod(String firstLine) throws CompEx{
        String methodName = getFirstWord(firstLine);
        if (checkMethodName(methodName)) {
            firstLine = firstLine.substring(firstLine.indexOf("("), firstLine.indexOf(")"));
            String[] parm = firstLine.split(",");
            return new Method(methodName, parm);
        }
        throw new CompEx();
    }

    private boolean checkMethodName(String name){
        if (name.length()==0) {
            return false;
        }
        Pattern pattern = Pattern.compile(goodName);
        Matcher match = pattern.matcher(name);
        return match.find();
    }
}
