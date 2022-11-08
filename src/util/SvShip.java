package util;

public class SvShip
{
    private SV_SHIP_COMPONENT_STATUS[] componentStatus;
    private int coordinate[];

    public SvShip()
    {
        coordinate = new int[5];
        componentStatus = new SV_SHIP_COMPONENT_STATUS[5];

        for(int i = 0; i < 5; i++)
            componentStatus[i] = SV_SHIP_COMPONENT_STATUS.SHIP_COMPONENT_STATUS_UNUSED;
    }

    public void updateShipComponentStatus(int index, int coordinate, SV_SHIP_COMPONENT_STATUS status)
    {
        this.coordinate[index] = coordinate;
        this.componentStatus[index] = status;
    }
}
