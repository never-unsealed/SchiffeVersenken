package main;

import game.SvGame;
import javax.swing.*;
import java.awt.*;

import static gui.SvFieldSizeDialog.SvGetFieldSize;
import static gui.SvNetworkDialog.SvGetNetworkInfo;
import static util.SV_GAME_MODE.*;

//Main Java class
public class SvMain
{
    //Entry point for Java application
    public static void main(String[] args)
    {
        JFrame mainFrame = new JFrame("Schiffe versenken b0.1");
        JPanel menuPanel = new JPanel();
        JButton modeOnline, modeOffline, modeAuto, restoreGame;
        SvGame game = new SvGame(mainFrame);

        modeOnline = new JButton("Gegen anderen Spieler spielen.");
        modeOffline = new JButton("Gegen Computer spielen.");
        modeAuto = new JButton("Zwei Computer gegeneinander spielen lassen.");
        restoreGame = new JButton("Spiel wiederherstellen");

        modeOnline.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeOnline.setMaximumSize(new Dimension(300, 50));
        modeOnline.addActionListener(e ->
        {
            game.mode = GAME_MODE_ONLINE;
            SvGetNetworkInfo(game);
        });

        modeOffline.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeOffline.setMaximumSize(new Dimension(300, 50));
        modeOffline.addActionListener(e ->
        {
            game.mode = GAME_MODE_OFFLINE;
            game.port = -1;

            SvGetFieldSize(game);
        });

        modeAuto.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeAuto.setMaximumSize(new Dimension(300, 50));
        modeAuto.addActionListener(e ->
        {
            game.mode = GAME_MODE_AUTO;
            SvGetNetworkInfo(game);
        });

        restoreGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        restoreGame.setMaximumSize(new Dimension(300, 50));
        restoreGame.addActionListener(e ->
        {

        });

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        menuPanel.add(modeOnline);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        menuPanel.add(modeOffline);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        menuPanel.add(modeAuto);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 75)));
        menuPanel.add(restoreGame);

        mainFrame.add(menuPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }
}
