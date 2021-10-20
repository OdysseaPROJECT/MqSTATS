package mystatistics.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPacketHandler {
    @SideOnly(Side.CLIENT)
    void handleClientSide(PacketBuffer paramPacketBuffer, byte paramByte, Minecraft paramMinecraft, WorldClient paramWorldClient,
                          EntityPlayerMP paramEntityClientPlayerMP);

    void handleServerSide(PacketBuffer paramPacketBuffer, byte paramByte, WorldServer paramWorldServer, EntityPlayerMP paramEntityPlayerMP);

    String getChannel();
}
