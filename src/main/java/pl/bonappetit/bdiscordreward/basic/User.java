package pl.bonappetit.bdiscordreward.basic;

import lombok.Data;
import pl.bonappetit.bdiscordreward.database.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public @Data class User {

    private String nick;
    private int count;
    private Timestamp nextRewardTime;

    public User(final String nick) {
        this.nick = nick;
        this.count = 0;
        this.nextRewardTime = Timestamp.valueOf(LocalDateTime.now());

        MySQL.update("INSERT INTO `bdiscordreward` (`id`, `nick`, `next_reward_time`, `reward_count`) VALUES (NULL, '"+getNick()+"', '"+getNextRewardTime()+"', '"+getCount()+"');");
    }

    public User(final ResultSet rs) throws SQLException {
        this.nick = rs.getString("nick");
        this.count = rs.getInt("reward_count");
        this.nextRewardTime = rs.getTimestamp("next_reward_time");
    }

    public void save() {
        MySQL.update("UPDATE `bdiscordreward` SET `next_reward_time` = '" + this.getNextRewardTime() +
                "', `reward_count` = '" + this.getCount() +
                "' WHERE `nick` = '" + this.getNick() + "';");
    }

    public boolean canReceiveReward() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rewardTime = nextRewardTime.toLocalDateTime();
        return rewardTime.isBefore(now);
    }

    public String getFormattedRewardTime() {
        LocalDateTime rewardTime = nextRewardTime.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return rewardTime.format(formatter);
    }

    public void setNextRewardTime(String cooldownClaiming) {
        LocalDateTime newRewardTime = LocalDateTime.now();
        Pattern pattern = Pattern.compile("(\\d+)([ymdhminse]+)");
        Matcher matcher = pattern.matcher(cooldownClaiming);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "y": newRewardTime = newRewardTime.plusYears(value); break;
                case "m": newRewardTime = newRewardTime.plusMonths(value); break;
                case "d": newRewardTime = newRewardTime.plusDays(value); break;
                case "h": newRewardTime = newRewardTime.plusHours(value); break;
                case "min": newRewardTime = newRewardTime.plusMinutes(value); break;
                case "s": newRewardTime = newRewardTime.plusSeconds(value); break;
                default: throw new IllegalArgumentException("Unknown unit of time: " + unit);
            }
        }
        this.nextRewardTime = Timestamp.valueOf(newRewardTime);
    }
}
