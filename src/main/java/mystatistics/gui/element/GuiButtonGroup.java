package mystatistics.gui.element;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonGroup {
    protected List<Item> buttons = new ArrayList<>();

    protected Item selection;

    public void add(Item item) {
        if(item == null || this.buttons.contains(item))
            return;
        this.buttons.add(item);
        if(item.isSelected()) {
            if(this.selection == null) {
                this.selection = item;
            } else {
                item.setSelected(false);
            }
        }
        item.setGroup(this);
    }

    public void remove(Item item) {
        if(item == null)
            return;
        this.buttons.remove(item);
        if(item == this.selection)
            this.selection = null;
        item.setGroup(null);
    }

    public void deselect() {
        for (Item item : this.buttons)
            item.setSelected(false);
    }

    public void clearSelection() {
        if(this.selection != null) {
            Item oldSelection = this.selection;
            this.selection = null;
            oldSelection.setSelected(false);
        }
    }

    public Item getSelection() {
        return this.selection;
    }

    public List<Item> getElements() {
        return this.buttons;
    }

    public void setSelected(Item item, boolean isSelected) {
        if(isSelected && item != null && item != this.selection) {
            Item oldSelection = this.selection;
            this.selection = item;
            if(oldSelection != null)
                oldSelection.setSelected(false);
            item.setSelected(true);
        }
    }

    public boolean isSelected(Item item) {
        return (item == this.selection);
    }

    public int getSelectionIndex() {
        if(this.selection == null) {
            return -1;
        }
        return this.buttons.indexOf(this.selection);
    }

    public static interface Item {
        boolean isSelected();

        void setSelected(boolean paramBoolean);

        GuiButtonGroup getGroup();

        void setGroup(GuiButtonGroup paramGuiButtonGroup);
    }
}
