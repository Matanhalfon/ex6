package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method {
    private String name;
    private ArrayList<Type> parameters = null;
    private static final String Spacses = "^\\s+|\\s+$";
    private static final String GoodName = "\\s*[a-zA-Z]\\s*|\\s*[a-zA-Z]+\\w*\\s*";


    Method(String name, String[] parmeters) throws CompEx {
        setName(name);
        BulidParmeters(parmeters);
    }

    Method(String name) throws CompEx {
        setName(name);
    }

    private void isDefinedparam(String name) throws CompEx {
        if (this.parameters != null) {
            for (Type t : this.parameters) {
                if (t.getName().equals(name)) {
                    throw new CompEx("paramter already defned");
                }
            }
        }
    }

    private void setName(String name) throws CompEx {
        Pattern pattern = Pattern.compile(GoodName);// TODO: 6/19/2018 pattern 
        Matcher match = pattern.matcher(name);
        if (match.matches()) {
            this.name = name.trim();
        } else {
            throw new CompEx("illegal name");
        }
    }

    protected String clearSpaces(String line) {
        return line.replaceAll(Spacses, "");
    }


    private ArrayList<String> clearEmepty(String[] param) {
        ArrayList<String> toreturn = new ArrayList<String>();
        for (String aParam : param) {
            if (!aParam.equals("")) {
                toreturn.add(aParam);
            }
        }
        return toreturn;
    }


    private void BulidParmeters(String[] paramArgs) throws CompEx {
        if(!paramArgs[0].equals("")){
            this.parameters=new ArrayList<Type>();
        }
        for (String s : paramArgs) {
            s = clearSpaces(s);
            String[] sArra = s.split("\\s");
            ArrayList<String> sArray = clearEmepty(sArra);
            if (sArray.size() == 3) {
                if (sArray.get(0).equals("final")) {
                    isDefinedparam(sArray.get(2));
                    Type ToAdd = new Type(sArray.get(1), sArray.get(2));
                    ToAdd.setFinal();
                    ToAdd.setParamter();
                    this.parameters.add(ToAdd);
                } else {
                    throw new CompEx("death");
                }
//                else { check if 3 param not final legal
            } else if (sArray.size() == 2) {
                isDefinedparam(sArray.get(1));
                Type ToAdd=new Type(sArray.get(0), sArray.get(1));
                ToAdd.setParamter();
                this.parameters.add(ToAdd);
            } else if (sArray.size() == 0) {
                return;
            } else {
                throw new CompEx("illegal num of parmeters");
            }

        }

    }

    public ArrayList<Type> getParameters() {
        return parameters;
    }

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

    public String getName() {
        return name;
    }
}
