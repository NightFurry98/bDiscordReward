package pl.bonappetit.bdiscordreward.utils;

import pl.bonappetit.bdiscordreward.basic.User;
import pl.bonappetit.bdiscordreward.managers.UserManager;

import java.sql.Timestamp;

public class RewardAPI {

    /**
     * Checks if the user with the given nickname can receive a reward.
     *
     * @param nick The nickname of the user.
     * @return true if the user can receive a reward, false otherwise.
     */
    public static boolean canReceiveReward(String nick) {
        User user = UserManager.getUser(nick);
        if (user == null) {
            return false;
        }
        return user.canReceiveReward();
    }

    /**
     * Retrieves the number of rewards the user with the given nickname has.
     *
     * @param nick The nickname of the user.
     * @return The number of rewards the user has, or 0 if the user doesn't exist.
     */
    public static int getRewardCount(String nick) {
        User user = UserManager.getUser(nick);
        if (user == null) {
            return 0;
        }
        return user.getCount();
    }

    /**
     * Retrieves the next reward time for the user with the given nickname.
     *
     * @param nick The nickname of the user.
     * @return The next reward time as a Timestamp, or null if the user doesn't exist.
     */
    public static Timestamp getNextRewardTime(String nick) {
        User user = UserManager.getUser(nick);
        if (user == null) {
            return null;
        }
        return user.getNextRewardTime();
    }


}
