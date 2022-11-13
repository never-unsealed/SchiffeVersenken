package gui;

import game.SvGame;
import util.SV_GAME_MODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

//Class to launch dialog asking for field size
public class SvFieldSizeDialog
{
    //Host field size info frame
    public static void SvGetFieldSize(SvGame game)
    {
        JFrame infoDialog = new JFrame("Game info");
        JButton connectButton = new JButton("Launch");
        JTextField textField = new JTextField("Enter field size (Ex. 5 for 5x5)");
        JTextField shipsField = new JTextField("Amount of ships & size (Ex. 5 5 4)");

        shipsField.setForeground(Color.GRAY);
        shipsField.setBounds(150, 80, 200, 30);
        shipsField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (shipsField.getText().equals("Amount of ships & size (Ex. 5 5 4)"))
                {
                    shipsField.setText("");
                    shipsField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(shipsField.getText().isEmpty())
                {
                    shipsField.setForeground(Color.GRAY);
                    shipsField.setText("Amount of ships & size (Ex. 5 5 4)");
                }
            }
        });


        textField.setForeground(Color.GRAY);
        textField.setBounds(150, 50, 200, 30);

        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (textField.getText().equals("Enter field size (Ex. 5 for 5x5)"))
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
                    textField.setText("Enter field size (Ex. 5 for 5x5)");
                }
            }
        });

        connectButton.setSize(100, 30);
        connectButton.setLocation(200, 120);
        connectButton.addActionListener(e ->
        {
            try
            {
                String[] strArray = shipsField.getText().split(" ");
                int totalUsed = 0, percentage;

                game.ships = new int[strArray.length];

                for(int i = 0; i < strArray.length; i++)
                {
                    game.ships[i] = Integer.parseInt(strArray[i]);
                    totalUsed += game.ships[i];

                    if(game.ships[i] > 5 || game.ships[i] < 2)
                        throw new Exception();
                }

                Arrays.sort(game.ships);

                for(int i = 0; i < game.ships.length / 2; i++)
                {
                    int temp = game.ships[i];
                    game.ships[i] = game.ships[game.ships.length - i - 1];
                    game.ships[game.ships.length - i - 1] = temp;
                }

                game.fieldSize = Integer.parseInt(textField.getText());

                if(game.fieldSize < 5 || game.fieldSize > 30)
                    throw new Exception();

                percentage = (totalUsed * 100) / (game.fieldSize * game.fieldSize);

                if(percentage > 40 || percentage < 10)
                    throw new Exception();

                infoDialog.dispose();

                if(game.mode == SV_GAME_MODE.GAME_MODE_OFFLINE)
                {
                    game.mode = SV_GAME_MODE.GAME_MODE_ONLINE;
                    game.hostname = null;
                    game.port = 4444;

                    Thread t1 = new Thread(() ->
                    {
                        SvGame botGame = new SvGame(null, true);
                        botGame.hostname = "localhost";
                        botGame.port = 4444;
                        botGame.mode = SV_GAME_MODE.GAME_MODE_AUTO;

                        botGame.loadGame();
                    });

                    t1.start();

                    game.loadGame();
                }
                else
                {
                    game.loadGame();
                }
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
        infoDialog.add(shipsField);

        infoDialog.setLayout(null);
        infoDialog.setResizable(false);
        infoDialog.setSize(500, 200);
        infoDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoDialog.setVisible(true);
    }
}
