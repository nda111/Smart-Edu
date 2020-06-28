package com.gachon.smartedu.Item;

public class LectureListItem {
    public String name;
    public String lecture_info;

    // Firebase 데이터 에서 LID = LectureID 가져와야함
    public LectureListItem(String name, String lecture_info){
        this.name = name;
        this.lecture_info = lecture_info;
    }
}
