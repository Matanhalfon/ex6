package oop.ex6.main;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainBlock extends Block {
    /**
     * run1 over the whole text check line just if the { } count are at 0
     * else just count {} if negative num  throw if finished read and not 0 throw
     * run2 recoursive call every time that meeting a VOID collect the lines until 0 {}
     * and add to a method/condition block object, and call the is OK method from them and sed to the
     * method block get the defined vars clone list  and method list
     * condition block get the actual vars list of the scope and the method list
     *
     * @param SjavaLines the line the main have to check
     */

    int barketCount = 0;
    ArrayList<MethodBlock> Scopes = new ArrayList<MethodBlock>();

    private static final String StartMethode = "[{]$";
    private static final String EndMethode = "[}]$";
    private Pattern ENDBARKET = Pattern.compile("\\s*[}]\\s*");
    private Pattern patternS = Pattern.compile(StartMethode);
    private Pattern patternE = Pattern.compile(EndMethode);
    private String curMethod;

    /**
     * the block of the global scope that parse all the lines and create new scopes
     * for the methods
     *
     * @param SjavaLines the lines that received by the parser and will be checked
     */
    public MainBlock(ArrayList<String> SjavaLines) {
        super(SjavaLines);
    }

    /**
     * the main method it reads the lines of the Sjava code and throw Compilation exception if needed
     * creates the methods scope that will be checked in the end after all the lines was reed and defined
     *
     * @throws CompEx if there is a Compilation error
     */


    public void readLines() throws CompEx {
        int index = 0;
        String line = null;
        while (index < this.lines.size()) {
            line = clearSpaces(this.lines.get(index));
            while (0 < this.barketCount) {
                if (index > this.lines.size()) {
                    throw new CompEx("illegal num of barkests");
                }
                line = clearSpaces(line);
                index++;
                updateBarkets(line);
                scopeLines.add(clearSpaces(line));
                if (index < lines.size()) {
                    line = clearSpaces(lines.get(index));
                }
            }
            if (index < this.lines.size()) {
                compaileLine(line);
                updateBarkets(line);
                index++;
            }
        }
        for (MethodBlock block : this.Scopes) {
            block.setFatherBlock(this);
            block.CheckBlock();
        }
        if (this.barketCount != 0) {
            throw new CompEx("illegal num of burkets");
        }
    }

    /*
    a method that get a kmethod block and add to it the parameter so they  could be defined
     */
    private void addParam(MethodBlock block) throws CompEx {
        Method met = IsDefinedM(this.curMethod);
        ArrayList<Type> param = met.getParameters();
        if (param != null) {
            for (Type t : param) {
                block.PlaceVariavle(t.getType(), t.getName(), t.getisFinal(), t.isParamter());
            }
        }
    }

    /*
    the method that get the lines of the scopes and then create a method block  accordingly
     */

    void createScope(ArrayList<String> lines) throws CompEx {
        MethodBlock block = new MethodBlock(lines);
        if (this.curMethod != null) {
            addParam(block);
            this.curMethod = null;
        }
        this.Scopes.add(block);
        this.scopeLines = new ArrayList<String>();
    }

    /*
    update the barketcount  and act accordingly ,create method block when getting and throw exception
    if getting an  negative number
     */

    void updateBarkets(String line) throws CompEx {
        Matcher matcherStart = patternS.matcher(line);
        Matcher matcherEnd = patternE.matcher(line);
        if (matcherStart.find()) {
            if (this.barketCount == 0) {
                this.curMethod = clearSpaces(getMethodName(line)[METHDNAME]);
            }
            this.barketCount++;
        }
        if (matcherEnd.find()) {
            Matcher mach = ENDBARKET.matcher(line);
            if (!mach.matches()) {
                throw new CompEx("illegal num barkets");
            }
            if (this.barketCount == 1) {
                createScope(this.scopeLines);
            }
            this.barketCount--;
        }
    }
    /*
    checks if the barket count if bigger then 0  will not check yet if negative will throw exception
    otherwise will check the line
     */

    private void compaileLine(String line) throws CompEx {
        if (this.barketCount > 0) {
            return;
        }
        if (this.barketCount < 0) {
            throw new CompEx("illegal num of barkets");
        }
        CheckLine(line);
    }
}
