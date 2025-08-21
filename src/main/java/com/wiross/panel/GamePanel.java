package com.wiross.panel;


import com.wiross.board.MineBoardView;
import com.wiross.logic.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private final JLabel stateLabel;
    private final JButton restartButton;
    private MineBoardView boardView;

    public GamePanel() {
        super();
        stateLabel = new JLabel("");
        restartButton = new JButton("Start");
        restartButton.addActionListener(this);
        boardView = new MineBoardView(this::onGameStateUpdate);


        setLayout(new BorderLayout());
        add(boardView, BorderLayout.CENTER);

        JPanel southBar = new JPanel();
        southBar.add(stateLabel);
        add(southBar, BorderLayout.SOUTH);

        JPanel northBar = new JPanel();
        northBar.add(restartButton);
        add(northBar, BorderLayout.NORTH);
    }

    void onGameStateUpdate(GameState gameState) {
        stateLabel.setText(gameState.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(restartButton)) {
            System.out.println("CLICKED RESET");
            boardView = new MineBoardView(this::onGameStateUpdate);
        }
    }
}
