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
    ArrayList<String> scopeLines = new ArrayList<String>();
    ArrayList<MethodBlock> methodScopes = new ArrayList<MethodBlock>();

    private static final String StartMethode = "[{]+$";
    private static final String EndMethode = "[}]+$";
    private Pattern patternS = Pattern.compile(StartMethode);
    private Pattern patternE = Pattern.compile(EndMethode);

    MainBlock(ArrayList<String> SjavaLines) {
        super(SjavaLines);
    }


    public void readLines() throws CompEx {
        int index = 0;
        String line = null;
        while (index < this.lines.size()) {
            line = this.lines.get(index).trim();
            while (0 < this.barketCount) {
                scopeLines.add(line);
                index++;
                updateBarkets(line);
                line = this.lines.get(index);
            }
            compaileLine(line);
            updateBarkets(line);
            index++;
        }
    }

    private void updateBarkets(String line) {
        Matcher matcherStart = patternS.matcher(line);
        Matcher matcherEnd = patternE.matcher(line);
        if (matcherStart.find()) {
            this.barketCount++;
        }
        if (matcherEnd.find()) {
            if (this.barketCount == 1) {
                this.methodScopes.add(new MethodBlock(this.scopeLines));
                this.scopeLines.clear();
            }
            this.barketCount--;
        }
    }

    private void compaileLine(String line) throws CompEx {
        if (this.barketCount < 0) {
            throw new CompEx("illegal num of barkets");
        }
        if (this.barketCount == 0) {
            CheckLine(line);
        }

    }

    private void AddDefined() {
        for (MethodBlock block : this.methodScopes) {
            block.AddVars(this.DEFINED_VAR)
            ;
        }
    }
}
