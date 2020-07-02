package com.gachon.smartedu.Item;

public class RegisterLectureItem {
    public String lectureName;
    public String pfName; // professor name
    public String credit;
    public String gradeP; // grading policy
    public String maxNum;
    public String LID;

    public RegisterLectureItem(String lectureName, String pfName, String credit, String gradeP, String maxNum, String LID){
        this.lectureName = lectureName;
        this.pfName = pfName;
        this.credit = credit;
        this.gradeP = gradeP;
        this.maxNum = maxNum;
        this.LID = LID;

    }
}
