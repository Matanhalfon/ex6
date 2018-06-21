package oop.ex6.main;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
    private BufferedReader Reader;
    private ArrayList<String> GOODLINES;
    //REGEX
    private String BadLine = "^\\/{2,}.*|\\s*";
    private Pattern Nolines = Pattern.compile(BadLine);


    public Parser(File jfile) throws IOEx {
        try {
            this.Reader = new BufferedReader(new FileReader(jfile));
            this.GOODLINES = new ArrayList<String>();
            ParsedFiles(jfile);
        } catch (FileNotFoundException e) {
            throw new IOEx("ERROR: not found file");
        }
    }

    private void ParsedFiles(File jfile) throws IOEx {
        try {
            String curLine = this.Reader.readLine();
            while (curLine != null) {
                Matcher match = Nolines.matcher(curLine);
                if (!match.matches()) {
                    GOODLINES.add(curLine);
                }
                curLine = Reader.readLine();

            }
        } catch (IOException e) {
            throw new IOEx("ERROR: can not read the file");

        }
    }


    public ArrayList<String> getGOODLINES() {

        return GOODLINES;
    }
}



