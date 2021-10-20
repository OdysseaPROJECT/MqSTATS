package mystatistics;

public class FeConomyHelper {
    private static Class<?> ACCOUNT_CLASS = null;

    private static Class<?> FE_CLASS = null;

    private static Class<?> FE_API_CLASS = null;

    private static Object FE = null;

    static {
        loadClasses();
    }

    public static double getBalance(String playerName) {
        return -1.0D;
    }

    public static void setBalance(String playerName, double balance) {}

    private static Object getAccount(String name) {
        return null;
    }

    private static void loadClasses() {}
}
