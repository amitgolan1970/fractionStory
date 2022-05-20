package com.golan.amit.fractionstory;

public class RelationFractionsHelper {

    public static int UPPER_LIMIT = 10;

    AddFractionsHelper.Level level = AddFractionsHelper.Level.Beginner;

    private int leftRelationMone, leftRelationMekhane;
    private int rightRelationMone, rightRelationMekhane;

    public RelationFractionsHelper() {
    }

    public void init() {
        if(level.equals(AddFractionsHelper.Level.Advanced)) {
            UPPER_LIMIT = 20;
        }
        leftRelationMone = (int)(Math.random() * UPPER_LIMIT + 1);
        leftRelationMekhane = (int)(Math.random() * UPPER_LIMIT + 1);
        rightRelationMone = (int)(Math.random() * UPPER_LIMIT + 1);
        rightRelationMekhane = (int)(Math.random() * UPPER_LIMIT + 1);
    }

    /**
     * Resolvers
     */

    public int getCalculatedCommonLeftMone() {
        return this.leftRelationMone * (getCommonBase()  / this.leftRelationMekhane);
    }

    public int getCalculatedCommonRightMone() {
        return this.rightRelationMone * (getCommonBase() / this.rightRelationMekhane);
    }

    public int getCommonBase() {
        if(this.leftRelationMekhane % this.rightRelationMekhane == 0) {
            return this.leftRelationMekhane;
        } else if(this.rightRelationMekhane % this.leftRelationMekhane == 0) {
            return this.rightRelationMekhane;
        }
        int common = getCommonGround();
        if(common != -1)
            return common;
        return this.leftRelationMekhane * this.rightRelationMekhane;
    }

    private int getCommonGround() {
        int common = -1;
        int larger = Math.max(this.leftRelationMekhane, this.rightRelationMekhane);
        int mult = this.leftRelationMekhane * this.rightRelationMekhane;
        for(int i = larger + 1; i < mult -1; i++) {
            if(i % this.leftRelationMekhane == 0 && i % this.rightRelationMekhane == 0) {
                common = i;
                break;
            }
        }
        return common;
    }

    private double leftResult() {
        return (double) this.leftRelationMone / (double) this.leftRelationMekhane;
    }

    private double rightResult() {
        return (double)this.rightRelationMone / (double)this.rightRelationMekhane;
    }

    public boolean isLeftLessThanRight() {
        return leftResult() < rightResult();
    }

    public boolean areEquals() {
        return leftResult() == rightResult();
    }

    public boolean isLeftBiggerThanRight() {
        return leftResult() > rightResult();
    }

    /**
     * Getters
     * @return
     */
    public int getLeftRelationMone() {
        return leftRelationMone;
    }

    public int getLeftRelationMekhane() {
        return leftRelationMekhane;
    }

    public int getRightRelationMone() {
        return rightRelationMone;
    }

    public int getRightRelationMekhane() {
        return rightRelationMekhane;
    }
}
