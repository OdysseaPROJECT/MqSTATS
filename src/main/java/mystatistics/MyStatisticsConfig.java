package mystatistics;

public class MyStatisticsConfig {
    private MySqlSettings mySqlSettings;

    private Table[] tables;

    private int searchDelay;

    private String pointsExpression;

    private boolean clearPlayingTime;

    private boolean oldSummarizing;

    public void setMySqlSettings(MySqlSettings dbSettings) {
        this.mySqlSettings = dbSettings;
    }

    public void setTables(Table[] dbTables) {
        this.tables = dbTables;
    }

    public void setSearchDelay(int delay) {
        this.searchDelay = delay;
    }

    public MySqlSettings getMySqlSettings() {
        return this.mySqlSettings;
    }

    public Table[] getTables() {
        return this.tables;
    }

    public int getSearchDelay() {
        return this.searchDelay;
    }

    public String getPointsExpression() {
        return this.pointsExpression;
    }

    public void setPointsExpression(String exp) {
        this.pointsExpression = exp;
    }

    public boolean isClearPlayingTime() {
        return this.clearPlayingTime;
    }

    public void setClearPlayingTime(boolean clear) {
        this.clearPlayingTime = clear;
    }

    public boolean isOldSummarizing() {
        return this.oldSummarizing;
    }

    public void setOldSummarizing(boolean old) {
        this.oldSummarizing = old;
    }

    public static class MySqlSettings {
        private String databaseUrl;

        private String databaseUser;

        private String databasePassword;

        private int timeOut;

        public void setDatabaseUrl(String url) {
            this.databaseUrl = url;
        }

        public void setDatabaseUser(String user) {
            this.databaseUser = user;
        }

        public void setDatabasePassword(String password) {
            this.databasePassword = password;
        }

        public void setTimeOut(int time) {
            this.timeOut = time;
        }

        public String getDatabaseUrl() {
            return this.databaseUrl;
        }

        public String getDatabaseUser() {
            return this.databaseUser;
        }

        public String getDatabasePassword() {
            return this.databasePassword;
        }

        public int getTimeOut() {
            return this.timeOut;
        }
    }

    public static class Table {
        private String databaseTable;

        private String createQuery;

        private String type;

        private String nameTable;

        private String nameTab;

        private int numberOfLines;

        private boolean activeTable;

        private int timeOfSummarizing;

        private boolean enableSummarizing;

        private boolean showLastWinners;

        private String[] tableDesc;

        private int[] moneyTrophy;

        private int[] topLineColor;

        public void setDatabaseTable(String table) {
            this.databaseTable = table;
        }

        public void setCreateQuery(String query) {
            this.createQuery = query;
        }

        public void setType(String tableType) {
            this.type = tableType;
        }

        public void setNameTable(String name) {
            this.nameTable = name;
        }

        public void setNameTab(String name) {
            this.nameTab = name;
        }

        public void setNumberOfLines(int lines) {
            this.numberOfLines = lines;
        }

        public void setActiveTable(boolean active) {
            this.activeTable = active;
        }

        public void setTimeOfSummarizing(int time) {
            this.timeOfSummarizing = time;
        }

        public void setEnableSummarizing(boolean enable) {
            this.enableSummarizing = enable;
        }

        public void setShowLastWinners(boolean lastWinners) {
            this.showLastWinners = lastWinners;
        }

        public void setTableDesc(String[] desc) {
            this.tableDesc = desc;
        }

        public void setMoneyTrophy(int[] trophy) {
            this.moneyTrophy = trophy;
        }

        public void setTopLineColor(int[] lineColor) {
            this.topLineColor = lineColor;
        }

        public String getDatabaseTable() {
            return this.databaseTable;
        }

        public String getCreateQuery() {
            return this.createQuery;
        }

        public String getType() {
            return this.type;
        }

        public String getNameTable() {
            return this.nameTable;
        }

        public String getNameTab() {
            return this.nameTab;
        }

        public int getNumberOfLines() {
            return this.numberOfLines;
        }

        public boolean isActiveTable() {
            return this.activeTable;
        }

        public int getTimeOfSummarizing() {
            return this.timeOfSummarizing;
        }

        public boolean isEnableSummarizing() {
            return this.enableSummarizing;
        }

        public boolean isShowLastWinners() {
            return this.showLastWinners;
        }

        public String[] getTableDesc() {
            return this.tableDesc;
        }

        public int[] getMoneyTrophy() {
            return this.moneyTrophy;
        }

        public int[] getTopLineColor() {
            return this.topLineColor;
        }
    }
}
