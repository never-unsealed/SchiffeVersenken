package gui;

import util.SV_SHIP_COMPONENT_STATUS;
import util.SvFieldButton;
import util.SvShip;
import util.SvShipButton;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

//Class for managing game stage
public class SvGameStage
{
    private JFrame stageFrame;
    private SvFieldButton[] playerField;
    private SvFieldButton[] opponentField;
    public int fieldsOccupied;
    public SvShipButton lastSelect = null;
    public boolean latestSelectModeIsVertical = false;
    public SvShipButton[] shipsBtn;
    public JFrame shipsFrame;
    private int fieldSize;

    public SvShip[] ships;
    private int shipsPlaced = 0;

    public SvGameStage(int fieldSize, int amountShips)
    {
        JButton saveButton = new JButton("Save game.");
        JPanel gridPanel = new JPanel();
        JPanel footer = new JPanel();
        JPanel container = new JPanel();

        stageFrame = new JFrame("Ingame.");
        ships = new SvShip[amountShips];

        for(int i = 0; i < amountShips; i++)
            ships[i] = new SvShip();

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.fieldSize = fieldSize;

        playerField = new SvFieldButton[fieldSize * fieldSize];
        opponentField = new SvFieldButton[fieldSize * fieldSize];

        gridPanel.setLayout(new GridLayout(fieldSize * 2, fieldSize * 2));

        for(int i = 0; i < opponentField.length; i++)
        {
            int xcord, ycord;

            xcord = (i % fieldSize) + 1;
            ycord = (i / fieldSize) + 1;

            opponentField[i] = new SvFieldButton("O:" + ycord + "-" + xcord);
            opponentField[i].xcord = xcord;
            opponentField[i].ycord = ycord;
            opponentField[i].setBackground(Color.DARK_GRAY);
            opponentField[i].addActionListener(e ->
            {
                //Sink
            });

            gridPanel.add(opponentField[i]);
        }

        for(int i = 0; i < playerField.length; i++)
        {
            int xcord, ycord;

            xcord = (i % fieldSize) + 1;
            ycord = (i / fieldSize) + 1;

            playerField[i] = new SvFieldButton("P:" + ycord + "-" + xcord);
            playerField[i].xcord = xcord;
            playerField[i].ycord = ycord;
            playerField[i].setBackground(Color.LIGHT_GRAY);
            playerField[i].addActionListener(e ->
            {
                SvFieldButton currentButton = (SvFieldButton)e.getSource();

                this.placeShip(((currentButton.ycord - 1) * fieldSize) + (currentButton.xcord - 1));
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

            if(this.latestSelectModeIsVertical)
            {
                if(index + (this.lastSelect.size * this.fieldSize) > playerField.length)
                    throw new Exception("Ship too small to fit at that spot");
            }
            else
            {
                if((index + this.lastSelect.size) % fieldSize < index % fieldSize)
                    throw new Exception("Ship too small to fit at that spot");
            }

            for(int i = 0; i < this.lastSelect.size; i++)
            {
                if(this.latestSelectModeIsVertical)
                {
                    if(playerField[index + (i * this.fieldSize)].containsShip)
                        throw new Exception("Collides with existing ship");

                    playerField[index + (i * this.fieldSize)].setBackground(Color.pink);
                    playerField[index + (i * this.fieldSize)].containsShip = true;

                    this.ships[this.shipsPlaced].updateShipComponentStatus(
                            i,
                            index + (i * this.fieldSize),
                            SV_SHIP_COMPONENT_STATUS.SHIP_COMPONENT_STATUS_VALID
                    );
                }
                else
                {
                    if(playerField[index + i].containsShip)
                        throw new Exception("Collides with existing ship");

                    playerField[index + i].setBackground(Color.pink);
                    playerField[index + i].containsShip = true;

                    this.ships[this.shipsPlaced].updateShipComponentStatus(
                            i,
                            index + i,
                            SV_SHIP_COMPONENT_STATUS.SHIP_COMPONENT_STATUS_VALID
                    );
                }
            }

            this.shipsPlaced++;
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
            {
                this.shipsFrame.dispose();
            }
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
