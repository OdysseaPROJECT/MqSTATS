package mystatistics;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SideOnly(Side.SERVER)
public class MyStatisticsDatabase {
    private MysqlDataSource dataSource;

    private ExecutorService executor;

    public MyStatisticsDatabase(String host, String username, String password) {
        this.dataSource = new MysqlDataSource();
        this.dataSource.setURL(host);
        this.dataSource.setUser(username);
        this.dataSource.setPassword(password);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public MysqlDataSource getDataSource() {
        return this.dataSource;
    }

    public ExecutorService getExecutor() {
        return this.executor;
    }

    public boolean execute(String query, Object... params) {
        return false;
    }

    public PreparedStatement executeQuery(Connection connection, String query, Object... params) {
        return null;
    }

    public void initTables() {}

    public void registerPlayer(String table, String name) {}

    public boolean isPlayerExits(String table, String name) {
        return false;
    }
}

