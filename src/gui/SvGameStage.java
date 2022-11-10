package gui;

import bot.SvBot;
import network.SV_NETWORK_TYPE;
import network.SvNetwork;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.net.SocketException;

import static util.SV_SHIP_COMPONENT_STATUS.*;

//Class for managing game stage
public class SvGameStage
{
    public SvNetwork network;
    private SvBot bot;
    private SV_GAME_MODE mode;
    private JFrame stageFrame;
    private SvFieldButton[] playerField;
    public SvFieldButton[] opponentField;
    public int fieldsOccupied;
    public SvShipButton lastSelect = null;
    public boolean latestSelectModeIsVertical = false;
    public SvShipButton[] shipsBtn;
    public JFrame shipsFrame;
    public int fieldSize;
    public SvShip[] ships;
    private int shipsPlaced = 0;
    private int opponentFields = 0;

    public SvGameStage(int fieldSize, int amountShips, SvNetwork network, SV_GAME_MODE mode)
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
        this.network = network;
        this.mode = mode;

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
                SvFieldButton currentButton = (SvFieldButton)e.getSource();

                this.stageCommandHandler(
                        true,
                        "shot " + currentButton.ycord + " " + currentButton.xcord
                );
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
                this.placeShip(coordinateToIndex(currentButton.ycord, currentButton.xcord));
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

    public void addBot(SvBot bot)
    {
        this.bot = bot;
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
            if(!field[i].revealed)
                field[i].setEnabled(isEnable);
        }
    }

    //Place ship on field
    public void placeShip(int index)
    {
        boolean allHidden = true;

        try
        {
            if(this.lastSelect == null)
                throw new Exception("No ship selected");

            if(this.latestSelectModeIsVertical)
            {
                if(index + ((this.lastSelect.size - 1) * this.fieldSize) > playerField.length)
                    throw new Exception("Ship too small to fit at that spot");
            }
            else
            {
                if((index + (this.lastSelect.size - 1)) % fieldSize < index % fieldSize)
                    throw new Exception("Ship too small to fit at that spot");
            }

            for(int i = 0; i < this.lastSelect.size; i++)
            {
                if(this.latestSelectModeIsVertical)
                {
                    if(playerField[index + (i * this.fieldSize)].containsShip)
                        throw new Exception("Collides with existing ship");
                }
                else
                {
                    if(playerField[index + i].containsShip)
                        throw new Exception("Collides with existing ship");
                }
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
            this.opponentFields += this.lastSelect.size;

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
                this.changeButtonDisabledState(false, false);

                this.network.sendWord("ready");

                if(network.networkType == SV_NETWORK_TYPE.NETWORK_TYPE_SERVER)
                {
                    network.receiveWord();

                    if(!network.outWord.contains("ready"))
                        throw new Exception("Invalid word");

                    this.changeButtonDisabledState(true, true);
                }
                else
                {
                    this.stageCommandHandler(false, null);
                }
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

    private int coordinateToIndex(int y, int x)
    {
        return ((y - 1) * this.fieldSize) + (x- 1);
    }

    private boolean updateShipShot(int fieldIndex)
    {
        boolean retVal = false;

        for(int i = 0; i < ships.length; i++)
        {
            for(int x = 0; x < 5; x++)
            {
                if(ships[i].coordinate[x] == fieldIndex)
                    ships[i].componentStatus[x] = SHIP_COMPONENT_STATUS_DESTROYED;
            }
        }

        for(int i = 0; i < ships.length; i++)
        {
            int cmp = 0;

            for(int x = 0; x < 5; x++)
            {
                if(ships[i].componentStatus[x] == SHIP_COMPONENT_STATUS_VALID)
                    cmp++;
            }

            if(cmp == 0)
            {
                ships[i].componentStatus[0] = SHIP_COMPONENT_STATUS_SUNK;
                retVal = true;
            }
        }

        return retVal;
    }

    private void stageCommandHandler(boolean sendWord, String word)
    {
        try
        {
            if(sendWord)
            {
                network.sendWord(word);
                this.changeButtonDisabledState(false, true);
            }

            network.receiveWord();

            System.out.println(network.outWord);

            if(network.outWord.contains("save"))
            {

            }
            else if(network.outWord.contains("shot"))
            {
                String answer;
                int index = coordinateToIndex(
                        Integer.parseInt(network.outWord.split(" ")[1]),
                        Integer.parseInt(network.outWord.split(" ")[2])
                );

                if(this.playerField[index].containsShip)
                {
                    System.out.println(this.fieldsOccupied);

                    if(this.fieldsOccupied-- == 1)
                    {
                        JOptionPane.showMessageDialog(
                                null,
                                "You lost the game",
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        this.network.closeNetwork();
                        System.exit(-1);
                    }

                    this.playerField[index].setBackground(Color.RED);

                    if(updateShipShot(index))
                    {
                        answer = "answer 2";
                    }
                    else
                    {
                        answer = "answer 1";
                    }
                }
                else
                {
                    this.playerField[index].setBackground(Color.BLUE);
                    answer = "answer 0";
                }

                stageCommandHandler(true, answer);
            }
            else if(network.outWord.contains("answer"))
            {
                int index = coordinateToIndex(
                        Integer.parseInt(word.split(" ")[1]),
                        Integer.parseInt(word.split(" ")[2])
                );

                this.opponentField[index].revealed = true;
                this.opponentField[index].setEnabled(false);

                if(network.outWord.contains("0"))
                {
                    this.opponentField[index].setBackground(Color.BLUE);
                    stageCommandHandler(true, "pass");
                }
                else if(network.outWord.contains("1") || network.outWord.contains("2"))
                {
                    this.opponentFields--;
                    this.opponentField[index].setBackground(Color.RED);
                    this.changeButtonDisabledState(true, true);
                }
            }
            else if(network.outWord.contains("pass"))
            {
                this.changeButtonDisabledState(true, true);
            }
            else
            {
                throw new Exception("Invalid word");
            }
        }
        catch(SocketException e)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "The game ended: " + (this.opponentFields == 1 ? "You won" : "Opponent disconnected"),
                    "End.",
                    JOptionPane.INFORMATION_MESSAGE
            );

            this.network.closeNetwork();
            System.exit(1);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Unexpected error: " + e,
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
