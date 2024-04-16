package cn.ussshenzhou.util;

import java.util.function.BiConsumer;

/**
 * @author USS_Shenzhou
 */
public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple(A pA, B pB) {
        this.a = pA;
        this.b = pB;
    }

    public A getA() {
        return this.a;
    }

    public void setA(A pA) {
        this.a = pA;
    }

    public B getB() {
        return this.b;
    }

    public void setB(B pB) {
        this.b = pB;
    }

    public void foreach(BiConsumer<A, B> action) {
        action.accept(this.a, this.b);
    }
}
