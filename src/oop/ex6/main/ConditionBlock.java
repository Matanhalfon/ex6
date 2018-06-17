package oop.ex6.main;

import java.util.ArrayList;

public class ConditionBlock extends Block {
    /**
     * create a methode that run over the internal lines and throw if needed
     *
     * @param SjavaLines
     */
    public ConditionBlock(ArrayList<String> SjavaLines) {
        super(SjavaLines);
    }


    public void AddVars(ArrayList<Type> globalvars){
        this.DEFINED_VAR.addAll(globalvars);
    }
}
