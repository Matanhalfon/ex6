package oop.ex6.main;

import java.util.*;
import java.lang.String;
import java.util.concurrent.locks.Condition;

public class MethodBlock extends MainBlock {

    private static final String goodName = "^[a-zA-Z]";
    private static final String METHOD_END = "return\\s*;";
    private String curCondoition;
    private ArrayList<ConditionBlock> loopsBlock = new ArrayList<ConditionBlock>();
    boolean ismethod = true;


    /*
    need to support getting a balck of lines and then run over and throw if needed
     */
    MethodBlock(ArrayList<String> SjavaLines) throws CompEx {
        super(SjavaLines);

    }

    void setFatherBlock(Block father) {
        this.fatherBlock = father;
    }


    void AddVars(HashMap<String, Type> globalvars) {
        this.DEFINED_VAR.putAll(globalvars);
    }

    @Override
    Method IsDefinedM(String name) {
        Method method = this.fatherBlock.IsDefinedM(name);
        if (method != null) {
            return method;
        } else {
            return null;
        }
    }

    private Boolean isLooping() throws CompEx {
        if (this.barketCount > 0) {
            return true;
        }
        if (this.barketCount == 0) {
            return false;
        } else {
            throw new CompEx("illegal num of barkets in method");
        }
    }

    void CheckBlock() throws CompEx {
        if (lines.size() < 1) {
            return;
        }
        int i = 0;
        String line = null;
        while (i < this.lines.size() ) {
            if((this.ismethod)&&(i==this.lines.size()-1)){
                break;
            }
            line = lines.get(i);
            while (isLooping()) {
                updateBarkets(line);
                if (i < lines.size() - 1) {
                    i++;
                }
                this.scopeLines.add(line);
                line = lines.get(i);

            }
            this.CheckLine(lines.get(i));
            updateBarkets(line);
            i++;
        }
        if (!lines.get(lines.size() - 1).matches(METHOD_END) && this.ismethod) {
            throw new CompEx("no return statment");
        }
//        AddDefined();
//        for (MethodBlock scopes : this.Scopes) {
//            scopes.CheckBlock();
//        }

    }

    @Override
    protected void assignVal(Type arg, String variable) throws CompEx {
        Type value = IsDefinedT(clearSpaces(variable));
        if(arg.getisFinal()){
            throw new CompEx();
        }
        if (value != null) {

            if (value.getVar() == null) {
                throw new CompEx("try to assign null");
            }
            Type ScopeType = new Type(arg.getType(), arg.getName(), arg.getVar());
            this.DEFINED_VAR.put(ScopeType.getName(), ScopeType);
        }
        if (value == null) {
            Type ScopeType = new Type(arg.getType(), arg.getName(), variable);
            this.DEFINED_VAR.put(ScopeType.getName(), ScopeType);
        }
    }


    void AddDefined(MethodBlock block) {
        block.AddVars(this.DEFINED_VAR);
        block.setFatherBlock(this);
    }

    @Override
    void createScope(ArrayList<String> lines) throws CompEx {
        ConditionBlock conBlock = new ConditionBlock(this.scopeLines);
        AddDefined(conBlock);
        if (conBlock.CheckCondition(this.curCondoition)) {
            conBlock.CheckBlock();
//            this.Scopes.add(conBlock);
        } else {
            throw new CompEx("illegal condition");
        }
        this.scopeLines = new ArrayList<String>();
    }


    @Override
    void CheckLine(String line) throws CompEx {
        int mark = CheckStart(line);
        switch (mark) {
            case CreateType:
                CreateType(line);
                break;
            case StartLoop:
                OpenLoop(line);
                break;
            case CheckLine:
                CheckStatment(line);
            case RETURN:
                break;
        }
    }


    private String clearCondtion(String line) throws CompEx {
        line = clearSpaces(checkEnd(line, BLOOKSTART));
        line = checkEnd(line, METHODEND);
        String[] nameArray = line.split("\\(");
        if (nameArray.length <= 1) {
            throw new CompEx("no condition");
        }
        String name = clearSpaces(nameArray[1]);
        int index = line.indexOf("(");
        if (index < 0) {
            throw new CompEx("no barket ");
        }
        return name;

    }

    void OpenLoop(String line) throws CompEx {
        line = clearCondtion(line);
        this.scopeLines.clear();
        this.curCondoition = line;

    }


}