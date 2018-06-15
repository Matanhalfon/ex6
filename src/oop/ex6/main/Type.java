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
    private static final String GoodName = "^[_]\\w+|[a-zA-Z]+\\w";
    private static final String INT = "\\d+";
    private static final String DOUBLE = "\\d*[.]\\d+";
    private static final String BOOLEAN = "[true,false]";
    private static final String STRING = ".+";
    private static final String CHAR = ".{1}";
    private static final Pattern INTP = Pattern.compile(INT);
    private static final Pattern DOUBLEP = Pattern.compile(DOUBLE);
    private static final Pattern BOOLEANP = Pattern.compile(BOOLEAN);
    private static final Pattern STRINGP = Pattern.compile(STRING);
    private static final Pattern CHARP = Pattern.compile(CHAR);


    Type(String type, String name, String var) throws CompEx {
        this.type = type;
        this.name = name;
        ChangeVar(var);
    }

    Type(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getVar() {
        return var;
    }


    public void ChangeVar(String var) throws CompEx {

        if (!IsFinal) {
            TypesEnm Enm = TypesEnm.getValue(var);
            switch (Enm) {
                case INT:
                    if (var.matches(INT)) {
                        this.var = var;
                        break;
                    } else throw new CompEx();
                case DOUBLE:
                    if (var.matches(INT) || var.matches(DOUBLE)) {
                        this.var = var;
                        break;
                    } else throw new CompEx();
                case CHAR:
                    if (var.matches(CHAR)) {
                        this.var = var;
                        break;
                    } else throw new CompEx();
                case BOOLEAN:
                    if (var.matches(BOOLEAN) || var.matches(INT) || var.matches(DOUBLE)) {
                        this.var = var;
                        break;
                    }
                case STRING:
                    if (var.matches(STRING)) {
                        this.var = var;
                        break;
                    } else throw new CompEx();
            }

        }
        if (IsFinal) {
            if (this.var.equals(null)) {
                this.var = var;
            }
            throw new CompEx();
        }

    }

    public String getName() {
        return var;
    }

    private void setName(String name) throws CompEx {
        Pattern pattern = Pattern.compile(GoodName);
        Matcher match = pattern.matcher(name);
        if (match.find()) {
            this.var = name;
        } else {
            throw new CompEx();
        }
    }

    public void setFinal() {
        IsFinal = true;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) throws CompEx {
        for (String Type : TYPES_list) {
            if (Type.equals(type)) {
                this.type = type;
                return;
            }
        }
        throw new CompEx();


    }
}
