package pl.bonappetit.bdiscordreward.database;


import pl.bonappetit.bdiscordreward.BDiscordReward;

import java.util.logging.*;

public final class Logger
{
    public static void info(final String... logs) {
        final String[] arrayOfString = logs;
        final int i = logs.length;
        for (byte b = 0; b < i; ++b) {
            final String s = arrayOfString[b];
            log(Level.INFO, s);
        }
    }

    public static void warning(final String... logs) {
        final String[] arrayOfString = logs;
        final int i = logs.length;
        for (byte b = 0; b < i; ++b) {
            final String s = arrayOfString[b];
            log(Level.WARNING, s);
        }
    }

    public static void severe(final String... logs) {
        final String[] arrayOfString = logs;
        final int i = logs.length;
        for (byte b = 0; b < i; ++b) {
            final String s = arrayOfString[b];
            log(Level.SEVERE, s);
        }
    }

    public static void log(final Level level, final String log) {
        BDiscordReward.getPlugin(BDiscordReward.class).getLogger().log(level, log);
    }
}
