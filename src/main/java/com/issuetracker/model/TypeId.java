package com.issuetracker.model;

/**
 *
 * @author vramik
 */
public enum TypeId {
    
    global(0),
    project(1),
    issue(2),
    comment(3);
    
    private final int value;
    
    private TypeId(int value) {
        this.value = value;
    }
        
    public int getValue() {
        return value;
    }
    
    public static TypeId parse(Integer id) {
        TypeId typeId = null;
        for (TypeId item : TypeId.values()) {
            if (item.getValue() == id) {
                typeId = item;
                break;
            }
        }
        return typeId;
    }
}
