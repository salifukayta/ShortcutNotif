package com.shortcut.sol.shortcutnotif;

/**
 * Created by safeki on 11/06/2015.
 */
public enum ConstEnum {

    NOTIFICATION_ID_EXIST("NOTIFICATION_ID_EXIST"),

    GET_NOTIFICATION("com.shortcut.sol.shortcutnotif.action.GET_NOTIFICATION"),

    GET_NOTIFICATION_RESPONSE("com.shortcut.sol.shortcutnotif.action.GET_NOTIFICATION_RESPONSE"),

    NOTIFICATION_ID("NOTIFICATION_ID");

    private final String value;

    ConstEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
