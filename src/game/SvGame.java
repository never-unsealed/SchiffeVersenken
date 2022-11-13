package game;

import bot.SvBot;
import gui.SvGameStage;
import gui.SvShipList;
import network.SV_NETWORK_TYPE;
import network.SvNetwork;
import util.SV_GAME_MODE;

import javax.swing.*;

import java.util.Arrays;

import static network.SV_NETWORK_TYPE.*;

//Main class for managing game
public class SvGame
{
    private JFrame mainFrame;
    private boolean isHeadless;

    public String hostname = null;
    public int fieldSize, port;
    public int[] ships;
    public SV_GAME_MODE mode;

    public SvGame(JFrame frame, boolean isHeadless)
    {
        this.mainFrame = frame;
        this.isHeadless = isHeadless;
    }

    //Load instance of game
    public void loadGame()
    {
        SvGameStage stage;
        SvBot bot;

        SV_NETWORK_TYPE type = this.hostname == null
                ?
                NETWORK_TYPE_SERVER
                :
                NETWORK_TYPE_CLIENT;

        if(mainFrame != null)
            mainFrame.setTitle("Connecting, please wait.");

        try
        {
            SvNetwork network = new SvNetwork(type, this.hostname, this.port);

            if(mainFrame != null)
                mainFrame.dispose();

            if(type == NETWORK_TYPE_SERVER)
            {
                network.sendWord("size " + this.fieldSize);

                network.receiveWord();

                if(!network.outWord.contains("done"))
                    throw new Exception("Invalid word");

                network.sendWord(
                        "ships " + Arrays.toString(this.ships)
                                        .replace("[", "")
                                        .replace("]", "")
                                        .replace(",", "")
                );

                System.out.println("ships " + Arrays.toString(this.ships)
                        .replace("[", "")
                        .replace("]", "")
                        .replace(",", ""));

                network.receiveWord();

                if(!network.outWord.contains("done"))
                    throw new Exception("Invalid word");
            }
            else
            {
                network.receiveWord();

                if(!network.outWord.contains("size"))
                    throw new Exception("Invalid word");

                this.fieldSize = Integer.parseInt(network.outWord.split(" ")[1]);

                network.sendWord("done");

                network.receiveWord();

                System.out.println(network.outWord);

                if(!network.outWord.contains("ships"))
                    throw new Exception("Invalid word");

                String[] shipsArr = network.outWord.split(" ");
                this.ships = new int[shipsArr.length - 1];

                for(int i = 0; i < shipsArr.length - 1; i++)
                    this.ships[i] = Integer.parseInt(shipsArr[i + 1]);

                network.sendWord("done");
            }

            stage = new SvGameStage(this.fieldSize, this.ships.length, network, this.mode, !this.isHeadless);
            bot = new SvBot(this.ships, stage);
            stage.addBot(bot);

            if(type == NETWORK_TYPE_SERVER)
            {
                if(this.mode == SV_GAME_MODE.GAME_MODE_AUTO)
                {
                    bot.placeShipsBot();
                }
                else
                {
                    new SvShipList(this.ships, stage);
                }
            }
            else
            {
                stage.changeButtonDisabledState(false, false);

                //Show nonblocking msg

                network.receiveWord();

                if(!network.outWord.contains("ready"))
                    throw new Exception("Invalid word");

                stage.changeButtonDisabledState(true, false);

                if(this.mode == SV_GAME_MODE.GAME_MODE_AUTO)
                {
                    bot.placeShipsBot();
                }
                else
                {
                    new SvShipList(this.ships, stage);
                }
            }

            System.out.println("Done");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "An unexpected error occurred: " + e,
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE
            );

            System.exit(-1);
        }
    }
}
