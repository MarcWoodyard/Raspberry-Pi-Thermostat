package com.marcwoodyard.RaspberryPiThermostat.utils;

import com.marcwoodyard.RaspberryPiThermostat.web.WebServer;
import com.marcwoodyard.RaspberryPiThermostat.web.security.KeyStoreManager;

import java.io.File;
import java.util.ArrayList;

public class ProgramSettings {

    // Ints default to 0, if run with default values, thermostat would always be running.
    private static int maxTemperature = 86;
    private static int minTemperature = 74;

    private static int gpioOnOff;
    private static int gpioFilterReset;

    private static int gpioDHT11;

    private static String WEB_USERNAME = "admin";
    private static String WEB_PASSWORD = PasswordTools.generateSecureString(10, false);

    private static String notificationReceipts;
    private static String fromEmail;
    private static String smtpServer;
    private static String port;
    private static boolean enableAuth = false;
    private static boolean enableSTARTLS = false;
    private static String emailUsername;
    private static String emailPassword;

    public static void importSettings() {

        if(!new File("Settings.ini").exists())
            save("Settings.ini");
        else {
            ArrayList<String> settings = new FileHandler().fileToArrayList(new File("Settings.ini"));

            maxTemperature = Integer.parseInt(settings.get(0));
            minTemperature = Integer.parseInt(settings.get(1));

            gpioOnOff = Integer.parseInt(settings.get(2));
            gpioFilterReset = Integer.parseInt(settings.get(3));

            gpioDHT11 = Integer.parseInt(settings.get(4));

            WEB_USERNAME = settings.get(5);
            WEB_PASSWORD = settings.get(6);

            // If web password is weak, replace it with stronger password.
            if (PasswordTools.calculatePasswordStrength(WEB_PASSWORD) < 7) {
                System.out.println("Web interface password weak. Replacing with autogenerated stronger one.");
                WEB_PASSWORD = PasswordTools.generateSecureString(10, false);
                ProgramSettings.save("Settings.ini");
            }
        }

        if(new File("NotificationSettings.ini").exists()) {
            ArrayList<String> mailSettings = new FileHandler().fileToArrayList(new File("NotificationSettings.ini"));

            notificationReceipts = mailSettings.get(0);
            fromEmail = mailSettings.get(1);
            smtpServer = mailSettings.get(2);
            port = mailSettings.get(3);
            enableAuth = Boolean.parseBoolean(mailSettings.get(4));
            enableSTARTLS = Boolean.parseBoolean(mailSettings.get(5));
            emailUsername = mailSettings.get(6);
            emailPassword = mailSettings.get(7);
        }
    }

    private static void save(String fileName) {
        ArrayList<String> settings = new ArrayList<>();

        if (fileName.equals("Settings.ini")) {
            settings.add(maxTemperature + "");
            settings.add(minTemperature + "");
            settings.add(gpioOnOff + "");
            settings.add(gpioFilterReset + "");
            settings.add(gpioDHT11 + "");
            settings.add(WEB_USERNAME);
            settings.add(WEB_PASSWORD);
        } else if (fileName.equals("NotificationSettings.ini")) {
            settings.add(notificationReceipts);
            settings.add(fromEmail);
            settings.add(smtpServer);
            settings.add(port);
            settings.add(enableAuth + "");
            settings.add(enableSTARTLS + "");
            settings.add(emailUsername);
            settings.add(emailPassword);
        }

        FileHandler.ArrayListToFile(new File(fileName), settings);
    }

    public static int getMaxTemperature() {
        return maxTemperature;
    }

    public static void setMaxTemperature(int maxTemperature) {
        ProgramSettings.maxTemperature = maxTemperature;
        save("Settings.ini");
    }

    public static int getMinTemperature() {
        return minTemperature;
    }

    public static void setMinTemperature(int minTemperature) {
        ProgramSettings.minTemperature = minTemperature;
        save("Settings.ini");
    }

    public static int getGpioOnOff() {
        return gpioOnOff;
    }

    public static void setGpioOnOff(int gpioOnOff) {
        ProgramSettings.gpioOnOff = gpioOnOff;
        save("Settings.ini");
    }

    public static int getGpioFilterReset() {
        return gpioFilterReset;
    }

    public static void setGpioFilterReset(int gpioFilterReset) {
        ProgramSettings.gpioFilterReset = gpioFilterReset;
        save("Settings.ini");
    }

    public static int getGpioDHT11() {
        return gpioDHT11;
    }

    public static void setGpioDHT11(int gpioDHT11) {
        ProgramSettings.gpioDHT11 = gpioDHT11;
        save("Settings.ini");
    }

    public static String getWebUsername() {
        return WEB_USERNAME;
    }

    public static void setWebUsername(String webUsername) {
        WEB_USERNAME = webUsername;
        save("Settings.ini");
    }

    public static String getWebPassword() {
        return WEB_PASSWORD;
    }

    public static void setWebPassword(String webPassword) {
        WEB_PASSWORD = webPassword;
        WebServer.stopServer();
        KeyStoreManager.deleteKeystore();
        WebServer.startServer(WebServer.getPort());
        save("Settings.ini");
    }

    public static String getNotificationReceipts() {
        return notificationReceipts;
    }

    public static void setNotificationReceipts(String notificationReceipts) {
        ProgramSettings.notificationReceipts = notificationReceipts;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String toEmail) {
        ProgramSettings.fromEmail = toEmail;
        save("NotificationSettings.ini");
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        ProgramSettings.smtpServer = smtpServer;
        save("NotificationSettings.ini");
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        ProgramSettings.port = port;
        save("NotificationSettings.ini");
    }

    public Boolean getEnableAuth() {
        return enableAuth;
    }

    public void setEnableAuth(Boolean enableAuth) {
        ProgramSettings.enableAuth = enableAuth;
        save("NotificationSettings.ini");
    }

    public Boolean getEnableSTARTLS() {
        return enableSTARTLS;
    }

    public void setEnableSTARTLS(Boolean enableSTARTLS) {
        ProgramSettings.enableSTARTLS = enableSTARTLS;
        save("NotificationSettings.ini");
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public void setEmailUsername(String username) {
        ProgramSettings.emailUsername = username;
        save("NotificationSettings.ini");
    }

    public String getEmailPassword() {
        return "password";
    }

    public void setEmailPassword(String password) {
        if (password.equalsIgnoreCase("password"))
            return;

        ProgramSettings.emailPassword = password;
        save("NotificationSettings.ini");
    }

}
