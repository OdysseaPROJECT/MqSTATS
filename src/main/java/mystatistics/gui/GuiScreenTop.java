package mystatistics.gui;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import mystatistics.MyStatisticMod;
import mystatistics.MyStatisticsConfig;
import mystatistics.gui.element.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenTop extends GuiScreen {
    public static Map<String, List<Triple<Integer, String, String>>> tableValues = Maps.newHashMap();

    public static Map<String, List<Pair<String, Integer>>> tableLastWinners = Maps.newHashMap();

    public static Map<String, Triple<Integer, String, String>> currentPlayer = Maps.newHashMap();

    private GuiButtonCustom buttonClose;

    private GuiButtonCustom[] buttonTab;

    private GuiButtonGroup tabGroup;

    private GuiScrollingList listTop;

    private GuiScrollingList listLastWinners;

    private GuiScrollingPane paneDescription;

    private GuiTextFieldCustom textFieldSearch;

    private MyStatisticsConfig.Table[] tables;

    private boolean isInitialized;

    public boolean searching;

    private int searchDelay;

    private String[] progress = new String[]{".", "..", "..."};

    public GuiScreenTop() {
        this.tables = MyStatisticMod.config.getTables();
    }

    public void initialize() {
        this.buttonList.clear();
        this.buttonList.add(this.buttonClose = (new GuiButtonCustom(0, I18n.format("gui.top.close", new Object[0]))).setButtonColor(color(183, 34, 62)).setHoverButtonColor(color(170, 41, 65)));
        this.tabGroup = new GuiButtonGroup();
        this.buttonTab = new GuiButtonCustom[this.tables.length];
        for (int i = 0; i < this.buttonTab.length; i++)
            this.buttonList.add(this.buttonTab[i] = (new GuiButtonCustom(1 + i, this.tables[i].getNameTab())).setButtonGroup(this.tabGroup).setButtonColor(color(27, 34, 52)).setHoverButtonColor(color(110, 85, 255)).setSelectedButtonColor(color(94, 85, 255)));
        this.tabGroup.setSelected((GuiButtonGroup.Item) this.buttonTab[0], true);
        this.listTop = (new GuiScrollingListTop(this, 20)).setSliderColor(color(54, 50, 144)).setSliderWidth(6).setDrawHoverColor(false).setDrawSelectedColor(false);
        this.listTop.setElements(tableValues.get(getCurrentTable().getDatabaseTable()));
        this.listLastWinners = (new GuiScrollingListLastWinners(this, 20)).setSliderColor(color(54, 50, 144)).setSliderWidth(6).setDrawHoverColor(false).setDrawSelectedColor(false);
        this.listLastWinners.setElements(tableLastWinners.getOrDefault(getCurrentTable().getDatabaseTable(), Collections.EMPTY_LIST));
        this.paneDescription = (new GuiScrollingPane(this) {

        public void drawScreen(int mouseX, int mouseY, float ticks) {
            GuiScreenTop.drawRectangle(this.x, this.y, this.width, this.height, GuiScreenTop.color(19, 25, 42));
            super.drawScreen(mouseX, mouseY, ticks);

        }}).setSliderColor(color(54, 50, 144)).setSliderWidth(6);

        this.paneDescription.setText(getCurrentTable().getTableDesc());
        this.textFieldSearch = new GuiTextFieldCustom(this.fontRenderer, 0, 0, 100, 16) {
            public void drawBackground() {
                GuiScreenTop.drawRectangle(this.xPosition, this.yPosition, this.width, this.height, GuiScreenTop.color(15, 21, 35));
            }
        };
        this.textFieldSearch.setMaxStringLength(64);
        this.textFieldSearch.setMnemonicText(I18n.format("gui.top.textField.search", new Object[0]));
    }

    public void resize() {
        this.buttonClose.setPosition(this.width - 100, 7);
        this.buttonClose.setSize(60, 16);
        int space = 4;
        int count = this.tables.length;
        int startX = 40;
        int endX = this.width - 40;
        int buttonWidth = MathHelper.floor(((endX - startX) / count)) - space + 1;
        for (int i = 0; i < this.buttonTab.length; i++) {
            this.buttonTab[i].setPosition(startX + i * (buttonWidth + space), 40);
            this.buttonTab[i].setSize(buttonWidth, 20);
        }
        this.listTop.setPosition(startX, 105);
        this.listTop.setSize(endX - startX - this.listTop.getSliderWidth(), this.height - 194);
        this.paneDescription.setPosition(startX, this.height - 78);
        this.paneDescription.setSize((!getCurrentTable().isShowLastWinners() || this.listLastWinners.getElements().isEmpty()) ? this.listTop.getWidth() : ((endX - startX) / 2 - 5), 83);
        this.listLastWinners.setPosition(this.paneDescription.getX() + this.paneDescription.getWidth() + 15, this.height - 70);
        this.listLastWinners.setSize((endX - startX) / 2 - 15, 53);
        this.textFieldSearch.xPosition = this.listTop.getX() + this.listTop.getWidth() - this.textFieldSearch.width - 2;
        this.textFieldSearch.yPosition = this.listTop.getY() - 39;
    }

    public void func_73866_w_() {
        if (!this.isInitialized) {
            initialize();
            this.isInitialized = true;
        }
        resize();
    }

    public void func_146280_a(Minecraft client, int w, int h) {
        this.mc = client;
        this.fontRenderer = client.fontRenderer;
        this.width = w;
        this.height = h;
        if (!MinecraftForge.EVENT_BUS.post((Event)new GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList)))
            func_73866_w_();
        MinecraftForge.EVENT_BUS.post((Event)new GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
    }

    public void func_146284_a(GuiButton button) {
        if (button.id == this.buttonClose.id) {
            this.mc.setIngameFocus();
        } else if (button.id >= 1 && button.id <= this.buttonTab.length) {
            if (getCurrentTable().isEnableSummarizing())
                this.paneDescription.setText(getCurrentTable().getTableDesc());
            this.listTop.setScrollOffset(0);
            if (!this.textFieldSearch.getText().isEmpty()) {
                this.searching = true;
                this.searchDelay = MyStatisticMod.config.getSearchDelay();
                this.listTop.setScrollOffset(0);
                this.listTop.clear();
            } else {
                this.listTop.setElements(tableValues.get(getCurrentTable().getDatabaseTable()));
            }
            if (getCurrentTable().isShowLastWinners() && getCurrentTable().isEnableSummarizing()) {
                this.listLastWinners.setScrollOffset(0);
                List<Pair<String, Integer>> elements = tableLastWinners.get(getCurrentTable().getDatabaseTable());
                this.listLastWinners.setElements(new ArrayList<>(elements));
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float ticks) {
        drawRect(0, 0, this.width, this.height, color(16, 21, 35));
        drawRect(0, 0, this.width, 30, color(19, 25, 42));
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        String tableName = getCurrentTable().getNameTable();
        List<String> list1 = this.mc.fontRenderer.listFormattedStringToWidth(tableName, this.width - 185 - 40);
        int yPos = this.mc.fontRenderer.getWordWrappedHeight(tableName, this.width - 185 - 40);
        for (int i = 0; i < list1.size(); i++)
            drawStringWithScale(this.fontRenderer, list1.get(i), 40.0F, (0 - yPos / 2 + i * this.mc.fontRenderer.FONT_HEIGHT), 1.3F, color(41, 178, 98));
        if (getCurrentTable().isEnableSummarizing())
            drawString(this.fontRenderer, I18n.format("gui.top.label.info", new Object[0]), 40, this.height - 88, -1);
        this.listTop.drawScreen(mouseX, mouseY, ticks);
        if (getCurrentTable().isShowLastWinners() && !this.listLastWinners.getElements().isEmpty() && getCurrentTable().isEnableSummarizing()) {
            drawString(this.fontRenderer, I18n.format("gui.top.label.lastWinners", new Object[0]), this.width / 2 + 11, this.height - 88, -1);
            this.listLastWinners.drawScreen(mouseX, mouseY, ticks);
        }
        if (getCurrentTable().isEnableSummarizing())
            this.paneDescription.drawScreen(mouseX, mouseY, ticks);
        this.textFieldSearch.drawTextBox();
        if (this.searching && this.searchDelay > 0) {
            String msg = I18n.format("Поиск", new Object[0]) + this.progress[this.searchDelay / 6 % this.progress.length];
            drawStringWithScale(this.fontRenderer, msg, ((this.listTop.getX() + this.listTop.getWidth()) / 2 - this.fontRenderer.getStringWidth(msg) / 2), ((this.listTop.getY() + this.listTop.getHeight()) / 2), 2.0F, color(200, 0, 0));
        } else if (!this.searching && this.listTop.getElements().isEmpty() && !this.textFieldSearch.getText().isEmpty()) {
            String msg = I18n.format("gui.top.label.searchNotFound", new Object[0]);
            drawStringWithScale(this.fontRenderer, msg, ((this.listTop.getX() + this.listTop.getWidth()) / 2 - this.fontRenderer.getStringWidth(msg) / 2), ((this.listTop.getY() + this.listTop.getHeight()) / 2), 2.0F, color(200, 0, 0));
        }
        GL11.glDisable(3042);
        super.drawScreen(mouseX, mouseY, ticks);
    }

    public void func_146281_b() {
        this.isInitialized = false;
    }

    public void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.listTop.mouseClicked(mouseX, mouseY, mouseButton);
            if (getCurrentTable().isShowLastWinners() && !this.listLastWinners.getElements().isEmpty() && getCurrentTable().isEnableSummarizing())
                this.listLastWinners.mouseClicked(mouseX, mouseY, mouseButton);
            this.textFieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
            this.paneDescription.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException exception) {
            MyStatisticMod.logger.error(exception);
        }
    }

    public void func_73869_a(char c, int i) {
        try {
            super.keyTyped(c, i);
            if (this.textFieldSearch.textboxKeyTyped(c, i))
                if (!this.textFieldSearch.getText().isEmpty()) {
                    this.searching = true;
                    this.searchDelay = MyStatisticMod.config.getSearchDelay();
                    this.listTop.setScrollOffset(0);
                    this.listTop.clear();
                } else {
                    this.searchDelay = 0;
                }
        } catch (IOException exception) {
            MyStatisticMod.logger.error(exception);
        }
    }

    public void func_146286_b(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.paneDescription.mouseReleased(mouseX, mouseY, mouseButton);
        this.listTop.mouseReleased(mouseX, mouseY, mouseButton);
        this.listLastWinners.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void func_146273_a(int mouseX, int mouseY, int mouseButton, long lastMouseEvent) {
        this.paneDescription.mouseClickMove(mouseX, mouseY, mouseButton);
        this.listTop.mouseClickMove(mouseX, mouseY, mouseButton);
        this.listLastWinners.mouseClickMove(mouseX, mouseY, mouseButton);
    }

    public void func_146274_d() {
        try {
            super.handleMouseInput();
            this.listTop.handleMouseInput();
            if (getCurrentTable().isShowLastWinners() && !this.listLastWinners.getElements().isEmpty() && getCurrentTable().isEnableSummarizing())
                this.listLastWinners.handleMouseInput();
            if (getCurrentTable().isEnableSummarizing())
                this.paneDescription.handleMouseInput();
        } catch (IOException exception) {
            MyStatisticMod.logger.error(exception);
        }
    }

    public void func_73876_c() {
        this.listTop.updateScreen();
        if (getCurrentTable().isShowLastWinners() && !this.listLastWinners.getElements().isEmpty() && getCurrentTable().isEnableSummarizing())
            this.listLastWinners.updateScreen();
        this.paneDescription.setSize((!getCurrentTable().isShowLastWinners() || this.listLastWinners.getElements().isEmpty()) ? this.listTop.getWidth() : ((this.width - 40 - 40) / 2 - 5), 74);
        this.listLastWinners.setPosition(this.paneDescription.getX() + this.paneDescription.getWidth() + 15, this.height - 57);
        this.listTop.setSize(this.width - 40 - 40 - this.listTop.getSliderWidth(), getCurrentTable().isEnableSummarizing() ? (this.height - 194) : (this.height - 115));
        this.textFieldSearch.updateCursorCounter();
        if (this.searching && --this.searchDelay <= 0)
            search();
    }

    public void search() {
        if (!this.textFieldSearch.getText().isEmpty()) {
            FMLProxyPacket packet = MyStatisticMod.network.createPacket((byte)2, (Object[])new String[] { getCurrentTable().getDatabaseTable(), this.textFieldSearch.getText() });
            MyStatisticMod.network.sendToServer(packet);
        } else {
            this.listTop.setElements(tableValues.get(getCurrentTable().getDatabaseTable()));
        }
    }

    public boolean func_73868_f() {
        return false;
    }

    public void filterRows() {
        search();
    }

    public static void fillSearch(List<Triple<Integer, String, String>> values) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiScreenTop) {
            GuiScreenTop gui = (GuiScreenTop)mc.currentScreen;
            gui.listTop.setElements(values);
            gui.searching = false;
            gui.searchDelay = 0;
        }
    }

    public static void fillTables(Map<String, List<Triple<Integer, String, String>>> values) {
        tableValues = values;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiScreenTop) {
            GuiScreenTop gui = (GuiScreenTop)mc.currentScreen;
            if (!gui.searching)
                gui.filterRows();
        }
    }

    public static void fillLastWinners(Map<String, List<Pair<String, Integer>>> values) {
        tableLastWinners = values;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiScreenTop) {
            GuiScreenTop gui = (GuiScreenTop)mc.currentScreen;
            if (gui.getCurrentTable().isShowLastWinners() && gui.getCurrentTable().isEnableSummarizing()) {
                List<Pair<String, Integer>> elements = tableLastWinners.get(gui.getCurrentTable().getDatabaseTable());
                gui.listLastWinners.setElements(new ArrayList<>(elements));
            }
        }
    }

    public static void fillCurrentPlayer(Map<String, Triple<Integer, String, String>> values) {
        currentPlayer = values;
    }

    public MyStatisticsConfig.Table getCurrentTable() {
        return this.tables[this.tabGroup.getSelectionIndex()];
    }

    public static int color(int r, int g, int b) {
        return color(r, g, b, 255);
    }

    public static int color(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    public static void drawStringWithScale(FontRenderer font, String line, float x, float y, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(x / scale, y / scale + font.FONT_HEIGHT * scale, 0.0F);
        font.drawString(line, 0, 0, color);
        GL11.glPopMatrix();
    }

    public static void drawRectangle(double x, double y, double width, double height, int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        // EXAMPLE OF NEW BUFFER BUILDER -> OLD TESSELLATOR
        // FOR INFO:
        // USE DefaultVertexFormats FOR DIFFERENT FORMS (ALIKE .startDrawingQuads();)
        // INSTEAD OF addVertex(x, y, z) and addVertexWithUV(x, y, z, u, v) USE: pos(x, y, z)
        // finishDrawing();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(r, g, b, a);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x, y, 0.0D);
        bufferBuilder.pos(x, y + height, 0.0D);
        bufferBuilder.pos(x + width, y + height, 0.0D);
        bufferBuilder.pos(x + width, y, 0.0D);
        bufferBuilder.finishDrawing();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
    }

    public class GuiScrollingListTop extends GuiScrollingList<Triple<Integer, String, String>> {
        public GuiScrollingListTop(GuiScreen screen, int listEntryHeight) {
            super(screen, listEntryHeight);
        }

        public void drawScreen(int mouseX, int mouseY, float ticks) {
            int space = 1;
            String[] headers = { "gui.top.column.index", "gui.top.column.playerName", "gui.top.column." + GuiScreenTop.this.getCurrentTable().getDatabaseTable() + ".points" };
            GuiScreenTop.drawRectangle(this.x, (this.y - 42), (this.width / 6), (this.entryHeight * 2), GuiScreenTop.color(27, 34, 52));
            GuiScreenTop.drawRectangle((this.x + this.width / 6 + space), (this.y - 42), (this.width / 3), (this.entryHeight * 2), GuiScreenTop.color(27, 34, 52));
            GuiScreenTop.drawRectangle((this.x + this.width / 6 + this.width / 3 + space * 2), (this.y - 42), (this.width / 2), (this.entryHeight * 2), GuiScreenTop.color(27, 34, 52));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[0], new Object[0]), this.x + 10, this.y - 42 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[1], new Object[0]), this.x + 10 + this.width / 6 + space, this.y - 42 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[2], new Object[0]), this.x + 10 + this.width / 6 + this.width / 3 + space * 2, this.y - 42 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            Triple<Integer, String, String> currPlr = GuiScreenTop.currentPlayer.get(GuiScreenTop.this.getCurrentTable().getDatabaseTable());
            if (currPlr != null) {
                drawString(this.parent.mc.fontRenderer, I18n.format(String.valueOf(currPlr.getLeft()), new Object[0]), this.x + 10, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
                drawString(this.parent.mc.fontRenderer, I18n.format((String)currPlr.getMiddle(), new Object[0]), this.x + 10 + this.width / 6 + space, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
                drawString(this.parent.mc.fontRenderer, I18n.format((String)currPlr.getRight(), new Object[0]), this.x + 10 + this.width / 6 + this.width / 3 + space * 2, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            }
            super.drawScreen(mouseX, mouseY, ticks);
        }

        public void drawEntry(Triple<Integer, String, String> entry, int index, int x, int y, boolean hovered) {
            int space = 1;
            int[] colorTable = GuiScreenTop.this.getCurrentTable().getTopLineColor();
            int color = hovered ? GuiScreenTop.color(21, 28, 46) : ((index < colorTable.length) ? colorTable[index] : GuiScreenTop.color(19, 25, 42));
            GuiScreenTop.drawRectangle(x, y, (this.width / 6), this.entryHeight, color);
            GuiScreenTop.drawRectangle((x + this.width / 6 + space), y, (this.width / 3), this.entryHeight, color);
            GuiScreenTop.drawRectangle((x + this.width / 6 + this.width / 3 + space * 2), y, (this.width / 2), this.entryHeight, color);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.column1", new Object[] { entry.getLeft() }), x + 10, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.column2", new Object[] { entry.getMiddle() }), x + 10 + this.width / 6 + space, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.column3", new Object[] { entry.getRight() }), x + 10 + this.width / 6 + this.width / 3 + space * 2, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
        }
    }

    public class GuiScrollingListLastWinners extends GuiScrollingList<Pair<String, String>> {
        public GuiScrollingListLastWinners(GuiScreen screen, int listEntryHeight) {
            super(screen, listEntryHeight);
        }

        public void drawScreen(int mouseX, int mouseY, float ticks) {
            int space = 1;
            String[] headers = { "gui.top.column.index", "gui.top.column.playerName", "gui.top.column.reward" };
            GuiScreenTop.drawRectangle(this.x, (this.y - 21), (this.width / 5), this.entryHeight, GuiScreenTop.color(27, 34, 52));
            GuiScreenTop.drawRectangle((this.x + this.width / 5 + space), (this.y - 21), (this.width / 2), this.entryHeight, GuiScreenTop.color(27, 34, 52));
            GuiScreenTop.drawRectangle((this.x + this.width / 5 + this.width / 2 + space * 2), (this.y - 21), this.width / 3.35D, this.entryHeight, GuiScreenTop.color(27, 34, 52));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[0], new Object[0]), this.x + 5, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[1], new Object[0]), this.x + 5 + this.width / 5 + space, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            drawString(this.parent.mc.fontRenderer, I18n.format(headers[2], new Object[0]), this.x + 5 + this.width / 5 + this.width / 2 + space * 2, this.y - 21 + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, GuiScreenTop.color(98, 105, 121));
            super.drawScreen(mouseX, mouseY, ticks);
        }

        public void drawEntry(Pair<String, String> entry, int index, int x, int y, boolean hovered) {
            int space = 1;
            int color = hovered ? GuiScreenTop.color(21, 28, 46) : GuiScreenTop.color(19, 25, 42);
            GuiScreenTop.drawRectangle(x, y, (this.width / 5), this.entryHeight, color);
            GuiScreenTop.drawRectangle((x + this.width / 5 + space), y, (this.width / 2), this.entryHeight, color);
            GuiScreenTop.drawRectangle((x + this.width / 5 + this.width / 2 + space * 2), y, this.width / 3.35D, this.entryHeight, color);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.lastWinners.column1", new Object[] { String.valueOf(index + 1) }), x + 5, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.lastWinners.column2", new Object[] { entry.getLeft() }), x + 5 + this.width / 5 + space, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
            drawString(this.parent.mc.fontRenderer, I18n.format("gui.top.lastWinners.column3", new Object[] { entry.getRight() }), x + 5 + this.width / 5 + this.width / 2 + space * 2, y + this.parent.mc.fontRenderer.FONT_HEIGHT / 2 + 2, -1);
        }
    }
}