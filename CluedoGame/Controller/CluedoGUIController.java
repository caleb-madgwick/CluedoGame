package Controller;

import Model.Board;
import Model.CluedoGUIModel;
import Model.Player;
import Model.Tile;
import View.CluedoGUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.regex.Pattern;

public class CluedoGUIController {

    public CluedoGUIController() {
        

        CluedoGUI.getExitOption().addActionListener(e -> CluedoGUIModel.ExitOptionModel());

        CluedoGUI.getRestartGame().addActionListener(e -> {  CluedoGUIModel.RestartOptionModel();  });

        CluedoGUI.getEndTurn().addActionListener(e -> { CluedoGUIModel.EndTurnModel(); CluedoGUI.getGameControlPanel().requestFocus();});

        CluedoGUI.getRollDice().addActionListener(e -> {
            if (!CluedoGUI.isHasRolled()) {
                CluedoGUIModel.RollDiceValidModel();
            }
            else {
                CluedoGUIModel.RollDiceInvalidModel();
            }
            CluedoGUI.getGameControlPanel().requestFocus();
        });

        CluedoGUI.getMakeAccusation().addActionListener(e -> {
            if (Board.getCurrentPlayer().canMakeActions() && Board.getCurrentPlayer().isInARoom()) {
                CluedoGUIModel.MakeAccusationValidModel();
            }
            else {
                CluedoGUIModel.MakeAccusationInvalidModel();

            }
            CluedoGUI.getGameControlPanel().requestFocus();
        });

        CluedoGUI.getMakeSuggestion().addActionListener(e -> {
            if (!Board.getCurrentPlayer().hasMadeSuggestion() && Board.getCurrentPlayer().canMakeActions() && Board.getCurrentPlayer().isInARoom()) {
                try {
                    CluedoGUIModel.MakeSuggestionValidModel();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else {
                CluedoGUIModel.MakeSuggestionInvalidModel();
            }
            CluedoGUI.getGameControlPanel().requestFocus();
        });

        // Add A KeyListener to the GameControlPanel
        CluedoGUI.getGameControlPanel().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                //function keys
                if (e.getKeyChar() == '1') {
                    CluedoGUI.getEndTurn().doClick();
                }
                else if (e.getKeyChar() == '2') {
                        CluedoGUI.getRollDice().doClick();
                }
                else if (e.getKeyChar() == '3') {
                    CluedoGUI.getMakeAccusation().doClick();
                }
                else if (e.getKeyChar() == '4') {
                    CluedoGUI.getMakeSuggestion().doClick();
                }
                if (CluedoGUI.isHasRolled()) {

                    Board.setCurrentPlayer(Player.getPlayerList().get(CluedoGUI.getCurrentPlayerPos()));

                    int tileX = Board.getCurrentPlayer().getAssignedCharacter().currentTile.getCol() / 30;

                    int tileY = Board.getCurrentPlayer().getAssignedCharacter().currentTile.getRow() / 30;

                    CluedoGUI.getPreviouslyTraversedTiles().add(new Tile(Board.getBoardLayoutArray()[tileY][tileX],tileX*30,tileY*30));

                    Pattern pattern = Pattern.compile("(Wall)", Pattern.CASE_INSENSITIVE);

                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        CluedoGUIModel.MoveNorthModel(tileY, tileX, pattern);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        CluedoGUIModel.MoveSouthModel(tileY, tileX, pattern);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        CluedoGUIModel.MoveWestModel(tileY, tileX, pattern);
                    }

                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        CluedoGUIModel.MoveEastModel(tileY, tileX, pattern);
                    }

                    if (Board.getCurrentPlayer().getRemainingMoves() <= 0) {
                        CluedoGUI.getPreviouslyTraversedTiles().clear();
                        JOptionPane.showMessageDialog(null, "You now have no more moves", "No more moves", JOptionPane.PLAIN_MESSAGE);
                    }
                    CluedoGUI.getCluedoGame().repaint();
                }
                else {
                    JOptionPane.showMessageDialog(null, "You need to roll the dice before you can move", "You have not rolled", JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        CluedoGUI.getBoardPanel().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Board.setCurrentPlayer(Player.getPlayerList().get(CluedoGUI.getCurrentPlayerPos()));

                int tileX = Board.getCurrentPlayer().getAssignedCharacter().currentTile.getCol() / 30;

                int tileY = Board.getCurrentPlayer().getAssignedCharacter().currentTile.getRow() / 30;

                CluedoGUI.getPreviouslyTraversedTiles().add(new Tile(Board.getBoardLayoutArray()[tileY][tileX],tileX*30,tileY*30));

                Pattern pattern = Pattern.compile("(Wall)", Pattern.CASE_INSENSITIVE);
                if (Board.getCurrentPlayer().getRemainingMoves() <= 0) {
                    CluedoGUI.getPreviouslyTraversedTiles().clear();
                    JOptionPane.showMessageDialog(null, "You now have no more moves", "No more moves", JOptionPane.PLAIN_MESSAGE);
                }
                else if ( (e.getX() > Board.getCurrentPlayer().getAssignedCharacter().getCol() + 30
                      && e.getX() < Board.getCurrentPlayer().getAssignedCharacter().getCol() + 60)
                      && (e.getY() < Board.getCurrentPlayer().getAssignedCharacter().getRow() + 30) )
                {
                    CluedoGUIModel.MoveEastModel(tileY, tileX, pattern);
                }
                else if ( ( e.getX() < Board.getCurrentPlayer().getAssignedCharacter().getCol()
                        && e.getX() > Board.getCurrentPlayer().getAssignedCharacter().getCol() - 30)
                        && (e.getY() < Board.getCurrentPlayer().getAssignedCharacter().getRow() + 30))
                {
                    CluedoGUIModel.MoveWestModel(tileY, tileX, pattern);
                }
                else if (e.getY() < Board.getCurrentPlayer().getAssignedCharacter().getRow()
                        && e.getY() > Board.getCurrentPlayer().getAssignedCharacter().getRow() - 30)
                {
                    CluedoGUIModel.MoveNorthModel(tileY, tileX, pattern);
                }
                else if (e.getY() > Board.getCurrentPlayer().getAssignedCharacter().getRow() + 30
                        && e.getY() < Board.getCurrentPlayer().getAssignedCharacter().getRow() + 60)
                {
                    CluedoGUIModel.MoveSouthModel(tileY, tileX, pattern);
                }
                CluedoGUI.getCluedoGame().repaint();
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        CluedoGUI.getLeaveRoom().addActionListener(e -> {
            if (!Board.getCurrentPlayer().isInARoom()) {
                JOptionPane.showMessageDialog(null,
                        "Cannot do this right now",
                        "You are not leaving a room!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Board.getCurrentPlayer().changeEntranceOfPlayer();
            Board.getCurrentPlayer().setRemainingMoves(Board.getCurrentPlayer().getRemainingMoves() - 1);
            CluedoGUI.getCluedoGame().repaint();
        });
        
        CluedoGUI.getCluedoGame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                int result = JOptionPane.showConfirmDialog(CluedoGUI.getCluedoGame(),
                        "Do you want to Exit ?", "Exit Confirmation : ",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    CluedoGUI.getCluedoGame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                else if (result == JOptionPane.NO_OPTION)
                    CluedoGUI.getCluedoGame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }
}
