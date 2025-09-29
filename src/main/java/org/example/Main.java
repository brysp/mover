package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final long MOVEMENT_SLEEP_MINUTES = 2;

    private static Mover mover;

    private static Timer timer;

    private static MenuItem statusMenuItem;
    private static MenuItem lastMoveItem;

    private static boolean isOn = false;


    public static void main(String[] args) throws AWTException, InterruptedException {

        try {
            initMover();
            initTimer();
            startTray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void initMover() {
        mover = Mover.getInstance();
    }

    public static void initTimer() {
        timer = new Timer();
    }

    public static void startMover() {

        long period = MOVEMENT_SLEEP_MINUTES * 60 * 1000;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(mover.move()){
                    lastMoveItem.setLabel("Last move at: " + getCurrentTime());
                }
            }
        },0, period);
    }

    public static String getCurrentTime() {
        Format f = new SimpleDateFormat("HH:mm:ss");
        return f.format(new Date());
    }

    public static void stopMover() {
        timer.cancel();
    }


    public static void startTray() throws IOException {

        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu trayPopupMenu = new PopupMenu();

        statusMenuItem = new MenuItem("Disabled");
        statusMenuItem.setEnabled(false);
        trayPopupMenu.add(statusMenuItem);

        lastMoveItem = new MenuItem("Last move: --:--:--");
        lastMoveItem.setEnabled(false);
        trayPopupMenu.add(lastMoveItem);

        MenuItem action = new MenuItem("Toggle");
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isOn) {
                    stopMover();
                    statusMenuItem.setLabel("Stopped");
                    isOn = false;
                } else {
                    startMover();
                    statusMenuItem.setLabel("Enabled");
                    isOn = true;
                }
            }
        });
        trayPopupMenu.add(action);

        //2nd menuitem of popupmenu
        MenuItem close = new MenuItem("Exit");
        close.addActionListener(e -> System.exit(0));
        trayPopupMenu.add(close);

        BufferedImage trayIconImage = ImageIO.read(Main.class.getResource("/mouse.png"));
        int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;
        TrayIcon trayIcon = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "Mouser", trayPopupMenu);


        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
    }
}