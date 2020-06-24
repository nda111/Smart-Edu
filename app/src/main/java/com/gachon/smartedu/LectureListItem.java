package com.gachon.smartedu;

public class LectureListItem {
    int image;
    String tv1;
    String tv2;

    public LectureListItem(int image, String tv1, String tv2){
        this.image = image;
        this.tv1 = tv1;
        this.tv2 = tv2;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTv1() {
        return tv1;
    }

    public void setTv1(String tv1) {
        this.tv1 = tv1;
    }

    public String getTv2() {
        return tv2;
    }

    public void setTv2(String tv2) {
        this.tv2 = tv2;
    }
}
