package com.tmlst.testtask.timetableapp;

/**
 *   Класс пункта меню экрана расписания
 */

class MenuItemData {

    private String bigText;
    private String middleText;
    private String smallText;
    private int imageSrc;

    MenuItemData(String bigText, String middleText, String smallText, int imageSrc) {
        this.bigText = bigText;
        this.middleText = middleText;
        this.smallText = smallText;
        this.imageSrc = imageSrc;
    }

    String getBigText() {
        return bigText;
    }

    void setBigText(String bigText) {
        this.bigText = bigText;
    }

    String getMiddleText() {
        return middleText;
    }

    void setMiddleText(String middleText) {
        this.middleText = middleText;
    }

    String getSmallText() {
        return smallText;
    }

    void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    int getImageSrc() {
        return imageSrc;
    }
}