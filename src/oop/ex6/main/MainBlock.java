package oop.ex6.main;

public class MainBlock extends Block {
    /**
     * run1 over the whole text check line just if the { } count are at 0
     * else just count {} if negative num  throw if finished read and not 0 throw
     * run2 recoursive call every time that meeting a VOID collect the lines until 0 {}
     * and add to a method/condition block object, and call the is OK method from them
     * @param SjavaLines
     */
    public MainBlock(String[] SjavaLines) {
        super(SjavaLines);
    }
}
