package oop.ex6.main;

import java.util.regex.*;

public class Type {
    private String var = null;
    private String type;
    private String name;
    private boolean IsFinal = false;
    String[] TYPES_list = new String[]{"boolean", "int", "double", "String", "char"};
    private final int Boolean = 0;
    private final int Int = 1;
    private final int Double = 2;
    private final int string = 3;
    private final int Char = 4;


    //Regex
    private static final String GoodName = "\\s*[_]\\w+\\s*|\\s*[a-zA-Z]+\\w*\\s*";
    private static final String INT = "\\s*\\-?\\d+\\s*";
    private static final String DOUBLE = "\\s*\\-?\\d*[.]\\d+\\s*";
    private static final String BOOLEAN = "\\s*(true){1}\\s*|\\s*(false){1}\\s*";
    private static final String STRING = "\\s*\".*\"\\s*";
    private static final String CHAR = "\\s*\'.{1}\'\\s*";
    private static final Pattern INTP = Pattern.compile(INT);
    private static final Pattern DOUBLEP = Pattern.compile(DOUBLE);
    private static final Pattern BOOLEANP = Pattern.compile(BOOLEAN);
    private static final Pattern STRINGP = Pattern.compile(STRING);
    private static final Pattern CHARP = Pattern.compile(CHAR);
    boolean isParamter = false;

    /**
     * a class that represent a variable
     *
     * @param type the type of the variable
     * @param name the name of the variable
     * @param var  the value of the variable
     * @throws CompEx if  one of the args is illegal
     */
    public Type(String type, String name, String var) throws CompEx {
        setType(type);
        setName(name);
        ChangeVar(var);
    }

    /**
     * a class that represent a variable
     *
     * @param type the type of the variable
     * @param name the name of the variable
     * @throws CompEx if one of  the args is  illegal
     */

    public Type(String type, String name) throws CompEx {
        setType(type);
        setName(name);
    }

    /**
     * @return the value of the variable
     */
    public String getVar() {
        return var;
    }

    /**
     * @return is the variable is final
     */

    public boolean getisFinal() {
        return IsFinal;
    }

    /**
     * a method that changes the type value
     *
     * @param var the value to set in the variable
     * @throws CompEx if it is illegal value
     */
    public void ChangeVar(String var) throws CompEx {
        if (!IsFinal) {
            TypesEnm Enm = TypesEnm.getValue(this.type);
            switch (Enm) {
                case INT:
                    if (var.matches(INT)) {
                        this.var = var;
                        break;
                    } else throw new CompEx("ilegal value");
                case DOUBLE:
                    if (var.matches(INT) || var.matches(DOUBLE)) {
                        this.var = var;
                        break;
                    } else throw new CompEx("ilegal value");
                case CHAR:
                    if (var.matches(CHAR)) {
                        this.var = var;
                        break;
                    } else throw new CompEx("ilegal value C");
                case BOOLEAN:
                    if (var.matches(BOOLEAN) || var.matches(INT) || var.matches(DOUBLE)) {
                        this.var = var;
                        break;
                    } else {
                        throw new CompEx("ilegal value B");
                    }
                case STRING:
                    if (var.matches(STRING)) {
                        this.var = var;
                        break;
                    } else throw new CompEx("ilegal value S");
            }

        }
        if (IsFinal) {
            if (this.var.equals(null)) {
                this.var = var;
            }
            throw new CompEx("try to change final");
        }

    }

    /**
     * @return the variable name
     */

    public String getName() {
        return name;
    }

    /*
     * change the variable name if needed
     * @param name the name to set
     * @throws CompEx if te name is illegal
     */
    private void setName(String name) throws CompEx {
        Pattern pattern = Pattern.compile(GoodName);
        Matcher match = pattern.matcher(name);
        if (match.matches()) {
            this.name = name.trim();
        } else {
            throw new CompEx("illegal name");
        }
    }


    /**
     * set the variable to final
     */

    public void setFinal() {
        IsFinal = true;
    }

    /**
     * @return the variable type
     */

    public String getType() {
        return type;
    }

    /**
     * set the variable to be parameter
     */

    public void setParamter() {
        isParamter = true;
    }

    /**
     * check if the variable is a paramter
     *
     * @return true if parameter false otherwise
     */

    public boolean isParamter() {
        return isParamter;
    }

    /**
     * change  the variable type , only in the constructor
     *
     * @param type the wanted type
     * @throws CompEx if it not a legal type
     */

    private void setType(String type) throws CompEx {
        for (String Type : TYPES_list) {
            if (Type.equals(type)) {
                this.type = type;
                return;
            }
        }
        throw new CompEx("illegal type");


    }
}
