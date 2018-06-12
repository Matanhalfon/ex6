package oop.ex6.main;

public enum TypesEnm {
    INT("int"), DOUBLE("double"), BOOLEAN("boolean"), CHAR("char"), STRING("String"),;
    private final String TypeText;

    TypesEnm(String TypeText) {
        this.TypeText = TypeText;
    }

    public static TypesEnm getValue(String text) throws CompEx {
        for (TypesEnm e : TypesEnm.values()) {
            if (e.TypeText.equals(text)) {
                return e;
            }

        }
        throw new CompEx();
    }
}
