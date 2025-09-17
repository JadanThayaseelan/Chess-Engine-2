package Main;

import javax.swing.*;

public class ChessFrame extends JFrame
{
    public ChessFrame()
    {
        setSize(800, 800);
        ChessPanel chessPanel = new ChessPanel();
        add(chessPanel);
        setVisible(true);
    }
}
