package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method {
    private String name;
    private static final int STARTER = 0;
    private ArrayList<Type> parameters = null;
    private static final String Spaces = "\\s";
    private static final String Spacses = "^\\s+|\\s+$";
    private static final String GoodName = "\\s*[a-zA-Z]\\s*|\\s*[a-zA-Z]+\\w*\\s*";
    private Pattern GOODNAMEPAT = Pattern.compile(GoodName);
    private static final int PARAMNAME = 1;
    private static final int PARAMETER = 2;
    String nothing = "";
    String FINAL = "final";


    /**
     * a class that represent the method
     *
     * @param name       the method name
     * @param parameters the  method parameters
     * @throws CompEx if the name or parmeters is  illegal
     */
    public Method(String name, String[] parameters) throws CompEx {
        setName(name);
        BulidParmeters(parameters);
    }

    /**
     * a class that represent the method
     *
     * @param name the method name
     * @throws CompEx if  it is  an illegal name
     */

    public Method(String name) throws CompEx {
        setName(name);
    }
    /*
    check if a String is  a defined parameter
     */

    private void isDefinedparam(String name) throws CompEx {
        if (this.parameters != null) {
            for (Type t : this.parameters) {
                if (t.getName().equals(name)) {
                    throw new CompEx("paramter already defned");
                }
            }
        }
    }
    /*
    change the name
     */

    private void setName(String name) throws CompEx {
        Matcher match = GOODNAMEPAT.matcher(name);
        if (match.matches()) {
            this.name = name.trim();
        } else {
            throw new CompEx("illegal name");
        }
    }

    /**
     * clear the spaces
     *
     * @param line an input line
     * @return the line without spaces
     */

    protected String clearSpaces(String line) {
        return line.replaceAll(Spacses, nothing);
    }

    /*
     method that remove the "" form the String list
     */
    private ArrayList<String> clearEmepty(String[] param) {
        ArrayList<String> toreturn = new ArrayList<String>();
        for (String aParam : param) {
            if (!aParam.equals("")) {
                toreturn.add(aParam);
            }
        }
        return toreturn;
    }
    /*
    a method that create the parameters
     */


    private void BulidParmeters(String[] paramArgs) throws CompEx {
        if (!paramArgs[STARTER].equals(nothing)) {
            this.parameters = new ArrayList<Type>();
        }
        for (String s : paramArgs) {
            s = clearSpaces(s);
            String[] sArra = s.split(Spaces);
            ArrayList<String> sArray = clearEmepty(sArra);
            if (sArray.size() == 3) {
                if (sArray.get(STARTER).equals(FINAL)) {
                    isDefinedparam(sArray.get(PARAMETER));
                    Type ToAdd = new Type(sArray.get(PARAMNAME), sArray.get(PARAMETER));
                    ToAdd.setFinal();
                    ToAdd.setParamter();
                    this.parameters.add(ToAdd);
                } else {
                    throw new CompEx("death");
                }
//                else { check if 3 param not final legal
            } else if (sArray.size() == 2) {
                isDefinedparam(sArray.get(PARAMNAME));
                Type ToAdd = new Type(sArray.get(STARTER), sArray.get(PARAMNAME));
                ToAdd.setParamter();
                this.parameters.add(ToAdd);
            } else if (sArray.size() == 0) {
                return;
            } else {
                throw new CompEx("illegal num of parmeters");
            }
        }
    }

    /**
     * @return the method parameters
     */

    public ArrayList<Type> getParameters() {
        return parameters;
    }

    /**
     * checks if the parameters is legal
     *
     * @param param the parameters to check
     * @return if legal
     * @throws CompEx if it is illegal parameters
     */

    public boolean Checkpar(String[] param) throws CompEx {
        if (this.parameters == null) {
            if (param[0].equals("")) {
                return true;
            }
            if (param.length > 0) {
                throw new CompEx("ya?");
            } else {
                return true;
            }
        }
        if (param.length == this.parameters.size()) {
            if (this.parameters.size() == 0) {
                return true;
            }
            for (int i = 0; i < param.length; i++) {
                this.parameters.get(i).ChangeVar(param[i]);
            }
            return true;
        }
        return false;
    }

    /**
     * @return the  method name
     */

    public String getName() {
        return name;
    }
}
