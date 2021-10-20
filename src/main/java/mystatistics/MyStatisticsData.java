package mystatistics;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MyStatisticsData {
    public static final TMap<String, MyStatisticsData> PLAYERS_DATA = (TMap<String, MyStatisticsData>)new THashMap();

    private static final Expression EXPRESSION = (new ExpressionBuilder(MyStatisticMod.config.getPointsExpression())).variables(new String[] { "playerKills", "mobKills", "playerDeaths", "playingTime", "walkingDistance", "playerJumps", "playerMoney" }).build();

    private String playerName;

    private int playerKills;

    private int mobKills;

    private int playerDeaths;

    private long playingTime;

    private float walkingDistance;

    private int playerJumps;

    private int playerMoney;

    private long points;

    public MyStatisticsData() {}

    public MyStatisticsData(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getPlayerKills() {
        return this.playerKills;
    }

    public void setPlayerKills(int kills) {
        this.playerKills = kills;
        calculatePoints();
    }

    public int getMobKills() {
        return this.mobKills;
    }

    public void setMobKills(int kills) {
        this.mobKills = kills;
        calculatePoints();
    }

    public int getPlayerDeaths() {
        return this.playerDeaths;
    }

    public void setPlayerDeaths(int deaths) {
        this.playerDeaths = deaths;
        calculatePoints();
    }

    public long getPlayingTime() {
        return this.playingTime;
    }

    public void setPlayingTime(long time) {
        this.playingTime = time;
        calculatePoints();
    }

    public float getWalkingDistance() {
        return this.walkingDistance;
    }

    public void setWalkingDistance(float distance) {
        this.walkingDistance = distance;
        calculatePoints();
    }

    public int getPlayerJumps() {
        return this.playerJumps;
    }

    public void setPlayerJumps(int jumps) {
        this.playerJumps = jumps;
        calculatePoints();
    }

    public int getPlayerMoney() {
        return this.playerMoney;
    }

    public void setPlayerMoney(int money) {
        this.playerMoney = money;
        calculatePoints();
    }

    public long getPoints() {
        return this.points;
    }

    public void calculatePoints() {
        EXPRESSION.setVariable("playerKills", this.playerKills);
        EXPRESSION.setVariable("mobKills", this.mobKills);
        EXPRESSION.setVariable("playerDeaths", this.playerDeaths);
        EXPRESSION.setVariable("playingTime", this.playingTime);
        EXPRESSION.setVariable("walkingDistance", this.walkingDistance);
        EXPRESSION.setVariable("playerJumps", this.playerJumps);
        EXPRESSION.setVariable("playerMoney", this.playerMoney);
        this.points = Math.round(EXPRESSION.evaluate());
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("pName", this.playerName);
        nbt.setInteger("pKills", this.playerKills);
        nbt.setInteger("mKills", this.mobKills);
        nbt.setInteger("pDeaths", this.playerDeaths);
        nbt.setLong("pTime", this.playingTime);
        nbt.setFloat("distance", this.walkingDistance);
        nbt.setInteger("pJumps", this.playerJumps);
        nbt.setLong("points", this.points);
        return nbt;
    }

    public void deserialize(NBTTagCompound nbt) {
        this.playerName = nbt.getString("pName");
        this.playerKills = nbt.getInteger("pKills");
        this.mobKills = nbt.getInteger("mKills");
        this.playerDeaths = nbt.getInteger("pDeaths");
        this.playingTime = nbt.getLong("pTime");
        this.walkingDistance = nbt.getFloat("distance");
        this.playerJumps = nbt.getInteger("pJumps");
        this.points = nbt.getLong("points");
    }

    public static MyStatisticsData getPlayerData(String playerName) {
        MyStatisticsData data = (MyStatisticsData)PLAYERS_DATA.get(playerName);
        if (data == null)
            PLAYERS_DATA.put(playerName, data = new MyStatisticsData(playerName));
        return data;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MyStatisticsData))
            return false;
        MyStatisticsData o = (MyStatisticsData)obj;
        return this.playerName.equals(o.getPlayerName());
    }

    public int hashCode() {
        return this.playerName.hashCode();
    }

    public String toString() {
        return this.playerName;
    }
}
