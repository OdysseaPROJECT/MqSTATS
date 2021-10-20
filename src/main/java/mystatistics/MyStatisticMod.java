package mystatistics;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.Map;

import mystatistics.event.MyStatisticsEventListener;
import mystatistics.network.IPacketHandler;
import mystatistics.network.MyStatisticsPacketHandler;
import mystatistics.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "mystatistics", name = "MyStatistics", version = "1.0-SNAPSHOT")
public class MyStatisticMod {
    @Instance("mystatistics")
    public static MyStatisticMod instance;

    public static final String MOD_ID = "mystatistics";

    public static final String MOD_NAME = "My Statistics";

    public static final String MOD_VERSION = "1.0";

    public static final Logger logger = LogManager.getLogger("My Statistics");

    public static final Gson GSON = (new GsonBuilder()).create();

    public static final boolean SERVER_COMPILATION = false;

    public static MyStatisticsConfig config;

    @SideOnly(Side.SERVER)
    public static Map<String, Long> listWinnersTime;

    @SideOnly(Side.SERVER)
    public static MyStatisticsDatabase database;

    public static MyStatisticsExtendedData extendedData;

    public static NetworkManager network;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger.info("Loading...");
        logger.info("Create network system.");
        network = NetworkManager.registerPacketHandler((IPacketHandler)new MyStatisticsPacketHandler());
        logger.info("Register event listener.");
        final MyStatisticsEventListener listener = new MyStatisticsEventListener();
        MinecraftForge.EVENT_BUS.register(listener);
        FMLCommonHandler.instance().bus().register(listener);
        logger.info("Successfully loaded!");
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void serverStopping(FMLServerStoppingEvent e) {}

    @SideOnly(Side.SERVER)
    public MyStatisticsConfig loadConfig(File file) {
        return null;
    }

    @SideOnly(Side.SERVER)
    public Map<String, Long> loadLastWinnersTime(File file) {
        return Maps.newHashMap();
    }

    @SideOnly(Side.SERVER)
    public void saveLastWinnersTime(File file) {}

    @SideOnly(Side.SERVER)
    public static void registerPlayer(String name) {}

    @SideOnly(Side.SERVER)
    public static void sendPlayersData() {}
}

