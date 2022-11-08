package gui;

import util.SvShipButton;

import javax.swing.*;
import java.awt.*;

//Frame for showing and selecting available ships during setup phase
public class SvShipList
{
    private SvShipButton[] shipSelect;
    private JFrame shipsFrame;

    public SvShipList(int[] shipsArr, SvGameStage stage)
    {
        JPanel list = new JPanel();
        this.shipsFrame = new JFrame("Select ships");
        this.shipSelect = new SvShipButton[shipsArr.length];

        stage.shipsBtn = this.shipSelect;
        stage.shipsFrame = this.shipsFrame;
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for(int i = 0; i < shipsArr.length; i++)
        {
            shipSelect[i] = new SvShipButton("Ship #" + i + " Size: " + shipsArr[i]);
            shipSelect[i].size = shipsArr[i];
            shipSelect[i].setMinimumSize(new Dimension(250, 50));
            shipSelect[i].addActionListener(e ->
            {
                SvShipButton currentButton = (SvShipButton)e.getSource();

                stage.lastSelect = currentButton;

                for(int x = 0; x < shipSelect.length; x++)
                    shipSelect[x].setEnabled(false);
            });

            list.add(shipSelect[i]);
        }

        shipsFrame.add(list);
        shipsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        shipsFrame.setResizable(false);
        shipsFrame.pack();
        shipsFrame.setMinimumSize(new Dimension(300, 200));
        shipsFrame.setVisible(true);
    }
}
