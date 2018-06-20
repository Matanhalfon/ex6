package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Block {
    static final int CreateType = 1;
    static final int CreateMethod = 2;
    static final int StartLoop = 3;
    static final int CheckLine = 4;
    static final int ENDMETHOD = 5;
    static final int RETURN = 6;
    static final int VARIABLE = 0;
    static final int VALUE = 1;
    private static final int FIRSTWORD = 0;
    private static final String equles = "=";
    static final String RETURNLINE = "return\\s*;?";
    private static final String LINEEND = "line start";
    static final String BLOOKSTART = "Block start";
    private static final String BLOOKEND = "Block end";
    static final String METHODEND = "end parameters";


    private String[] TYPES_Start = new String[]{"boolean", "int", "double", "String", "char", "final"};
    private String METHOD_start = "void";
    ArrayList<String> lines;
    ArrayList<String> scopeLines = new ArrayList<String>();
    HashMap<String, Type> DEFINED_VAR = new HashMap<String, Type>();
    HashMap<String, Method> DEFIND_METHODES = new HashMap<String, Method>();
    //Regex
    private static final String Spacses = "^\\s+|\\s+$";

    private static final String EndLine = ".*[;]$";
    private static final String StartMethode = ".*[{]$";
    private static final String EndMethode = "}";
    private static final String PARAMEND = ".*[)]$";
    private Pattern CLOSBLOOK = Pattern.compile("\\s}{1}\\s*");
    private Pattern IF = Pattern.compile("^if");
    private Pattern WHILE = Pattern.compile("^while");
    private Pattern[] LOOP_STARTERS = new Pattern[]{IF, WHILE};
    Block fatherBlock;


    public Block(ArrayList<String> SjavaLines) {
        this.lines = SjavaLines;
    }

    public Block() {
    }


    /**
     * checks the end fo the line by the line verse
     *
     * @param line   the line to check
     * @param suffix the line type
     * @throws CompEx if it not a legal ender
     */

    String checkEnd(String line, String suffix) throws CompEx {
        String pattern = null;
        switch (suffix) {
            case LINEEND:
                pattern = EndLine;
                break;
            case BLOOKSTART:
                pattern = StartMethode;
                break;
            case BLOOKEND:
                pattern = EndMethode;
                break;
            case METHODEND:
                pattern = PARAMEND;
        }
        if (!line.matches(pattern)) {
            throw new CompEx("illegal end");
        } else {
            return line.substring(0, line.length() - 1);
        }

    }

    /**
     * get the first word of the line
     *
     * @param line the line
     * @return the first word
     */

    String getFirstWord(String line) {
        String[] TheStart = line.split(" ");
        if (TheStart[FIRSTWORD].contains("(")) {//if there is  a method call
            TheStart = TheStart[FIRSTWORD].split("\\(");
        }
        return clearSpaces(TheStart[FIRSTWORD]);
    }

    /**
     * checks if the first word is legal and return what type it is
     *
     * @param line the line to check
     * @return a int that represent the line type  (create variable ,create method block ect..)
     * @throws CompEx if the line does'nt start with a legal starter
     */


    int CheckStart(String line) throws CompEx {
        String start = getFirstWord(line);
        for (String type : TYPES_Start) {
            if (start.matches(type)) {
                return CreateType;
            }
        }
        for (Pattern pattern : LOOP_STARTERS) {
            Matcher matcher = pattern.matcher(start);
            if (matcher.find()) {
                return StartLoop;
            }
        }
        if (start.equals(METHOD_start)) {
            return CreateMethod;
        }
        if ((IsDefinedTGlob(start) != null) || (IsDefinedM(start) != null)) {
            return CheckLine;
        }
        if (start.matches("}")) {
            return ENDMETHOD;
        }
        if (start.matches(RETURNLINE)) {
            checkEnd(line, LINEEND);
            return RETURN;
        }
        throw new CompEx("illegal line starter");
    }


    void CheckLine(String line) throws CompEx {
        int mark = CheckStart(line);
        switch (mark) {
            case CreateType:
                CreateType(line);
                break;
            case CreateMethod:
                CreateMethode(line);
                break;
            case CheckLine:
                CheckStatment(line);
            case ENDMETHOD:
                break;
            default:// get to this line only if does not match any starter
                throw new CompEx("illegal line starter");
        }
    }

    void CheckStatment(String line) throws CompEx {
        line = checkEnd(line, LINEEND);
        String start = getFirstWord(line);
        if (IsDefinedTGlob(start) != null) {
            CheckAssinment(line);
            return;
        }
        if (IsDefinedM(start) != null) {
            if (!CheckMethod_call(line)) {
                throw new CompEx("illegal method call");
            }
            return;
        } else
            throw new CompEx();
    }


    private boolean CheckMethod_call(String line) throws CompEx {
        String[] lineArray = splitbark(line);
        Method met = IsDefinedM(clearSpaces(lineArray[0]));
        if (met != null) {//the method  has been created
            String param = checkEnd(lineArray[1], METHODEND);
            String[] paramArray = param.split(",");
            for (int i = 0; i < paramArray.length; i++) {
                Type parmeter = IsDefinedTGlob(paramArray[i]);
                if (parmeter != null && parmeter.getVar() != null) {
                    paramArray[i] = parmeter.getVar();
                }
            }
            return met.Checkpar(paramArray);
        }
        return false;
    }

    /**
     * a method that check if the assignment is legal
     *
     * @param line the of the assignment
     * @throws CompEx
     */


    private void CheckAssinment(String line) throws CompEx {
        String[] lineArray = line.split(equles);
        if (lineArray.length < 2) {
            throw new CompEx();
        }
        Type arg = IsDefinedTGlob((clearSpaces(lineArray[VARIABLE])));
        assignVal(arg, clearSpaces(lineArray[VALUE]));
    }


    protected void addType(Type variable) {
        this.DEFINED_VAR.put(variable.getName(), variable);
    }

    protected void assignVal(Type arg, String variable) throws CompEx {
        Type value = IsDefinedTGlob(clearSpaces(variable));
        if (value != null) {
            if (value.getVar() == null) {
                throw new CompEx("try to assign null");
            }

            arg.ChangeVar(value.getVar());

        } else {
            arg.ChangeVar(variable);
        }
    }

    Method IsDefinedM(String name) {
        if (DEFIND_METHODES.containsKey(name)) {
            return this.DEFIND_METHODES.get(name);
        }
        return null;

    }

    protected String clearSpaces(String line) {
        return line.replaceAll(Spacses, "");
    }

    Type IsDefinedTGlob(String name) {
        Block block = this;
        if (block.DEFINED_VAR.containsKey(name)) {
            return this.DEFINED_VAR.get(name);
        }
        while (block.fatherBlock != null) {
            block = block.fatherBlock;
            if (block.DEFINED_VAR.containsKey(name)) {
                return block.DEFINED_VAR.get(name);
            }
        }
        return null;
    }


    private String[] splitbark(String line) throws CompEx {
        Pattern pat = Pattern.compile("\\(");
        Matcher mach = pat.matcher(line);
        if (mach.find()) {
            return new String[]{line.substring(0, mach.start()), line.substring(mach.start() + 1, line.length())};
        }
        throw new CompEx("no opning barket");
    }

    String[] getMethodName(String line) throws CompEx {
        line = checkEnd(line, BLOOKSTART);
        String met = getFirstWord(line);
        line = clearSpaces(line.substring(met.length(), line.length()));//remove void
        line = checkEnd(line, METHODEND);
        String[] lineArray = splitbark(line);
        return lineArray;
    }


    /**
     * create a new method
     *
     * @param line create a new method if legal
     * @throws CompEx if it not a legal method to crete
     */

    private void CreateMethode(String line) throws CompEx {

        String[] lineArray = getMethodName(line);
        if (lineArray.length < 1) {
            throw new CompEx();
        }
        String name = lineArray[0];//method name
        String param = lineArray[1];
        name = clearSpaces(name);
        this.scopeLines.clear();
        if (IsDefinedM(name) == null) {
            Method toAdd = null;
            if (lineArray.length < 2 || param.matches(Spacses)) {
                toAdd = new Method(name);
            } else {
                if (param.matches("^,.*")) {
                    throw new CompEx("illegal starter");
                }
                toAdd = new Method(name, lineArray[1].split(","));
            }
            DEFIND_METHODES.put(toAdd.getName(), toAdd);
        } else {
            //the method is already defined
            throw new CompEx("themethod is already defained  ");
        }
    }


    String[] readySet(String line) throws CompEx {
        String[] lineArray = line.split(equles);
        if (lineArray.length != 2) {
            throw new CompEx("didnt get args to set");
        }

        lineArray[VARIABLE] = clearSpaces(lineArray[VARIABLE]);
        lineArray[VALUE] = clearSpaces(lineArray[VALUE]);
        return lineArray;

    }


    void SetVariable(String line, String type, boolean isfinal) throws CompEx {

        String[] lineArray = readySet(line);
        if (IsDefinedTGlob(lineArray[VARIABLE]) == null) {
            Type val = IsDefinedTGlob(lineArray[VALUE]);
            if (val != null) {
                lineArray[VALUE] = assignVariable(val, type);
            }
            Type ToAdd = new Type(type, lineArray[VARIABLE], lineArray[VALUE]);
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

    /*
     * check if possable to to assign the variable
     */

    String assignVariable(Type val, String type) throws CompEx {
        if (val.getVar() != null|| val.isParamter) {
            if (val.getType().equals("double") && type.equals("int")) {
                throw new CompEx("fuck you");
            }
            return val.getVar();
        } else {
            throw new CompEx("try to assign null");
        }

    }

    boolean PlaceVariavle(String type, String name, boolean Isfinal,boolean isparamter) throws CompEx {
        if (Isfinal) {
            throw new CompEx("cannt start final without value");
        }
        name = clearSpaces(name);
        if (IsDefinedTGlob(name) == null) {
            Type toadd = new Type(type, name);
            DEFINED_VAR.put(toadd.getName(), toadd);
            return true;
        }
        return false;
    }


    void CreateType(String line) throws CompEx {

        line = checkEnd(line, LINEEND);
        if (line.endsWith(",")) {
            throw new CompEx("illegal ,");
        }
        String type = getFirstWord(line);
        boolean IsFinal = false;
        if (type.equals("final")) {
            IsFinal = true;
            line = clearSpaces(line.substring(type.length(), line.length()));//cut the final and the space after
            type = getFirstWord(line);
        }
        line = line.substring(type.length() + 1, line.length());//cut the type and the space after
        String[] lineArray = line.split(",");//if it is a multi assignment cut to some parts
        for (String s : lineArray) {
            if (s.contains(equles)) {//if assignment
                SetVariable(s, type, IsFinal);
            } else {
                if (!PlaceVariavle(type, s, IsFinal,false)) {//if declaration todo magic false
                    throw new CompEx("the variable was defined");
                }
            }


        }
    }
}




