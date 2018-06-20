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
    Block fatherBlock = null;

    private static final String StartMethode = "[{]$";
    private static final String EndMethode = "[}]$";
    private Pattern patternS = Pattern.compile(StartMethode);
    private Pattern patternE = Pattern.compile(EndMethode);
    private String curMethod;

    MainBlock(ArrayList<String> SjavaLines) {
        super(SjavaLines);
    }


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
        AddDefined();
        for (MethodBlock block : this.Scopes) {
            block.CheckBlock();
        }
        if (this.barketCount != 0) {
            throw new CompEx("illegal num of burkets");
        }
    }

    void addParam(MethodBlock block) throws CompEx {
        Method met = IsDefinedM(this.curMethod);
        ArrayList<Type> param = met.getParameters();
        if (param != null) {
            for (Type t : param) {
                block.PlaceVariavle(t.getType(), t.getName(), t.getisFinal());
            }
        }

    }

    void createScope(ArrayList<String> lines) throws CompEx {
        MethodBlock block = new MethodBlock(lines);
        if (this.curMethod != null) {
            addParam(block);
            this.curMethod = null;
        }
        this.Scopes.add(new MethodBlock(lines));
        this.scopeLines = new ArrayList<String>();
    }

    /*
    update the num of barkets and act accordingly
     */

    void updateBarkets(String line) throws CompEx {
        Matcher matcherStart = patternS.matcher(line);
        Matcher matcherEnd = patternE.matcher(line);
        if (matcherStart.find()) {
            if (this.barketCount == 0) {
                this.curMethod = clearSpaces(getMethodName(line)[0]);
            }
            this.barketCount++;
        }
        if (matcherEnd.find()) {
            if (!line.matches("\\s*[}]\\s*")){
                throw new CompEx("illegal num barkets");
            }
            if (this.barketCount == 1) {
                createScope(this.scopeLines);
            }
            this.barketCount--;
        }
    }

    private void compaileLine(String line) throws CompEx {
        if (this.barketCount > 0) {
            return;
        }
        if (this.barketCount < 0) {
            throw new CompEx("illegal num of barkets");
        }

        CheckLine(line);
    }

    void AddDefined() {
        for (MethodBlock block : this.Scopes) {
            block.AddVars(this.DEFINED_VAR);
            block.setFatherBlock(this);
        }
    }
}
