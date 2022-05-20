package com.golan.amit.fractionstory;

public class FractionHelper {

    private static final int MATH_FACTOR = 5;
    public static final int ROUNDS = 10;
    public static final int ERRORS_PER_FAIL = 3;
    public static final int ASSISTANCE = 3;


    private int leftMone;
    private int leftMekhane;
    private int randomFactor;

    private int sivuv;
    private int error;
    private int fails;

    private int assist;

    /**
     * CTOR
     */

    public FractionHelper() {
        this.sivuv = 0;
        this.error = 0;
        this.fails = 0;
        this.assist = 0;
        init();
    }

    /**
     * Work methods
     */

    public void init() {
        this.leftMone = (int)(Math.random() * MATH_FACTOR + 1);             //  minimum 1
        this.leftMekhane = (int)(Math.random() * MATH_FACTOR + 1 + this.leftMone);
        this.randomFactor = (int)(Math.random() * MATH_FACTOR + 2);         //  minimum multiply by two
    }

    /**
     * Getters Setters and dynamic resolvers
     */

    public int getRightMone() {
        return this.leftMone * this.randomFactor;
    }

    public int getRightMekhane() {
        return this.leftMekhane * this.randomFactor;
    }

    public int getLeftMone() {
        return leftMone;
    }

    public void setLeftMone(int leftMone) {
        this.leftMone = leftMone;
    }

    public int getLeftMekhane() {
        return leftMekhane;
    }

    public void setLeftMekhane(int leftMekhane) {
        this.leftMekhane = leftMekhane;
    }

    public int getSivuv() {
        return sivuv;
    }

    public void setSivuv(int sivuv) {
        this.sivuv = sivuv;
    }

    public void resetSivuv() {
        setSivuv(0);
    }

    public void increaseSivuv() {
        this.sivuv++;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void resetError() {
        setError(0);
    }

    public void increaseError() {
        this.error++;
    }

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    public void increaseFails() {
        this.fails++;
    }

    public void reserFails() {
        setFails(0);
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public void increaseAssist() {
        this.assist++;
    }

    public void resetAssist() {
        setAssist(0);
    }
}
