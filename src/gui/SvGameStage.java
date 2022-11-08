package gui;

import util.SvFieldButton;
import util.SvShipButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Class for managing game stage
public class SvGameStage
{
    private JFrame stageFrame;
    private SvFieldButton[] playerField;
    private SvFieldButton[] opponentField;
    public int fieldsOccupied;
    public SvShipButton lastSelect = null;
    public SvShipButton[] shipsBtn;
    public JFrame shipsFrame;

    public SvGameStage(int fieldSize)
    {
        JButton saveButton = new JButton("Save game.");
        JPanel gridPanel = new JPanel();
        JPanel footer = new JPanel();
        JPanel container = new JPanel();

        stageFrame = new JFrame("Ingame.");

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        playerField = new SvFieldButton[fieldSize * fieldSize];
        opponentField = new SvFieldButton[fieldSize * fieldSize];

        gridPanel.setLayout(new GridLayout(fieldSize * 2, fieldSize * 2));

        for(int i = 0; i < opponentField.length; i++)
        {
            opponentField[i] = new SvFieldButton("O:");
            //Set coord
            opponentField[i].setBackground(Color.DARK_GRAY);
            opponentField[i].addActionListener(e ->
            {
                //Sink
            });

            gridPanel.add(opponentField[i]);
        }

        for(int i = 0; i < playerField.length; i++)
        {
            playerField[i] = new SvFieldButton("P:");
            //Set coord
            playerField[i].setBackground(Color.LIGHT_GRAY);
            playerField[i].addActionListener(e ->
            {
                SvFieldButton currentButton = (SvFieldButton)e.getSource();
                this.placeShip(0);
            });

            gridPanel.add(playerField[i]);
        }

        footer.add(saveButton);

        container.add(gridPanel);
        container.add(footer);

        stageFrame.add(container);
        stageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stageFrame.setResizable(true);
        stageFrame.pack();
        stageFrame.setVisible(true);

        this.changeButtonDisabledState(false, true);
    }

    static private String buttonFieldIndexToCoordinate(int i)
    {
        String retVal = null;



        return retVal;
    }

    //Change field disable state
    public void changeButtonDisabledState(boolean isEnable, boolean isOpponent)
    {
        SvFieldButton[] field;

        if(isOpponent)
            field = this.opponentField;
        else
            field = this.playerField;

        for(int i = 0; i < field.length; i++)
        {
            field[i].setEnabled(isEnable);
        }
    }

    //Place ship on field
    private void placeShip(int index)
    {
        boolean allHidden = true;

        try
        {
            if(this.lastSelect == null)
                throw new Exception("No ship selected");

            this.fieldsOccupied += this.lastSelect.size;

            this.lastSelect.setVisible(false);
            this.lastSelect.used = true;
            this.lastSelect = null;

            for(int x = 0; x < shipsBtn.length; x++)
            {
                if(!shipsBtn[x].used)
                    allHidden = false;

                shipsBtn[x].setEnabled(true);
            }

            if(allHidden)
                this.shipsFrame.dispose();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Error placing ship: " + e,
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
