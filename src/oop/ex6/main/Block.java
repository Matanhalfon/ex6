package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Block {
    static final int CreateType = 1;
    private static final int CreateMethod = 2;
    static final int StartLoop = 3;
    static final int CheckLine = 4;
    private  static final int ENDMETHOD = 5;
    static final int RETURN = 6;
    static final int VARIABLE = 0;
    static final int VALUE = 1;
    private static final int PARAMETERS = 1;
    protected static final int METHDNAME = 0;
    private static final boolean NotParameter = false;
    private static final String INT="int";
    private static final String DOUBLE="double";
    private static final String FINAL="final";
    private static final int FIRSTWORD = 0;
    private static final String COMA = ",";
    private static final String equles = "=";
    static final String nothing = "";
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
    static final String RETURNLINE = "return\\s*;?";
    private static final String Spacses = "^\\s+|\\s+$";
    private static final String SpacesCUt = "\\s";
    static final String BARKET = "\\(";
    private static final String EQUELS = "=";
    private static final String StartWithComa = "^,.*";
    private static final String EndLine = ".*[;]$";
    private static final String StartMethode = ".*[{]$";
    private static final String EndMethode = "}";
    private static final String PARAMEND = ".*[)]$";
    private Pattern CLOSBLOOK = Pattern.compile("\\s}{1}\\s*");
    private Pattern IF = Pattern.compile("^if");
    private Pattern WHILE = Pattern.compile("^while");
    private Pattern BARKETCOM = Pattern.compile(BARKET);
    private Pattern[] LOOP_STARTERS = new Pattern[]{IF, WHILE};
    Block fatherBlock;


    public Block(ArrayList<String> SjavaLines) {
        this.lines = SjavaLines;
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

    private String getFirstWord(String line) {
        String[] TheStart = line.split(SpacesCUt);
        if (TheStart[FIRSTWORD].contains("(")) {//if there is  a method call
            TheStart = TheStart[FIRSTWORD].split(BARKET);
        }
        if (TheStart[FIRSTWORD].contains(EQUELS)) {
            TheStart = TheStart[FIRSTWORD].split(EQUELS);
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
    /*
    a method that checks the line start and act accordingly
     */

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
    /*
    a method that check if a statement is legal
     */

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
        Method met = IsDefinedM(clearSpaces(lineArray[METHDNAME]));
        if (met != null) {//the method  has been created
            String param = checkEnd(lineArray[PARAMETERS], METHODEND);
            String[] paramArray = param.split(COMA);
            for (int i = 0; i < paramArray.length; i++) {
                Type parameter = IsDefinedTGlob(paramArray[i]);
                if (parameter != null && parameter.getVar() != null) {
                    paramArray[i] = parameter.getVar();
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
            throw new CompEx("try to assign null");
        }
        Type arg = IsDefinedTGlob((clearSpaces(lineArray[VARIABLE])));
        assignVal(arg, clearSpaces(lineArray[VALUE]));
    }

    /*
    a method that assign a val to an exerting variable
     */
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

    /*
    a method that search for a method by it name
    return - the method , null if not defined
     */

    Method IsDefinedM(String name) {
        if (DEFIND_METHODES.containsKey(name)) {
            return this.DEFIND_METHODES.get(name);
        }
        return null;

    }


    /*
    a method that clear spaces
     */

    String clearSpaces(String line) {
        return line.replaceAll(Spacses, nothing);
    }

    /*
    a method that search for a variable if defined in the anywhere in this scope or the above ones
    return the looked variable null if not defined
     */

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

    /*
    a method that split the line by it first Barket
     */
    private String[] splitbark(String line) throws CompEx {
        Pattern pat = BARKETCOM;
        Matcher mach = pat.matcher(line);
        if (mach.find()) {
            return new String[]{line.substring(0, mach.start()), line.substring(mach.start() + 1, line.length())};
        }
        throw new CompEx("no opening barket");
    }
    /*
    get the method name from the lne where it is declared
     */

    String[] getMethodName(String line) throws CompEx {
        line = checkEnd(line, BLOOKSTART);
        String met = getFirstWord(line);
        line = clearSpaces(line.substring(met.length(), line.length()));//remove void
        line = checkEnd(line, METHODEND);
        return splitbark(line);
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
                if (param.matches(StartWithComa)) {
                    throw new CompEx("illegal starter");
                }
                toAdd = new Method(name, lineArray[1].split(COMA));
            }
            DEFIND_METHODES.put(toAdd.getName(), toAdd);
        } else {
            //the method is already defined
            throw new CompEx("the method is already defained  ");
        }
    }

    /*
    a method that get the an array of the sets variable
     */
    String[] readySet(String line) throws CompEx {
        String[] lineArray = line.split(equles);
        if (lineArray.length != 2) {
            throw new CompEx("didnt get args to set");
        }
        lineArray[VARIABLE] = clearSpaces(lineArray[VARIABLE]);
        lineArray[VALUE] = clearSpaces(lineArray[VALUE]);
        return lineArray;

    }

    /*
    set a new variable by name and value and add to the define variables
     */

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
            throw new CompEx("variable was assigned ");
        }
    }

    /*
     * check if possable to to assign the variable
     */

    String assignVariable(Type val, String type) throws CompEx {
        if (val.getVar() != null || val.isParamter) {
            if (val.getType().equals(DOUBLE) && type.equals(INT)) {
                throw new CompEx("try to assgin");
            }
            return val.getVar();
        } else {
            throw new CompEx("try to assign null");
        }

    }

    /*
    set a variable only by name and add to this scope Defined variables
     */

    boolean PlaceVariavle(String type, String name, boolean Isfinal, boolean isparamter) throws CompEx {
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

    /*
        a method that get a line and crate a type accordingly
     */


    void CreateType(String line) throws CompEx {

        line = checkEnd(line, LINEEND);
        if (line.endsWith(COMA)) {
            throw new CompEx("illegal ,");
        }
        String type = getFirstWord(line);
        boolean IsFinal = false;
        if (type.equals(FINAL)) {
            IsFinal = true;
            line = clearSpaces(line.substring(type.length(), line.length()));//cut the final and the space after
            type = getFirstWord(line);
        }
        line = line.substring(type.length() + 1, line.length());//cut the type and the space after
        String[] lineArray = line.split(COMA);//if it is a multi assignment cut to some parts
        for (String s : lineArray) {
            if (s.contains(equles)) {//if assignment
                SetVariable(s, type, IsFinal);
            } else {
                if (!PlaceVariavle(type, s, IsFinal, NotParameter)) {//if declaration
                    throw new CompEx("the variable was defined");
                }
            }


        }
    }
}




