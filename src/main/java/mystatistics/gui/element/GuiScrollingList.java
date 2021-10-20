package mystatistics.gui.element;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScrollingList<T> extends Gui {
    public static final float DEFAULT_SCROLL_SPEED = 10.0F;

    protected final GuiScreen parent;

    protected final Minecraft mc;

    protected int x;

    protected int y;

    protected int width;

    protected int height;

    protected int entryHeight;

    protected int scrollOffset;

    protected float scrollSpeed;

    protected int hoverIndex;

    protected int selected;

    protected int mouseX;

    protected int mouseY;

    protected int hoverColor;

    protected int selectedColor;

    protected int textColor;

    protected int selectedTextColor;

    protected int hoverTextColor;

    protected int sliderColor;

    protected int sliderBackgroundColor;

    protected int sliderOffset;

    protected int sliderWidth;

    protected boolean drawHoverColor;

    protected boolean drawSelectedColor;

    protected boolean drawSliderBackground;

    protected boolean canLoseFocus;

    protected boolean canSelect;

    private boolean dragging;

    private int dragged;

    private int mouseYOffset;

    protected List<T> elementData;

    public GuiScrollingList(GuiScreen screen, int listEntryHeight) {
        this(screen, 0, 0, 100, 50, listEntryHeight);
    }

    public GuiScrollingList(GuiScreen screen, int posX, int posY, int listWidth, int listHeight, int listEntryHeight) {
        this.parent = screen;
        this.mc = this.parent.mc;
        this.width = listWidth;
        this.height = listHeight;
        this.x = posX;
        this.y = posY;
        this.entryHeight = listEntryHeight;
        this.scrollSpeed = 10.0F;
        this.hoverIndex = -1;
        this.selected = -1;
        this.hoverColor = 839518730;
        this.selectedColor = -1778384896;
        this.sliderColor = (new Color(0, 0, 0, 125)).getRGB();
        this.sliderBackgroundColor = (new Color(14, 18, 30)).getRGB();
        this.textColor = this.selectedTextColor = this.hoverTextColor = -1;
        this.sliderOffset = 1;
        this.sliderWidth = 3;
        this.drawHoverColor = true;
        this.drawSelectedColor = true;
        this.canLoseFocus = true;
        this.canSelect = true;
        this.drawSliderBackground = true;
        this.elementData = Lists.newArrayList();
    }

    public void onEntryClicked(T entry, int index, int mouseX, int mouseY, int button) {}

    public void drawEntry(T entry, int index, int x, int y, boolean hovered) {
        drawCenteredString(this.mc.fontRenderer, entry.toString(), x + this.width / 2, y + this.entryHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2, isSelected(index) ? this.selectedTextColor : (hovered ? this.hoverTextColor : this.textColor));
    }

    public void drawEntryForeground(T entry, int index, int x, int y, boolean hovered) {}

    public int getSize() {
        return this.elementData.size();
    }

    public int getContentSize() {
        return getSize() * this.entryHeight;
    }

    public boolean isSelected(int index) {
        return (index == this.selected);
    }

    public boolean isSelected() {
        return (this.selected >= 0);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hoverIndex != -1) {
            if (this.canSelect)
                this.selected = this.hoverIndex;
            T entry = getElement(this.hoverIndex);
            if (entry != null)
                onEntryClicked(entry, this.hoverIndex, mouseX, mouseY, button);
        } else if (this.canLoseFocus) {
            this.selected = -1;
        }
        int start = getContentSize() - this.height;
        if (start > 0) {
            int scrollBarXStart = this.x + this.width + this.sliderOffset;
            int scrollBarXEnd = scrollBarXStart + this.sliderWidth;
            int length = this.height * this.height / getContentSize();
            if (length < 8)
                length = 8;
            if (length > this.height - 8)
                length = this.height - 8;
            int end = this.scrollOffset * (this.height - length) / start + this.y;
            if (end < this.y)
                end = this.y;
            if (mouseX > scrollBarXStart && mouseY >= end && mouseX < scrollBarXEnd && mouseY < end + length) {
                this.dragging = true;
                this.mouseYOffset = mouseY;
            }
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int button) {
        if (this.dragging) {
            this.scrollOffset += (mouseY - this.mouseYOffset) * getContentSize() / this.height;
            if (this.scrollOffset > getContentSize() - this.height)
                this.scrollOffset = getContentSize() - this.height;
            if (this.scrollOffset < 0)
                this.scrollOffset = 0;
            this.mouseYOffset = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        this.dragging = false;
    }

    public void handleMouseInput() {
        if (isMouseOver()) {
            int delta = Mouse.getDWheel();
            if (delta != 0) {
                if (delta > 0) {
                    delta = -1;
                } else if (delta < 0) {
                    delta = 1;
                }
                int maxScrollOffset = Math.max(0, getSize() * this.entryHeight - this.height);
                this.scrollOffset = (int)Math.max(Math.min(this.scrollOffset + delta * this.scrollSpeed, maxScrollOffset), 0.0F);
            }
        }
    }

    public void updateScreen() {
        if (isMouseOver()) {
            this.hoverIndex = (this.mouseY - this.y + this.scrollOffset) / this.entryHeight;
            if (this.hoverIndex >= getSize() || this.hoverIndex < 0)
                this.hoverIndex = -1;
        } else {
            this.hoverIndex = -1;
        }
    }

    public void drawScreen(int mX, int mY, float ticks) {
        this.mouseX = mX;
        this.mouseY = mY;
        GL11.glEnable(3089);
        glScissor(this.x, this.y, this.width, this.height);
        int currentY = this.y - this.scrollOffset;
        for (int l = 0; l < getSize(); l++) {
            if (currentY >= this.y - this.entryHeight && currentY <= this.y + this.height) {
                boolean isHover = (this.hoverIndex == l);
                T entry = getElement(l);
                if (entry != null) {
                    if (isSelected(l) && this.drawSelectedColor)
                        drawRect(this.x, currentY, this.x + this.width, currentY + this.entryHeight, this.selectedColor);
                    if (isHover && this.drawHoverColor)
                        drawRect(this.x, currentY, this.x + this.width, currentY + this.entryHeight, this.hoverColor);
                    drawEntry(entry, l, this.x, currentY, isHover);
                }
            }
            currentY += this.entryHeight;
        }
        GL11.glDisable(3089);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        int start = getContentSize() - this.height;
        if (start > 0) {
            int length = this.height * this.height / getContentSize();
            if (length < 8)
                length = 8;
            if (length > this.height - 8)
                length = this.height - 8;
            int end = this.scrollOffset * (this.height - length) / start + this.y;
            if (end < this.y)
                end = this.y;
            int scrollBarXStart = this.x + this.width + this.sliderOffset;
            int scrollBarXEnd = scrollBarXStart + this.sliderWidth;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.drawSliderBackground)
                drawRect(scrollBarXStart, this.y, scrollBarXEnd, this.y + this.height, this.sliderBackgroundColor);
            drawRect(scrollBarXStart, end + length, scrollBarXEnd, end, this.sliderColor);
        }
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        currentY = this.y - this.scrollOffset;
        for (int i = 0; i < getSize(); i++) {
            if (currentY >= this.y - this.entryHeight && currentY <= this.y + this.height) {
                boolean isHover = (this.hoverIndex == i);
                T entry = getElement(i);
                if (entry != null)
                    drawEntryForeground(entry, i, this.x, currentY, isHover);
            }
            currentY += this.entryHeight;
        }
    }

    public void cleanUp() {
        this.hoverIndex = -1;
        this.selected = -1;
        this.scrollOffset = 0;
        this.elementData.clear();
    }

    public boolean isMouseOver() {
        return (this.mouseX >= this.x && this.mouseX < this.x + this.width && this.mouseY >= this.y && this.mouseY < this.y + this.height);
    }

    public List<T> getElements() {
        return this.elementData;
    }

    public void clear() {
        this.elementData.clear();
    }

    public T removeFirstElement() {
        return (this.elementData.size() > 0) ? this.elementData.remove(0) : null;
    }

    public T removeLastElement() {
        return (this.elementData.size() > 0) ? this.elementData.remove(this.elementData.size() - 1) : null;
    }

    public T removeSelectedElement() {
        return isSelected() ? this.elementData.remove(getSelectedIndex()) : null;
    }

    public boolean removeElement(T element) {
        return this.elementData.remove(element);
    }

    public T removeElement(int index) {
        return this.elementData.remove(index);
    }

    public void setElements(List<T> elements) {
        this.elementData = elements;
    }

    public boolean addElement(T element) {
        return this.elementData.add(element);
    }

    public boolean addElements(T... elements) {
        return this.elementData.addAll(Arrays.asList(elements));
    }

    public boolean addElements(Collection<? extends T> elements) {
        return this.elementData.addAll(elements);
    }

    public T getElement(int index) {
        return (index >= 0 && index < this.elementData.size()) ? this.elementData.get(index) : null;
    }

    public T getSelectedElement() {
        return getElement(this.selected);
    }

    public float getScrollSpeed() {
        return this.scrollSpeed;
    }

    public GuiScrollingList setScrollSpeed(float speed) {
        this.scrollSpeed = speed;
        return this;
    }

    public int getHoverColor() {
        return this.hoverColor;
    }

    public GuiScrollingList setHoverColor(int color) {
        this.hoverColor = color;
        return this;
    }

    public int getSelectedColor() {
        return this.selectedColor;
    }

    public GuiScrollingList setSelectedColor(int color) {
        this.selectedColor = color;
        return this;
    }

    public int getSliderColor() {
        return this.sliderColor;
    }

    public GuiScrollingList setSliderColor(int color) {
        this.sliderColor = color;
        return this;
    }

    public boolean isDrawHoverColor() {
        return this.drawHoverColor;
    }

    public GuiScrollingList setDrawHoverColor(boolean draw) {
        this.drawHoverColor = draw;
        return this;
    }

    public boolean isDrawSelectedColor() {
        return this.drawSelectedColor;
    }

    public GuiScrollingList setDrawSelectedColor(boolean draw) {
        this.drawSelectedColor = draw;
        return this;
    }

    public boolean isDrawSliderBackground() {
        return this.drawSliderBackground;
    }

    public GuiScrollingList setDrawSliderBackground(boolean draw) {
        this.drawSliderBackground = draw;
        return this;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public GuiScrollingList setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public int getHoverTextColor() {
        return this.hoverTextColor;
    }

    public GuiScrollingList setHoverTextColor(int color) {
        this.hoverTextColor = color;
        return this;
    }

    public int getSelectedTextColor() {
        return this.selectedTextColor;
    }

    public GuiScrollingList setSelectedTextColor(int color) {
        this.selectedTextColor = color;
        return this;
    }

    public int getSliderBackgroundColor() {
        return this.sliderBackgroundColor;
    }

    public GuiScrollingList setSliderBackgroundColor(int color) {
        this.sliderBackgroundColor = color;
        return this;
    }

    public int getSliderOffset() {
        return this.sliderOffset;
    }

    public GuiScrollingList setSliderOffset(int offset) {
        this.sliderOffset = offset;
        return this;
    }

    public int getSliderWidth() {
        return this.sliderWidth;
    }

    public GuiScrollingList setSliderWidth(int width) {
        this.sliderWidth = width;
        return this;
    }

    public int getScrollOffset() {
        return this.scrollOffset;
    }

    public void setScrollOffset(int offset) {
        this.scrollOffset = offset;
    }

    public int getSelectedIndex() {
        return this.selected;
    }

    public void setSelectedIndex(int index) {
        this.selected = index;
    }

    public boolean canLoseFocus() {
        return this.canLoseFocus;
    }

    public GuiScrollingList setCanLoseFocus(boolean loseFocus) {
        this.canLoseFocus = loseFocus;
        return this;
    }

    public boolean canSelect() {
        return this.canSelect;
    }

    public GuiScrollingList setCanSelect(boolean select) {
        this.canSelect = select;
        return this;
    }

    public GuiScreen getParent() {
        return this.parent;
    }

    public void setPosition(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getEntryHeight() {
        return this.entryHeight;
    }

    public int getHoverIndex() {
        return this.hoverIndex;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    @SideOnly(Side.CLIENT)
    public static void glScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();
        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - y * scale;
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
}
