package oop.ex6.main;

import java.util.*;
import java.lang.String;

public class MethodBlock extends MainBlock {

    private static final String METHOD_END = "return\\s*;";
    private String curCondoition;
    boolean ismethod = true;
    private static final int CONNAME = 1;


    /*
    representing the method scope , getting the methods line
     */
    public MethodBlock(ArrayList<String> SjavaLines) throws CompEx {
        super(SjavaLines);

    }

    /*
    set the block father to a wanted block
     */

    void setFatherBlock(Block father) {
        this.fatherBlock = father;
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
    /*
    true if the method is reading a if\while block
     */

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

    /*
     *checks the method block
     * @throws CompEx - if the there is a compilation error
     */

    void CheckBlock() throws CompEx {
        if (lines.size() < 1) {
            return;
        }
        int i = 0;
        String line = null;
        while (i < this.lines.size()) {
            if ((this.ismethod) && (i == this.lines.size() - 1)) {
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
    }

    @Override
    protected void assignVal(Type arg, String variable) throws CompEx {
        Type value = IsDefinedTGlob(clearSpaces(variable));
        if (arg.getisFinal()) {
            throw new CompEx();
        }
        if (value != null) {
            if (value.getVar() == null && !value.isParamter) {
                throw new CompEx("try to assign null");
            }
            Type ScopeType = null;
            if (!value.isParamter) {
                ScopeType = new Type(arg.getType(), arg.getName(), arg.getVar());
            } else {
                ScopeType = new Type(arg.getType(), arg.getName());
            }
            this.DEFINED_VAR.put(ScopeType.getName(), ScopeType);
        }
        if (value == null) {
            Type ScopeType = new Type(arg.getType(), arg.getName(), variable);
            this.DEFINED_VAR.put(ScopeType.getName(), ScopeType);
        }
    }


    @Override
    void createScope(ArrayList<String> lines) throws CompEx {
        ConditionBlock conBlock = new ConditionBlock(this.scopeLines);
        conBlock.setFatherBlock(this);
        if (conBlock.CheckCondition(this.curCondoition)) {
            conBlock.CheckBlock();
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


    @Override
    void SetVariable(String line, String type, boolean isfinal) throws CompEx {
        String[] lineArray = readySet(line);
        if (IsDefinedT(lineArray[VARIABLE]) == null) {
            Type val = IsDefinedTGlob(lineArray[VALUE]);
            Type ToAdd = null;
            if (val != null) {
                lineArray[VALUE] = assignVariable(val, type);
                if (val.isParamter) {
                    ToAdd = new Type(type, lineArray[VARIABLE]);
                    ToAdd.setParamter();
                } else {
                    ToAdd = new Type(type, lineArray[VARIABLE], lineArray[VALUE]);

                }
            } else {
                ToAdd = new Type(type, lineArray[VARIABLE], lineArray[VALUE]);
            }
            if (isfinal) {
                ToAdd.setFinal();
                DEFINED_VAR.put(ToAdd.getName(), ToAdd);
            } else {
                this.DEFINED_VAR.put(ToAdd.getName(), ToAdd);
            }
        } else {
            throw new CompEx("variablewas assigned ");
        }

    }

    @Override
    boolean PlaceVariavle(String type, String name, boolean Isfinal, boolean ismeparamter) throws CompEx {
        if (Isfinal) {
            throw new CompEx("cannt start final without value");
        }
        name = clearSpaces(name);
        if (IsDefinedT(name) == null) {
            Type toadd = new Type(type, name);
            if (ismeparamter) {
                toadd.setParamter();
            }
            this.DEFINED_VAR.put(toadd.getName(), toadd);
            return true;
        }
        return false;
    }

    /*
     * @param name of  the variable
     * @return the variable if defined in the current scope null else
     */

    private Type IsDefinedT(String name) {
        Block block = this;
        if (block.DEFINED_VAR.containsKey(name)) {
            return this.DEFINED_VAR.get(name);
        }

        return null;
    }
    /*
    get a line and only return the condition in it (first word)
     */

    private String clearCondtion(String line) throws CompEx {
        line = clearSpaces(checkEnd(line, BLOOKSTART));
        line = checkEnd(line, METHODEND);
        String[] nameArray = line.split(BARKET);
        if (nameArray.length <= 1) {
            throw new CompEx("no condition");
        }
        String name = clearSpaces(nameArray[CONNAME]);
        int index = line.indexOf(BARKET);
        if (index < 0) {
            throw new CompEx("no barket ");
        }
        return name;

    }
    /*
    start  a loop start the condition
     lines clear and get the method name
     */

    private void OpenLoop(String line) throws CompEx {
        line = clearCondtion(line);
        this.scopeLines.clear();
        this.curCondoition = line;

    }


}