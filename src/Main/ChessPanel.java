package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessPanel extends JPanel
{
    private final int boardOffsetX = 50;
    private final int boardOffsetY = 50;
    private final int squareSize = 75;

    public Game game = new Game(null);

    private long lastPositionClickedBitBoard = 0L;

    public ChessPanel()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (true)
                {
                    int row = (e.getY() - boardOffsetY) / squareSize;
                    int column = (e.getX() - boardOffsetX) / squareSize;

                    if (row < 0 || row > 7 || column < 0 || column > 7) {
                        return;
                    }

                    int offset = row * 8 + column;

                    String positionClickedBinary = "0".repeat(offset) + "1" + "0".repeat(63 - offset);
                    long positionClickedBitBoard = Long.parseUnsignedLong(positionClickedBinary, 2);

                    if ((game.getFriendlyPieces() & positionClickedBitBoard) != 0) {
                        lastPositionClickedBitBoard = positionClickedBitBoard;
                    } else
                    {
                        char moveToMake = MoveGeneration.encodeMove(lastPositionClickedBitBoard, positionClickedBitBoard, (byte) 0);
                        char validMoveToMake = game.getValidMove(moveToMake);
                        if(validMoveToMake != 0)
                        {
                            game.makeEngineMove(validMoveToMake);
                        }
                    }

                    repaint();
                }
                else
                {
                    Engine engine = new Engine();
                    char startSquareMask = 0xFC00;
                    char endSquareMask = 0x03F0;

                    char move = engine.iterativeDeepening(5, game);
                    game.makeEngineMove(move);

                    repaint();
                }
            }
        });
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawChessboard(g2d);

        if((game.getFriendlyPieces() & lastPositionClickedBitBoard) != 0L)
        {
            ArrayList<Character> legalMoves = game.calculateLegalMoves(lastPositionClickedBitBoard);
            if(legalMoves != null)
            {
                drawPossibleSquares(g2d, game.convertEncodedMovesToBitboard(legalMoves));
                drawPossibleSquares(g2d, game.getAllPossibleEnemyAttacks());
            }
        }

        try {
            drawPieces(g2d);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawChessboard(Graphics2D g2d)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if((i + j) % 2 == 0){g2d.setColor(new Color(186, 202, 68));}
                else{g2d.setColor(new Color(238, 238, 210));}
                g2d.fillRect(boardOffsetX + i * squareSize, boardOffsetY + j * squareSize, squareSize, squareSize);
                g2d.setColor(new Color(0, 0, 0));
                g2d.drawRect(boardOffsetX + i * squareSize, boardOffsetY + j * squareSize, squareSize, squareSize);
            }
        }
    }

    public void drawPieces(Graphics2D g2d) throws IOException
    {
        HashMap<String, String> letterToImage = new HashMap<>();
        letterToImage.put("P", "Pieces/white-pawn.png");
        letterToImage.put("R", "Pieces/white-rook.png");
        letterToImage.put("N", "Pieces/white-knight.png");
        letterToImage.put("B", "Pieces/white-bishop.png");
        letterToImage.put("Q", "Pieces/white-queen.png");
        letterToImage.put("K", "Pieces/white-king.png");

        letterToImage.put("p", "Pieces/black-pawn.png");
        letterToImage.put("r", "Pieces/black-rook.png");
        letterToImage.put("n", "Pieces/black-knight.png");
        letterToImage.put("b", "Pieces/black-bishop.png");
        letterToImage.put("q", "Pieces/black-queen.png");
        letterToImage.put("k", "Pieces/black-king.png");


        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                String piece = Bitboard.convertBitBoardsToStringBoard(game.bitBoards)[i][j];
                if(piece != null)
                {
                    BufferedImage img = ImageIO.read(new File(letterToImage.get(piece)));
                    Image img2 = img.getScaledInstance(img.getWidth(null) / 2, img.getHeight(null) / 2, Image.SCALE_DEFAULT);
                    g2d.drawImage(img2, boardOffsetY + j * squareSize, boardOffsetX + i * squareSize, null);
                }
            }
        }

    }
    public void drawPossibleSquares(Graphics2D g2d, long possibleSquares)
    {
        for(int i = 0; i < 64; i++)
        {
            String stringBitBoard = String.format("%64s", Long.toBinaryString(possibleSquares)).replace(' ', '0');

            if (stringBitBoard.charAt(i) == '1')
            {
                int row = i / 8;
                int col = i - (row*8);
                g2d.setColor(new Color(144, 238, 144));
                g2d.fillRect(boardOffsetX + col * squareSize, boardOffsetY + row * squareSize, squareSize, squareSize);
                g2d.setColor(new Color(0, 0, 0));
                g2d.drawRect(boardOffsetX + col * squareSize, boardOffsetY + row * squareSize, squareSize, squareSize);
            }
        }
    }

}

