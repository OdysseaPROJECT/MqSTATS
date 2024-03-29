package mystatistics.gui.element;

import mystatistics.gui.GuiScreenTop;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTextFieldCustom extends Gui {
    private final FontRenderer field_146211_a;

    public int xPosition;

    public int yPosition;

    public int width;

    public int height;

    private String text = "";

    private String mnemonicText = "";

    private int maxStringLength = 32;

    private int cursorCounter;

    private boolean enableBackgroundDrawing = true;

    private boolean canLoseFocus = true;

    private boolean isFocused;

    private boolean isEnabled = true;

    private int lineScrollOffset;

    private int cursorPosition;

    private int selectionEnd;

    private int enabledColor = 14737632;

    private int disabledColor = 7368816;

    private boolean visible = true;

    private static final String __OBFID = "CL_00000670";

    public GuiTextFieldCustom(FontRenderer p_i1032_1_, int p_i1032_2_, int p_i1032_3_, int p_i1032_4_, int p_i1032_5_) {
        this.field_146211_a = p_i1032_1_;
        this.xPosition = p_i1032_2_;
        this.yPosition = p_i1032_3_;
        this.width = p_i1032_4_;
        this.height = p_i1032_5_;
    }

    public void updateCursorCounter() {
        this.cursorCounter++;
    }

    public void setText(String p_146180_1_) {
        if (p_146180_1_.length() > this.maxStringLength) {
            this.text = p_146180_1_.substring(0, this.maxStringLength);
        } else {
            this.text = p_146180_1_;
        }
        setCursorPositionEnd();
    }

    public String getText() {
        return this.text;
    }

    public void setMnemonicText(String text) {
        this.mnemonicText = text;
    }

    public String getMnemonicText() {
        return this.mnemonicText;
    }

    public String getSelectedText() {
        int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    public void writeText(String p_146191_1_) {
        int l;
        String s1 = "";
        String s2 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        int i = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        int j = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - i - this.selectionEnd;
        boolean flag = false;
        if (this.text.length() > 0)
            s1 = s1 + this.text.substring(0, i);
        if (k < s2.length()) {
            s1 = s1 + s2.substring(0, k);
            l = k;
        } else {
            s1 = s1 + s2;
            l = s2.length();
        }
        if (this.text.length() > 0 && j < this.text.length())
            s1 = s1 + this.text.substring(j);
        this.text = s1;
        moveCursorBy(i - this.selectionEnd + l);
    }

    public void deleteWords(int p_146177_1_) {
        if (this.text.length() != 0)
            if (this.selectionEnd != this.cursorPosition) {
                writeText("");
            } else {
                deleteFromCursor(getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
    }

    public void deleteFromCursor(int p_146175_1_) {
        if (this.text.length() != 0)
            if (this.selectionEnd != this.cursorPosition) {
                writeText("");
            } else {
                boolean flag = (p_146175_1_ < 0);
                int j = flag ? (this.cursorPosition + p_146175_1_) : this.cursorPosition;
                int k = flag ? this.cursorPosition : (this.cursorPosition + p_146175_1_);
                String s = "";
                if (j >= 0)
                    s = this.text.substring(0, j);
                if (k < this.text.length())
                    s = s + this.text.substring(k);
                this.text = s;
                if (flag)
                    moveCursorBy(p_146175_1_);
            }
    }

    public int getNthWordFromCursor(int p_146187_1_) {
        return getNthWordFromPos(p_146187_1_, getCursorPosition());
    }

    public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
        return func_146197_a(p_146183_1_, getCursorPosition(), true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        int k = p_146197_2_;
        boolean flag1 = (p_146197_1_ < 0);
        int l = Math.abs(p_146197_1_);
        for (int i1 = 0; i1 < l; i1++) {
            if (flag1) {
                while (p_146197_3_ && k > 0 && this.text.charAt(k - 1) == ' ')
                    k--;
                while (k > 0 && this.text.charAt(k - 1) != ' ')
                    k--;
            } else {
                int j1 = this.text.length();
                k = this.text.indexOf(' ', k);
                if (k == -1) {
                    k = j1;
                } else {
                    while (p_146197_3_ && k < j1 && this.text.charAt(k) == ' ')
                        k++;
                }
            }
        }
        return k;
    }

    public void moveCursorBy(int p_146182_1_) {
        setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    public void setCursorPosition(int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        int j = this.text.length();
        if (this.cursorPosition < 0)
            this.cursorPosition = 0;
        if (this.cursorPosition > j)
            this.cursorPosition = j;
        setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
        if (!this.isFocused)
            return false;
        switch (p_146201_1_) {
            case '\001':
                setCursorPositionEnd();
                setSelectionPos(0);
                return true;
            case '\003':
                GuiScreen.setClipboardString(getSelectedText());
                return true;
            case '\026':
                if (this.isEnabled)
                    writeText(GuiScreen.getClipboardString());
                return true;
            case '\030':
                GuiScreen.setClipboardString(getSelectedText());
                if (this.isEnabled)
                    writeText("");
                return true;
        }
        switch (p_146201_2_) {
            case 14:
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled)
                        deleteWords(-1);
                } else if (this.isEnabled) {
                    deleteFromCursor(-1);
                }
                return true;
            case 199:
                if (GuiScreen.isShiftKeyDown()) {
                    setSelectionPos(0);
                } else {
                    setCursorPositionZero();
                }
                return true;
            case 203:
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
                    } else {
                        setSelectionPos(getSelectionEnd() - 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    setCursorPosition(getNthWordFromCursor(-1));
                } else {
                    moveCursorBy(-1);
                }
                return true;
            case 205:
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
                    } else {
                        setSelectionPos(getSelectionEnd() + 1);
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    setCursorPosition(getNthWordFromCursor(1));
                } else {
                    moveCursorBy(1);
                }
                return true;
            case 207:
                if (GuiScreen.isShiftKeyDown()) {
                    setSelectionPos(this.text.length());
                } else {
                    setCursorPositionEnd();
                }
                return true;
            case 211:
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled)
                        deleteWords(1);
                } else if (this.isEnabled) {
                    deleteFromCursor(1);
                }
                return true;
        }
        if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
            if (this.isEnabled)
                writeText(Character.toString(p_146201_1_));
            return true;
        }
        return false;
    }

    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
        boolean flag = (p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height);
        if (this.canLoseFocus)
            setFocused(flag);
        if (this.isFocused && p_146192_3_ == 0) {
            int l = p_146192_1_ - this.xPosition;
            if (this.enableBackgroundDrawing)
                l -= 4;
            String s = this.field_146211_a.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
            setCursorPosition(this.field_146211_a.trimStringToWidth(s, l).length() + this.lineScrollOffset);
        }
    }

    // drawString :: func_78261_a

    public void drawTextBox() {
        if (getVisible()) {
            if (getEnableBackgroundDrawing())
                drawBackground();
            boolean drawMnemonic = (!this.isFocused && this.text.isEmpty());
            int i = drawMnemonic ? GuiScreenTop.color(98, 105, 121) : (this.isEnabled ? this.enabledColor : this.disabledColor);
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = drawMnemonic ? this.field_146211_a.trimStringToWidth(this.mnemonicText.substring(this.lineScrollOffset), getWidth()) : this.field_146211_a.trimStringToWidth(this.text.substring(this.lineScrollOffset), getWidth());
            boolean flag = (j >= 0 && j <= s.length());
            boolean flag1 = (this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag);
            int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
            int i1 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
            int j1 = l;
            if (k > s.length())
                k = s.length();
            if (s.length() > 0) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = this.field_146211_a.drawString(s1, l, i1, i);
            }
            boolean flag2 = (this.cursorPosition < this.text.length() || this.text.length() >= getMaxStringLength());
            int k1 = j1;
            if (!flag) {
                k1 = (j > 0) ? (l + this.width) : l;
            } else if (flag2) {
                k1 = j1 - 1;
                j1--;
            }
            if (s.length() > 0 && flag && j < s.length())
                this.field_146211_a.drawString(s.substring(j), j1, i1, i);
            if (flag1)
                if (flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.field_146211_a.FONT_HEIGHT, -3092272);
                } else {
                    this.field_146211_a.drawString("_", k1, i1, i);
                }
            if (k != j) {
                int l1 = l + this.field_146211_a.getStringWidth(s.substring(0, k));
                drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.field_146211_a.FONT_HEIGHT);
            }
        }
    }

    public void drawBackground() {
        drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
    }

    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            int i1 = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i1;
        }
        if (p_146188_2_ < p_146188_4_) {
            int i1 = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = i1;
        }
        if (p_146188_3_ > this.xPosition + this.width)
            p_146188_3_ = this.xPosition + this.width;
        if (p_146188_1_ > this.xPosition + this.width)
            p_146188_1_ = this.xPosition + this.width;
        // EXAMPLE OF NEW BUFFER BUILDER -> OLD TESSELLATOR
        // FOR INFO:
        // USE DefaultVertexFormats FOR DIFFERENT FORMS (ALIKE .startDrawingQuads();)
        // INSTEAD OF addVertex(x, y, z) and addVertexWithUV(x, y, z, u, v) USE: pos(x, y, z)
        // finishDrawing();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        GL11.glColor4f(0.0f, 0.0f, 255.0f, 255.0f);
        GL11.glDisable(3553);
        GL11.glEnable(3058);
        GL11.glLogicOp(5387);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(p_146188_1_, p_146188_4_, 0.0D);
        bufferBuilder.pos(p_146188_3_, p_146188_4_, 0.0D);
        bufferBuilder.pos(p_146188_3_, p_146188_2_, 0.0D);
        bufferBuilder.pos(p_146188_1_, p_146188_2_, 0.0D);
        bufferBuilder.finishDrawing();
        GL11.glDisable(3058);
        GL11.glEnable(3553);
    }

    public void setMaxStringLength(int p_146203_1_) {
        this.maxStringLength = p_146203_1_;
        if (this.text.length() > p_146203_1_)
            this.text = this.text.substring(0, p_146203_1_);
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    public void setEnableBackgroundDrawing(boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }

    public void setTextColor(int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }

    public void setDisabledTextColour(int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }

    public void setFocused(boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused)
            this.cursorCounter = 0;
        this.isFocused = p_146195_1_;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public void setEnabled(boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getWidth() {
        return getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }

    public void setSelectionPos(int p_146199_1_) {
        int j = this.text.length();
        if (p_146199_1_ > j)
            p_146199_1_ = j;
        if (p_146199_1_ < 0)
            p_146199_1_ = 0;
        this.selectionEnd = p_146199_1_;
        if (this.field_146211_a != null) {
            if (this.lineScrollOffset > j)
                this.lineScrollOffset = j;
            int k = getWidth();
            String s = this.field_146211_a.trimStringToWidth(this.text.substring(this.lineScrollOffset), k);
            int l = s.length() + this.lineScrollOffset;
            if (p_146199_1_ == this.lineScrollOffset)
                this.lineScrollOffset -= this.field_146211_a.trimStringToWidth(this.text, k, true).length();
            if (p_146199_1_ > l) {
                this.lineScrollOffset += p_146199_1_ - l;
            } else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }
            if (this.lineScrollOffset < 0)
                this.lineScrollOffset = 0;
            if (this.lineScrollOffset > j)
                this.lineScrollOffset = j;
        }
    }

    public void setCanLoseFocus(boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }
}
