package oop.ex6.main;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * A class that parse file to ArrayList of lines
 */
public class Parser {

    /*The error message in case of no file error*/
    private static final String NO_FILE_MSG = "ERROR: not found file";

    /*The error message in case of can not read the file error*/
    private static final String CANT_READ_ERR_MSG = "ERROR: can not read the file";

    /*A reader of files*/
    private BufferedReader Reader;

    /*A list that holding all the good lines from the file*/
    private ArrayList<String> GoodLines;


    /*A constant that represent the regex of bad lines case*/
    private final String BADLINES = "^\\/{2,}.*|\\s*";

    /*A constant that represent the pattern of bad line regex*/
    private final Pattern Nolines = Pattern.compile(BADLINES);

    /**
     * initialize parser that convert file to ArrayList of lines without the bad lines
     * @param jfile the file to convert
     * @throws IOEx thrown in case of IO exception
     */
    public Parser(File jfile) throws IOEx {
        try {
            this.Reader = new BufferedReader(new FileReader(jfile));
            this.GoodLines = new ArrayList<String>();
            ParsedFiles(jfile);
        } catch (FileNotFoundException e) {
            throw new IOEx(NO_FILE_MSG);
        }
    }

    /*A method that convert the file to list of good lines*/
    private void ParsedFiles(File jfile) throws IOEx {
        try {
            String curLine = this.Reader.readLine();
            while (curLine != null) {
                Matcher match = Nolines.matcher(curLine);
                if (!match.matches()) {
                    GoodLines.add(curLine);
                }
                curLine = Reader.readLine();

            }
        } catch (IOException e) {
            throw new IOEx(CANT_READ_ERR_MSG);

        }
    }


    /**
     * returns the good lines in the file as list
     * @return the lines list
     */
    public ArrayList<String> getGoodLines() {
        return this.GoodLines;
    }
}




