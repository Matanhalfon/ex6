package oop.ex6.main;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
    private BufferedReader bufferedReader;
    private ArrayList<String> GOODLINES;
    //REGEX
    String BadLine = "^\\/{2,}.*|[\\s*]";
    Pattern Nolines = Pattern.compile(BadLine);


    public Parser(String jfile) throws IOException {
        ArrayList<String> ParsedFiles = new ArrayList<String>();
        BufferedReader bufferReader = new BufferedReader(new FileReader(jfile));
        ArrayList<String> GOODLIINES = Parsejava(jfile);
    }


    private ArrayList<String> Parsejava(String Path) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        String curLine = bufferedReader.readLine();
        while (curLine != null) {
            Matcher match = Nolines.matcher(curLine);
            if (!match.matches()) {
                lines.add(curLine);
            }
            curLine = bufferedReader.readLine();

        }
        return lines;
    }

    public ArrayList<String> getGOODLINES() {
        return GOODLINES;
    }
}



