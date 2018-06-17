package oop.ex6.main;

import java.util.*;

public class MethodBlock extends Block {
    /*
    need to support getting a balck of lines and then run over and throw if needed
     */
    public MethodBlock(ArrayList<String> SjavaLines) {
        super(SjavaLines);
    }

    public void AddVars(ArrayList<Type> globalvars) {
        this.DEFINED_VAR.addAll(globalvars);
    }
}
