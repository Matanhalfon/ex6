package oop.ex6.main;

import java.util.ArrayList;

public class Method {
    private String name;
    private ArrayList<Type> parameters;

    public Method(String name, String[] parmeters) {
        this.name = name;
        BulidParmeters(parmeters);
    }


    private void BulidParmeters(String[] paramArgs) {
        for (String s : paramArgs) {
            String[] sArray = s.split(" ");
            if (sArray.length == 3) {
                if (sArray[0].equals("final")) {
                    Type ToAdd = new Type(sArray[1], sArray[2]);
                    ToAdd.setFinal();
                    this.parameters.add(ToAdd);
                }
//                else { check if 3 param not final legal
            }
            this.parameters.add(new Type(sArray[0], sArray[1]));
        }
    }

    public boolean Checkpar(String[] param) throws CompEx {
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
