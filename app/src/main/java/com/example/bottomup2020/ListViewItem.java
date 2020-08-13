package com.example.bottomup2020;

public class ListViewItem {
    private String contentStr;
    private String titleStr;

//    public ListViewItem() {
//        this.contentStr = contentStr;
//        this.titleStr = titleStr;
//    }


    public void setTitle(String title){
        titleStr = title;
    }
    public void setContent(String content) {
        contentStr = content;
    }

    public String getContent() {
        return this.contentStr;
    }
    public String getTitle() {
        return this.titleStr;
    }
}
