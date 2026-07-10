package model;

public class DatabaseConfig {
    private int databaseId;
    private String databaseName;
    private String serverName;
    private String databaseType;
    private String databaseUser;
    private String databasePassword;
    private boolean isActive;

    public DatabaseConfig() {
    }

    public DatabaseConfig(int databaseId, String databaseName, String serverName, String databaseType, String databaseUser, String databasePassword, boolean isActive) {
        this.databaseId = databaseId;
        this.databaseName = databaseName;
        this.serverName = serverName;
        this.databaseType = databaseType;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.isActive = isActive;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "databaseId=" + databaseId +
                ", databaseName='" + databaseName + '\'' +
                ", serverName='" + serverName + '\'' +
                ", databaseType='" + databaseType + '\'' +
                ", databaseUser='" + databaseUser + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
