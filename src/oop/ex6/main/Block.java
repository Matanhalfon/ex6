package oop.ex6.main;

import java.util.regex.*;

import java.util.ArrayList;

public abstract class Block {
    private static final int CreateType = 1;
    private static final int CreateMethod = 2;
    private static final int StartLoop = 3;


    String[] TYPES_Start = new String[]{"boolean", "int", "double", "String", "char", "final"};
    String[] LOOP_STARTERS = new String[]{"while", "if"};
    String METHOD = "void";
    String[] lines;
    ArrayList<String> DEFINED_VAR = new ArrayList<String>();
    ArrayList<String> DEFIND_METHODES = new ArrayList<String>();
    //Regex
    static final String EndLines = "[{};]+$";
    static final String EmpteyLine = "\\s*";// TODO: 6/12/2018 find emepty lines and not add them to the checked one

    public Block(String[] SjavaLines) {
        this.lines = SjavaLines;
    }

    public void checkEnd(String line) throws CompEx {
        Pattern pattern = Pattern.compile(EndLines);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new CompEx();
        }
    }

    private String getFirstWord(String start) {
        String[] thestart = start.split(" ");
        return thestart[0];
    }


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
        throw new CompEx();
    }


    public void CheckLine(String line) throws CompEx {
        checkEnd(line);
        int mark = CheckStart(line);
        switch (mark) {
            case CreateType:
                break;
            case StartLoop:
                break;
            case CreateMethod:
                break;
        }
    }


}




