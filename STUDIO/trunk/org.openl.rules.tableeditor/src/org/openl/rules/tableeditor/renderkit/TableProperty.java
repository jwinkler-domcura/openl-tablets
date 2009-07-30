package org.openl.rules.tableeditor.renderkit;

/**
 * Temporary class for holding table properties
 * @author DLiauchuk
 *
 */
public class TableProperty {
    
    private String displayName;
    private Object value;
    private Class<?> type;
    private boolean show;
    private boolean canEdit;
    private String group;
    
    public TableProperty(String displayName, Object value, Class<?> type, String group) {
        this.displayName = displayName;
        this.value = value;
        this.type = type;
        this.group = group;
    }
    
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
