package bot;

import gui.SvGameStage;

import java.util.concurrent.ThreadLocalRandom;

public class SvBot
{
    private SvGameStage stage;
    private int[] shipsArr;

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
}
