package com.wiross.panel;


import com.wiross.board.MineBoardView;
import com.wiross.logic.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private static final long TIME_COUNT_MAX = 999_999;
    private final JLabel stateLabel;
    private final JLabel timeLabel;
    private final JLabel foundBombsLabel;
    private final JButton restartButton;
    private MineBoardView boardView;
    private GameState gameState;
    private long timeCount;
    private Timer timer;

    private int x;
    private int y;
    private int bombs;

    public GamePanel() {
        super();
        gameState = GameState.NOT_STARTED;
        stateLabel = new JLabel(gameState.toString());
        timeLabel = new JLabel("Time: 0");
        foundBombsLabel = new JLabel("Flagged bombs: 0");
        restartButton = new JButton("Start");
        restartButton.addActionListener(this);
        boardView = new MineBoardView(this::onGamePanelUpdate);


        setLayout(new BorderLayout());
        add(boardView, BorderLayout.CENTER);

        JPanel southBar = new JPanel();
        southBar.setLayout(new BorderLayout());
        southBar.add(stateLabel, BorderLayout.CENTER);
        stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southBar.add(timeLabel, BorderLayout.EAST);
        southBar.add(foundBombsLabel, BorderLayout.WEST);
        add(southBar, BorderLayout.SOUTH);

        JPanel northBar = new JPanel();
        northBar.add(restartButton);
        add(northBar, BorderLayout.NORTH);

        this.x = 10;
        this.y = 10;
        this.bombs = 10;
    }

    public void onGamePanelUpdate(GamePanelUpdateEvent updateEvent) {
        if (updateEvent instanceof GameState receivedGameState) {
            onGameStateUpdate(receivedGameState);
        } else if (updateEvent instanceof FlaggedBombsCounterUpdateEvent(int counter)) {
            updateMarkedBombsCounter(counter);
        }
    }

    private void updateMarkedBombsCounter(int bombs) {
        foundBombsLabel.setText("Flagged bombs: " + bombs);
    }

    private void onGameStateUpdate(GameState gameState) {
        stateLabel.setText(gameState.toString());
        this.gameState = gameState;

        if (gameState == GameState.IN_PROGRESS) {
            timeCount = 0;
            timeLabel.setText("Time: " + timeCount);
            timer = new Timer(1000, e -> {
                if (timeCount < TIME_COUNT_MAX) {
                    timeLabel.setText("Time: " + ++timeCount);
                }
            });
            timer.start();
        }

        if (gameState.hasEnded() && timer != null) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(restartButton)) {
            restartGame(x, y, bombs);
        }
    }

    public void restartGame(int x, int y, int bombs) {
        this.x = x;
        this.y = y;
        this.bombs = bombs;
        //boardView = new MineBoardView(x, y, bombs, this::onGamePanelUpdate);
        boardView.reset(x, y, bombs, this::onGamePanelUpdate);
        onGameStateUpdate(GameState.NOT_STARTED);
        revalidate();
        repaint();
    }
}
