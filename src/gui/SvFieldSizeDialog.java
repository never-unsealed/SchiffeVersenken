package gui;

import game.SvGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

//Class to launch dialog asking for field size
public class SvFieldSizeDialog
{
    //Host field size info frame
    public static void SvGetFieldSize(SvGame game)
    {
        JFrame infoDialog = new JFrame("Enter field size (Ex. 5 for 5x5).");
        JButton connectButton = new JButton("Launch");
        JTextField textField = new JTextField("Enter field size");

        textField.setForeground(Color.GRAY);
        textField.setBounds(150, 70, 200, 30);
        textField.setFocusable(false);
        textField.setFocusable(true);
        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (textField.getText().equals("Enter field size"))
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
                    textField.setText("Enter field size");
                }
            }
        });

        connectButton.setSize(100, 30);
        connectButton.setLocation(200, 120);
        connectButton.addActionListener(e ->
        {
            try
            {
                game.fieldSize = Integer.parseInt(textField.getText());

                if(game.fieldSize < 5 || game.fieldSize > 30)
                    throw new Exception();

                infoDialog.dispose();
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

        infoDialog.add(connectButton);
        infoDialog.add(textField);

        infoDialog.setLayout(null);
        infoDialog.setResizable(false);
        infoDialog.setSize(500, 200);
        infoDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoDialog.setVisible(true);
    }
}
