package bot;

import gui.SvGameStage;

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
        for(int i = 0; i < shipsArr.length; i++)
        {

        }
    }

    //Automatically shoot at opponent field
    public void shootFieldBot()
    {
        stage.opponentField[previousIndex++].doClick();
    }
}
