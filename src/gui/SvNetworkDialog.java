package gui;

import game.SvGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.atomic.AtomicBoolean;

import static gui.SvFieldSizeDialog.SvGetFieldSize;

//Class to ask for network configuration
public class SvNetworkDialog
{
    //Host network info frame
    public static void SvGetNetworkInfo(SvGame game)
    {
        JFrame infoDialog = new JFrame("Enter network information.");
        JButton connectButton = new JButton("Continue");
        JCheckBox isServer = new JCheckBox();
        JLabel askServer = new JLabel("Start a new server?");
        JTextField textField = new JTextField("Enter [Hostname]:[Port]");
        AtomicBoolean isSelected = new AtomicBoolean(false);

        askServer.setBounds(150, 20, 150, 50);
        isServer.setBounds(270, 35, 20, 20);
        isServer.addActionListener(e ->
        {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            isSelected.set(abstractButton.getModel().isSelected());

            if(isSelected.get())
            {
                textField.setForeground(Color.GRAY);
                textField.setText("Enter [Port]");
            }
            else
            {
                textField.setForeground(Color.GRAY);
                textField.setText("Enter [Hostname]:[Port]");
            }
        });

        textField.setForeground(Color.GRAY);
        textField.setBounds(150, 70, 200, 30);
        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (textField.getText().equals("Enter [Port]") ||
                        textField.getText().equals("Enter [Hostname]:[Port]"))
                {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(textField.getText().isEmpty())
                {
                    textField.setForeground(Color.GRAY);
                    textField.setText(isSelected.get() ? "Enter [Port]" : "Enter [Hostname]:[Port]");
                }
            }
        });

        connectButton.setSize(100, 30);
        connectButton.setLocation(200, 120);
        connectButton.addActionListener(e ->
        {
            try
            {
                String portVal = textField.getText();

                if(!isSelected.get())
                {
                    portVal = textField.getText().split(":")[1];
                    game.hostname = textField.getText().split(":")[0];
                }
                else
                {
                    game.hostname = null;
                }

                game.port = Integer.parseInt(portVal);

                if(game.port <= 0)
                    throw new Exception();

                infoDialog.dispose();

                System.out.println("Connect to: " + game.hostname + ":" + game.port);

                if(game.hostname == null)
                    SvGetFieldSize(game);
                else
                    game.loadGame();
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid input",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        infoDialog.add(askServer);
        infoDialog.add(isServer);
        infoDialog.add(textField);
        infoDialog.add(connectButton);

        infoDialog.setLayout(null);
        infoDialog.setResizable(false);
        infoDialog.setSize(500, 200);
        infoDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoDialog.setVisible(true);
    }
}
