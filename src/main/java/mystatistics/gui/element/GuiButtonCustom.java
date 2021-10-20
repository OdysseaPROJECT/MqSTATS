package mystatistics.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonCustom extends GuiButton implements GuiButtonGroup.Item {
    private int buttonColor;

    private int hoverButtonColor;

    private int disabledButtonColor;

    private int selectedButtonColor;

    private int textColor = 14737632;

    private int hoverTextColor = 16777120;

    private int disabledTextColor = 10526880;

    private int selectedTextColor = 16777120;

    private GuiButtonGroup group;

    private boolean toggle;

    public GuiButtonCustom(int id, int x, int y, int width, int height, String label) {
        super(id, x, y, width, height, label);
    }

    public GuiButtonCustom(int id, int x, int y, String label) {
        super(id, x, y, label);
    }

    public GuiButtonCustom(int id, String label) {
        super(id, 0, 0, label);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getButtonColor() {
        return this.buttonColor;
    }

    public GuiButtonCustom setButtonColor(int color) {
        this.buttonColor = color;
        return this;
    }

    public GuiButtonCustom setHoverButtonColor(int color) {
        this.hoverButtonColor = color;
        return this;
    }

    public int getDisabledButtonColor() {
        return this.disabledButtonColor;
    }

    public GuiButtonCustom setDisabledButtonColor(int color) {
        this.disabledButtonColor = color;
        return this;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public int getSelectedButtonColor() {
        return this.selectedButtonColor;
    }

    public GuiButtonCustom setSelectedButtonColor(int color) {
        this.selectedButtonColor = color;
        return this;
    }

    public GuiButtonCustom setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public int getHoverTextColor() {
        return this.hoverTextColor;
    }

    public GuiButtonCustom setHoverTextColor(int color) {
        this.hoverTextColor = color;
        return this;
    }

    public int getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public GuiButtonCustom setDisabledTextColor(int color) {
        this.disabledTextColor = color;
        return this;
    }

    public int getSelectedTextColor() {
        return this.selectedTextColor;
    }

    public GuiButtonCustom setSelectedTextColor(int color) {
        this.selectedTextColor = color;
        return this;
    }

    public GuiButtonCustom setButtonGroup(GuiButtonGroup buttonGroup) {
        this.group = buttonGroup;
        this.group.add(this);
        return this;
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible)
            return;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, !this.enabled ? this.disabledButtonColor : (isSelected() ? this.selectedButtonColor : (this.hovered ? this.hoverButtonColor : this.buttonColor)));
        mouseDragged(mc, mouseX, mouseY);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        List<String> list = mc.fontRenderer.listFormattedStringToWidth(this.displayString, this.width - 8);
        int yPos = mc.fontRenderer.getWordWrappedHeight(this.displayString, this.width - 8);
        for (int i = 0; i < list.size(); i++)
            drawCenteredString(mc.fontRenderer, list.get(i), this.x + this.width / 2 + 1, this.y + (this.height - yPos) / 2 + i * mc.fontRenderer.FONT_HEIGHT, !this.enabled ? this.disabledTextColor : (isSelected() ? this.selectedTextColor : (this.hovered ? this.hoverTextColor : this.textColor)));
        GL11.glDisable(3042);
    }

    public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (this.group != null)
                this.group.setSelected(this, true);
            return true;
        }
        return false;
    }

    public void setSelected(boolean isSelected) {
        this.toggle = isSelected;
    }

    public boolean isSelected() {
        return this.toggle;
    }

    public GuiButtonGroup getGroup() {
        return this.group;
    }

    public void setGroup(GuiButtonGroup buttonGroup) {
        this.group = buttonGroup;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GuiButtonCustom))
            return false;
        GuiButtonCustom o = (GuiButtonCustom)obj;
        return (this.id == o.id);
    }

    public int hashCode() {
        return 31 * this.id;
    }
}
