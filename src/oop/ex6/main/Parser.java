package oop.ex6.main;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * A class that parse the file to lines array
 */
public class Parser {
    /*A constant that represent the message in case of not found file*/
    private static final String NO_FILE_ERR_MSG = "ERROR: not found file";

    /*A constant that represent the message in case of problem in the file reading*/
    private static final String CANT_READ_ERR_MSG = "ERROR: can not read the file";
    /*A buffer reader*/
    private BufferedReader Reader;

    /*Array List that holding all the lines*/
    private ArrayList<String> GoodLines;

    /*A constant that represent the regex of bad line*/
    private final String BADLINE = "^\\/{2,}.*|\\s*";

    /*A constant that represent the pattern of the regex of bad line*/
    private final Pattern NOLINE = Pattern.compile(BADLINE);


    /**
     * Initialize a parser that convert the file to ArrayList that hold all the good
     * lines.
     * @param jfile the Sjavac file we needed to convert
     * @throws IOEx in case of IO exception
     */
    public Parser(File jfile) throws IOEx {
        try {
            ArrayList<String> ParsedFiles = new ArrayList<String>();
            this.Reader = new BufferedReader(new FileReader(jfile));
            this.GoodLines = new ArrayList<String>();
            ParsedFiles(jfile);
        } catch (FileNotFoundException e) {
            throw new IOEx(NO_FILE_ERR_MSG);
        }
    }

    /**
     * A method that gets file and convert it to ArrayList without the lines it need to ignore
     * @param jfile the file we want to convert
     * @throws IOEx in case of IO exception
     */
    private void ParsedFiles(File jfile) throws IOEx {
        try {
            String curLine = this.Reader.readLine();
            while (curLine != null) {
                Matcher match = NOLINE.matcher(curLine);
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
     * retuens the ArrayList of the good lines
     * @return the list of the good lines
     */
    public ArrayList<String> getGoodLines() {
            return GoodLines;
        }
    }



