package Main;

import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setSize(800, 800);

        ChessPanel chessPanel = new ChessPanel();
        frame.add(chessPanel);
        frame.setVisible(true);
    }
}