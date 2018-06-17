package oop.ex6.main;
import java.io.*;

import com.sun.tools.javac.Main;

import java.io.IOException;
import java.util.ArrayList;

public class Sjavac {

    public static void main(String[] args)  {
        try {
            File javaFile=new File(args[0]);
            Parser parser = new Parser(javaFile);
            ArrayList<String> SjavaFiles = parser.getGOODLINES();
            MainBlock compaler = new MainBlock(SjavaFiles);
            compaler.readLines();

            } catch(CompEx compEx){
                System.out.println(1);
                return;
            }
            catch (IOException e){
                System.out.println(2);
                return;
            }
        System.out.println(0);
        }


    }
