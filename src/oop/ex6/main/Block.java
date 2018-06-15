package oop.ex6.main;

import java.util.regex.*;

import java.util.ArrayList;


public abstract class Block {
    private static final int CreateType = 1;
    private static final int CreateMethod = 2;
    private static final int StartLoop = 3;
    private static final int CheckLine = 4;
    private static final int CheckMethod = 5;
    private static final String equles = "=";
    private static final String VARS = "vars";
    private static final String METHOD = "METHOD";
    private static final String LINEEND = "line start";
    private static final String BLOOKSTART = "Block start";
    private static final String BLOOKEND = "Block end";


    String[] TYPES_Start = new String[]{"boolean", "int", "double", "String", "char", "final"};
    String[] LOOP_STARTERS = new String[]{"while", "if"};
    String METHOD_start = "void";
    String[] lines;
    static ArrayList<Type> DEFINED_VAR = new ArrayList<Type>();
    static ArrayList<Method> DEFIND_METHODES = new ArrayList<Method>();
    //Regex
    static final String EndLine = "[;]+$";
    static final String StartMethode = "[{]+$";
    static final String EndMethode = "[}]+$";
    static final String EmpteyLine = "\\s*";// TODO: 6/12/2018 find emepty lines and not add them to the checked one

    public Block(String[] SjavaLines) {
        this.lines = SjavaLines;
    }

    /**
     * checks the end fo the line by the line verse
     *
     * @param line   the line to check
     * @param suffix the line type
     * @throws CompEx if it not a legal ender
     */

    public void checkEnd(String line, String suffix) throws CompEx {
        Pattern pattern = null;
        switch (suffix) {
            case LINEEND:
                pattern = Pattern.compile(EndLine);
            case BLOOKSTART:
                pattern = Pattern.compile(StartMethode);
            case BLOOKEND:
                pattern = Pattern.compile(EndMethode);

        }
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new CompEx();
        }
    }

    /**
     * get the first word of the line
     *
     * @param line the line
     * @return the first word
     */

    protected String getFirstWord(String line) {
        String[] TheStart = line.split(" ");
        if (TheStart[0].contains("\\(")) {//if there is  a method call
            TheStart = TheStart[0].split("\\(");
        }
        return TheStart[0];
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
        if (DEFINED_VAR.contains(start)) {
            return CheckLine;
        }
        if (DEFIND_METHODES.contains(start)) {
            return CheckMethod;
        }
        throw new CompEx();
    }


    public void CheckLine(String line) throws CompEx {
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
                CheckAssinment(line);
        }
    }

    private void Checkstatment(String line) throws CompEx {
        checkEnd(line, LINEEND);
        String start = getFirstWord(line);
        if (this.DEFINED_VAR.contains(start)) {
            CheckAssinment(line);
        }
        if (this.DEFIND_METHODES.contains(start)) {
            if (!CheckMethod_call(line)) {
                throw new CompEx();
            }
        }
        throw new CompEx();
    }


    private boolean CheckMethod_call(String line) throws CompEx {
        String[] lineArray = line.split("\\(");
        Method met = IsDefinedM(lineArray[0]);
        if (met != null) {//the method  has been created
            String param = lineArray[1].substring(0, lineArray[1].length() - 2);
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


    private void CheckAssinment(String line) throws CompEx {
        String[] lineArray = line.split(equles);
        if (lineArray.length < 2) {
            throw new CompEx();
        }
        Type arg = IsDefinedT(lineArray[0]);
        Type var = IsDefinedT(lineArray[1]);
        if (var != null) {
            arg.ChangeVar(var.getVar());
        }
        if (var == null) {
            arg.ChangeVar(lineArray[1]);
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

        checkEnd(line, BLOOKSTART);
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

    private void CreateType(String line) throws CompEx {
        checkEnd(line, LINEEND);
        String type = getFirstWord(line);
        boolean IsFinal = false;
        if (type.equals("final")) {
            IsFinal = true;
            line = line.substring(type.length(), line.length());
            type = getFirstWord(line);
        }
        line = line.substring(type.length(), line.length());
        String[] lineArray = line.split(",");
        for (String s : lineArray) {
            if (s.contains(equles)) {
                String[] sArray = s.split(equles);
                if (IsDefinedT(sArray[0]) == null) {// TODO: 6/14/2018 magic
                    Type ToAdd = new Type(type, sArray[0], sArray[1]);
                    if (IsFinal) {
                        ToAdd.setFinal();
                        DEFINED_VAR.add(ToAdd);
                    }
                } else {
                    throw new CompEx();
                }
            } else {
                if (IsDefinedT(s) == null) {
                    Type ToAdd = new Type(type, s);
                    if (IsFinal) {
                        ToAdd.setFinal();
                        DEFINED_VAR.add(ToAdd);
                    } else throw new CompEx();

                }
            }


        }
    }
}




