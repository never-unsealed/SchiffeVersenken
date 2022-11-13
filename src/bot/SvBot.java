package bot;

import gui.SvGameStage;
import util.SvShipButton;

public class SvBot
{
    private SvGameStage stage;
    private int[] shipsArr;
    private int previousIndex = 0;

    public SvBot(int[] ships, SvGameStage stage)
    {
        this.stage = stage;
        this.shipsArr = ships;
    }

    public void placeShipsBot()
    {
        SvShipButton[] btnArr = new SvShipButton[shipsArr.length];

        stage.latestSelectModeIsVertical = true;
        stage.shipsBtn = btnArr;

        for(int i = 0; i < shipsArr.length; i++)
        {
            btnArr[i] = new SvShipButton(
                    "Ship #" + i +
                            " Size: " + shipsArr[i] +
                            " Vertical: " + stage.latestSelectModeIsVertical);

            btnArr[i].size = shipsArr[i];
            btnArr[i].index = i;
        }

        for(int i = 0; i < shipsArr.length; i++)
        {
            stage.lastSelect = btnArr[i];
            stage.placeShip(i);
        }
    }

    //Automatically shoot at opponent field
    public void shootFieldBot()
    {
        stage.opponentField[previousIndex].setEnabled(true);
        stage.opponentField[previousIndex++].doClick();
        System.out.println("Click");
    }
}
