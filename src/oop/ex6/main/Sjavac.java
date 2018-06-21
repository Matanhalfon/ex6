package oop.ex6.main;


import java.io.*;
import java.util.ArrayList;

/**
 * This class is the main class. It implements all the checks for the Sjavac
 * file by using other classes.
 */
public class Sjavac {

    /*A constant of the wrong number of arguments message*/
    private static final String ARGS_ERR_MSG = "ERROR: to many arguments";

    /*A constant that represent the path to the file in the commend line*/
    private static final int PATH = 0;

    /*A constant that represent the printed line in compilation error case*/
    private static final int COMPEX = 1;

    /*A constant that represent the good length of the arguments line*/
    private static final int GOODLENGTH = 1;

    /*A constant that represent the printed line in IO error case*/
    private static final int IOEX = 2;

    /*A constant that represent the printed line in case of all checks pass*/
    private static final int ALLGOOD = 0;

    /**
     * The main method. Gets the file path as argument, convert it to Array of lines, and
     * implements all the checks that requires.
     * @param args the path to the Sjavac file
     */
    public static void main(String[] args)  {
        try {
            if (args.length!= GOODLENGTH) {
                throw new IOEx(ARGS_ERR_MSG);
            }
            File javaFile=new File(args[PATH]);
            Parser parser = new Parser(javaFile);
            ArrayList<String> SjavaFiles = parser.getGoodLines();
            MainBlock compaler = new MainBlock(SjavaFiles);
            compaler.readLines();

            } catch(CompEx compEx){
                System.out.println(COMPEX);
                return;
            }
            catch (IOEx e){
                System.out.println(IOEX);
                System.err.println(e.getMessage());
                return;
            }
        System.out.println(ALLGOOD);
        }

    }
