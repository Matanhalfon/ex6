package oop.ex6.main;

import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Block {
    private static final int CreateType = 1;
    private static final int CreateMethod = 2;
    private static final int StartLoop = 3;
    private static final int CheckLine = 4;
    private static final int CheckMethod = 5;
    private static final int VARIABLE = 0;
    private static final int VALUE = 1;
    private static final int FIRSTWORD = 0;
    private static final String equles = "=";
    private static final String VARS = "vars";
    private static final String METHOD = "METHOD";
    private static final String LINEEND = "line start";
    private static final String BLOOKSTART = "Block start";
    private static final String BLOOKEND = "Block end";


    String[] TYPES_Start = new String[]{"boolean", "int", "double", "String", "char", "final"};
    String[] LOOP_STARTERS = new String[]{"while", "if"};
    String METHOD_start = "void";
    ArrayList<String> lines;
    ArrayList<Type> DEFINED_VAR = new ArrayList<Type>();
    ArrayList<Method> DEFIND_METHODES = new ArrayList<Method>();
    //Regex
    static final String Spacses = "^\\s+|\\s+$";
    private static String Equels="=";
    Pattern equePat=Pattern.compile(Equels);
    private static final String EndLine = ";";
    private static final String StartMethode = "\\{";
    private static final String EndMethode = "}";

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

    private String checkEnd(String line, String suffix) throws CompEx {
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

        }
        if (!line.endsWith(pattern)) {
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
        String[] TheStart = line.split(" ");
        if (TheStart[FIRSTWORD].contains("\\(")) {//if there is  a method call
            TheStart = TheStart[FIRSTWORD].split("\\(");
        }
        return TheStart[FIRSTWORD];
    }

    /**
     * checks if the first word is legal and return what type it is
     *
     * @param line the line to check
     * @return a int that represent the line type  (create variable ,create method block ect..)
     * @throws CompEx if the line does'nt start with a legal starter
     */


    private int CheckStart(String line) throws CompEx {
        String start = getFirstWord(line);
        for (String type : TYPES_Start) {
            if (type.equals(start)) {
                return CreateType;
            }
        }

        for (String str : LOOP_STARTERS) {

            if (str.equals(start)) {
                return StartLoop;
            }
        }
        if (start.equals(METHOD)) {
            return CreateMethod;
        }
        if ((IsDefinedT(start) != null) || (IsDefinedM(start) != null)) {
            return CheckLine;
        }
        throw new CompEx("illegal line starter");
    }


    void CheckLine(String line) throws CompEx {
        int mark = CheckStart(line);
        switch (mark) {
            case CreateType:
                CreateType(line);
                break;
            case StartLoop:
                break;
            case CreateMethod:
                CreateMethode(line);
                break;
            case CheckLine:
                CheckStatment(line);
        }
    }

    private void CheckStatment(String line) throws CompEx {
        line = checkEnd(line, LINEEND);
        String start = getFirstWord(line);
        if (IsDefinedT(start) != null) {
            CheckAssinment(line);
            return;
        }
        if (IsDefinedM(start) != null) {
            if (!CheckMethod_call(line)) {
                throw new CompEx();
            }
            return;
        }else
        throw new CompEx();
    }


    private boolean CheckMethod_call(String line) throws CompEx {
        String[] lineArray = line.split("\\(");
        Method met = IsDefinedM(lineArray[0]);
        if (met != null) {//the method  has been created
            String param = lineArray[1].substring(0, lineArray[1].length() - 1);
            String[] paramArray = param.split(",");
            for (int i = 0; i < paramArray.length; i++) {
                Type parmeter = IsDefinedT(paramArray[i]);
                if (parmeter != null) {
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
        Type arg = IsDefinedT((clearSpaces(lineArray[VARIABLE])));
        Type var = IsDefinedT(clearSpaces(lineArray[VALUE]));
        if (var != null) {
            if (var.getVar() == null) {
                throw new CompEx();
            }
            arg.ChangeVar(var.getVar());
        }
        if (var == null) {
            arg.ChangeVar(lineArray[VALUE]);
        }
    }

    private Method IsDefinedM(String name) {
        for (Method m : DEFIND_METHODES) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;

    }

    protected String clearSpaces(String line) {
        return line.replaceAll(Spacses, "");
    }

    private Type IsDefinedT(String name) {
        for (Type t : DEFINED_VAR) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }


    /**
     * create a new method
     *
     * @param line create a new method if legal
     * @throws CompEx if it not a legal method to crete
     */

    private void CreateMethode(String line) throws CompEx {

        line = checkEnd(line, BLOOKSTART);
        String met = getFirstWord(line);
        line = line.substring(met.length(), line.length());
        String name = getFirstWord(line);
        if (IsDefinedM(name) == null) {
            String[] lineArray = line.split("\\(");
            Method toAdd = new Method(lineArray[0], lineArray[1].split(","));
            DEFIND_METHODES.add(toAdd);
        } else {
            //the method is already defined
            throw new CompEx();
        }
    }


    private void SetVariable(String line, String type, boolean isfinal) throws CompEx {

        String[] lineArray = line.split(equles);
        lineArray[VARIABLE] = clearSpaces(lineArray[VARIABLE]);
        lineArray[VALUE] = clearSpaces(lineArray[VALUE]);
        if (IsDefinedT(lineArray[VARIABLE]) == null) {
            Type val = IsDefinedT(lineArray[VALUE]);
            if (val != null) {
                if (val.getVar() != null) {
                    if (val.getType().equals("double") && type.equals("int")) {
                        throw new CompEx("fuck you");
                    }
                    lineArray[VALUE] = val.getVar();
                } else {
                    throw new CompEx("try to assign null");
                }
            }
            Type ToAdd = new Type(type, lineArray[VARIABLE], lineArray[VALUE]);
            if (isfinal) {
                ToAdd.setFinal();
                DEFINED_VAR.add(ToAdd);
            } else {
                this.DEFINED_VAR.add(ToAdd);
            }
        } else {
            throw new CompEx("variablewas assigned ");
        }

    }

    private boolean PlaceVariavle(String type, String name, boolean Isfinal) throws CompEx {
        if (Isfinal) {
            throw new CompEx("cannt start final without value");
        }
        name = clearSpaces(name);
        if (IsDefinedT(name) == null) {
            Type toadd = new Type(type, name);
            DEFINED_VAR.add(toadd);
            return true;
        }
        return false;
    }
    private boolean containsEqules(String line)throws CompEx {
        int count=0;
        Matcher mach=equePat.matcher(line);
        while (mach.find()){
            count++;
        }
        if(count==1){
            return true;
        }
        if (count==0){
            return false;
        }
        else{
            throw new CompEx();
        }
    }


    private void CreateType(String line) throws CompEx {

        line = checkEnd(line, LINEEND);
        if (line.endsWith(",")) {
            throw new CompEx();
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
            if (containsEqules(s)) {//if assignment
                SetVariable(s, type, IsFinal);
            } else {
                if (!PlaceVariavle(type, s, IsFinal)) {//if declaration
                    throw new CompEx();
                }
            }


        }
    }
}




