package game;

import network.SV_NETWORK_TYPE;
import network.SvNetwork;
import util.SV_GAME_MODE;

import javax.swing.*;

import static network.SV_NETWORK_TYPE.*;

//Main class for managing game
public class SvGame
{
    private JFrame mainFrame;

    public String hostname = null;
    public int fieldSize, port;
    public SV_GAME_MODE mode;

    public SvGame(JFrame frame)
    {
        this.mainFrame = frame;
    }

    //Load instance of game
    public void loadGame()
    {
        JFrame gameFrame = new JFrame("Game in progress");

        SV_NETWORK_TYPE type = this.hostname == null
                ?
                NETWORK_TYPE_SERVER
                :
                NETWORK_TYPE_CLIENT;

        mainFrame.setTitle("Connecting, please wait.");

        try
        {
            SvNetwork network = new SvNetwork(type, this.hostname, this.port);

            mainFrame.dispose();

            if(type == NETWORK_TYPE_SERVER)
            {
                network.sendWord("size " + this.fieldSize);

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
            }

            //Draw fields

            gameFrame.setSize(50 * this.fieldSize, 50 * this.fieldSize);

            //Let player chose ships

            //Send ready/wait ready

            //Enter while

            network.closeNetwork();
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
