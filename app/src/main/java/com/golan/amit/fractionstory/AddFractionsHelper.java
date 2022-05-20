package com.golan.amit.fractionstory;

import android.util.Log;

public class AddFractionsHelper {

    private int FIRST_UPPER_LIMIT = 18;
    private int SECOND_UPPER_LIMIT = 10;

    private int leftMone;
    private int leftMekhane;
    private int rightMone;
    private int rightMekhane;

    public static enum Level {
        Beginner, Advanced
    }

    private Level level = Level.Advanced;

    /**
     * Construction
     */
    public AddFractionsHelper() {
    }

    public void init() {
        if(level.equals(Level.Beginner)) {
            if (MainActivity.DEBUG) {
                Log.i(MainActivity.DEBUGTAG, "working on beginner mode level");
            }
            FIRST_UPPER_LIMIT = 8;
            SECOND_UPPER_LIMIT = 5;
        }

        this.leftMekhane = (int) (Math.random() * FIRST_UPPER_LIMIT + 2);   //  [2-20] on advanced mode, [2-10] beginner mode
        if (this.leftMekhane == 2) {
            this.leftMone = 1;
        } else {
            this.leftMone = (int) (Math.random() * (this.leftMekhane - 1) + 1); //  [1-mekhane]
        }

        if (this.leftMekhane <= SECOND_UPPER_LIMIT) {
            this.rightMekhane = (int) (Math.random() * FIRST_UPPER_LIMIT + 2);
        } else {
            this.rightMekhane = (int) (Math.random() * SECOND_UPPER_LIMIT + 2);
        }
        if (this.rightMekhane == 2) {
            this.rightMone = 1;
        } else {
            this.rightMone = (int) (Math.random() * (this.rightMekhane - 1) + 1);
        }
    }

    /**
     * Logic
     */

    private int getCommonGround() {
        int common = -1;
        int larger = Math.max(this.leftMekhane, this.rightMekhane);
        int mult = this.leftMekhane * this.rightMekhane;
        for (int i = larger + 1; i < mult - 1; i++) {
            if (i % this.leftMekhane == 0 && i % this.rightMekhane == 0) {
                common = i;
                break;
            }
        }
        return common;
    }

    public int getAddResultMekhane() {
        //  case 1:
        if (this.leftMekhane % this.rightMekhane == 0) {
            return this.leftMekhane;
        } else if (this.rightMekhane % this.leftMekhane == 0) {
            return this.rightMekhane;
        }
        //  case 2:
        int common = getCommonGround();
        if(common != -1)
            return common;
        //  case 3:
        return this.leftMekhane * this.rightMekhane;
    }

    public int getAddResultMone() {
        int result = -1;
        if (this.leftMekhane % this.rightMekhane == 0) {
            return this.leftMone + (this.rightMone * this.leftMekhane / this.rightMekhane);
        } else if (this.rightMekhane % this.leftMekhane == 0) {
            return this.rightMone + (this.leftMone * this.rightMekhane / this.leftMekhane);
        }
        int common = getCommonGround();
        if (common != -1) {
            return this.leftMone * (common / this.leftMekhane) + this.rightMone * (common / this.rightMekhane);
        }
        int mult = this.leftMekhane * this.rightMekhane;
        return this.leftMone * this.rightMekhane + this.rightMone * this.getLeftMekhane();
    }

    /**
     * Getters & Setters & result resolvers
     */

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

    public int getRightMone() {
        return rightMone;
    }

    public void setRightMone(int rightMone) {
        this.rightMone = rightMone;
    }

    public int getRightMekhane() {
        return rightMekhane;
    }

    public void setRightMekhane(int rightMekhane) {
        this.rightMekhane = rightMekhane;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
