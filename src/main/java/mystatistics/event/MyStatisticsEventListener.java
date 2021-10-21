package mystatistics.event;

import mystatistics.MyStatisticMod;
import mystatistics.gui.GuiScreenTop;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MyStatisticsEventListener {
    @SideOnly(Side.SERVER)
    private long tickUpdate;

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {}

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onServerTick(TickEvent.ServerTickEvent e) {}

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {}

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post e) {
        if (e.getGui() instanceof net.minecraft.client.gui.GuiIngameMenu) {
            e.getButtonList().remove(3);
            e.getButtonList().add(new GuiButton(1704, e.getGui().width / 2 + 2, e.getGui().height / 4 + 96 - 16, 98, 20, I18n.format("menu.playersTop", new Object[0])));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onButtonAction(GuiScreenEvent.ActionPerformedEvent.Post e) {

        // CONFIG AND GETTERS OF TABLES ARE NULLABLES.
        // MUST BE CONNECTED TO DB OR SMTH ELSE
        // IDK.
        if (e.getGui() instanceof GuiIngameMenu &&
                e.getButton().id == 1704 &&
                MyStatisticMod.config != null)
                e.getGui().mc.displayGuiScreen((GuiScreen)new GuiScreenTop());
    }
}
