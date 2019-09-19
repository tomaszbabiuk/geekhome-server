package com.geekhome.common;

public class SelectableObject extends NamedObject {
    private boolean _selected;

    @Persistable(name="Selected")
    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean value) {
        _selected = value;
    }

    public SelectableObject(DescriptiveName name, boolean selected) {
        super(name);
        _selected = selected;
    }
}
