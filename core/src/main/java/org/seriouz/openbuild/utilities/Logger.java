/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.utilities;

public class Logger {
    public static void info(String message) {
        System.out.println("<LOG> " + message);
    }

    public static void info(String message, String sender) {
        System.out.println("<LOG> <" + sender.toUpperCase() + "> " + message);
    }

    public static void err(String message) {
        System.out.println("<ERR> " + message);
    }

    public static void err(String message, String sender) {
        System.out.println("<ERR> <" + sender.toUpperCase() + "> " + message);
    }
}
