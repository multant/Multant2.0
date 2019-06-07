package com.androstock.multant.ActiveDesk;

public class ActiveDeskCheckBox {
    private String text_checkbox = "";
    private String id = "";
    private String checked;

    public ActiveDeskCheckBox(String text_checkbox, String id, boolean checked) {
        this.text_checkbox = text_checkbox;
        this.id = id;
        this.checked = Boolean.toString(checked);
    }

    public ActiveDeskCheckBox() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText_checkbox(String text_checkbox) {
        this.text_checkbox = text_checkbox;
    }

    public String getText_checkbox() {
        return text_checkbox;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String isChecked() {
        return checked;
    }
}
