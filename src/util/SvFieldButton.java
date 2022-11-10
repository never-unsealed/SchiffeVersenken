package util;

import javax.swing.*;

public class SvFieldButton extends JButton
{
    public int xcord;
    public int ycord;
    public boolean containsShip;
    public boolean revealed = false;

    public SvFieldButton(String text)
    {
        super(text);
    }
}
