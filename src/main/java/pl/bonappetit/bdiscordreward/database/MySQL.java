package pl.bonappetit.bdiscordreward.database;

import pl.bonappetit.bdiscordreward.BDiscordReward;

import java.sql.*;

public class MySQL
{
    private static Connection connection;

    public static boolean isConnection() {
        return MySQL.connection != null;
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            MySQL.connection = DriverManager.getConnection("jdbc:mysql://" + BDiscordReward.getPlugin().getConfig().getString("database.hostname") + ":" + BDiscordReward.getPlugin().getConfig().getInt("database.port") + "/" + BDiscordReward.getPlugin().getConfig().getString("database.name"), BDiscordReward.getPlugin().getConfig().getString("database.user"), BDiscordReward.getPlugin().getConfig().getString("database.password"));
            BDiscordReward.getPlugin().getLogger().info("Connecting to the database!");
        }
        catch (ClassNotFoundException ex) {}
        catch (SQLException ex2) {}
    }

    public static void disconnect() throws SQLException {
        if (isConnection()) {
            MySQL.connection.close();
            BDiscordReward.getPlugin().getLogger().info("Disonnecting to the database!");
        }
    }

    public static void update(final String update) {
        if (MySQL.connection == null) {
            return;
        }
        try {
            MySQL.connection.createStatement().executeUpdate(update);
        }
        catch (SQLException ex) {
            Logger.warning("[MYSQL] Could not update " + update + "\n" + ex.getMessage());
        }
    }

    public static ResultSet query(final String query) {
        if (MySQL.connection == null) {
            return null;
        }
        try {
            return MySQL.connection.createStatement().executeQuery(query);
        }
        catch (SQLException ex) {
            Logger.warning("[MYSQL] Could not query " + query);
            ex.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return MySQL.connection;
    }
}
