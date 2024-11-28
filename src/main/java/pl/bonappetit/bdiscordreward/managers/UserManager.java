package pl.bonappetit.bdiscordreward.managers;


import java.util.*;
import pl.bonappetit.bdiscordreward.basic.User;
import pl.bonappetit.bdiscordreward.database.Logger;
import pl.bonappetit.bdiscordreward.database.MySQL;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager
{
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void createUserData(final String nick) {
        final User u = new User(nick);
        UserManager.users.put(nick, u);
    }

    public static User getUser(final String nick) {
        return UserManager.users.get(nick);
    }

    public static void loadUsers() {
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM `bdiscordreward`");
            while (rs.next()) {
                final User u = new User(rs);
                UserManager.users.put(u.getNick(), u);
            }
            rs.close();
        }
        catch (SQLException e) {
            Logger.info("Can not load players Error " + e.getMessage());
            e.printStackTrace();
        }
    }

}
