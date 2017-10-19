package com.example.om.basestructure.data;

/**
 * Created by om on 10/17/17.
 */

public class RVItemData {

    private String[] mBrandArray;
    private String[] mYearList;
    private String[] mColorList;

    public RVItemData(String[] brand, String[] year, String[] color) {
        this.mBrandArray = brand;
        this.mYearList = year;
        this.mColorList = color;
    }

    public String[] getBrand() {
        return mBrandArray;
    }

    public void setBrand(String[] brand) {
        this.mBrandArray = brand;
    }

    public String[] getYear() {
        return mYearList;
    }

    public void setYear(String[] year) {
        this.mYearList = year;
    }

    public String[] getColor() {
        return mColorList;
    }

    public void setColor(String[] color) {
        this.mColorList = color;
    }
}
