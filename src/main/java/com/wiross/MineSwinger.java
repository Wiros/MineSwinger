package com.wiross;

import com.wiross.board.MineBoardView;

import javax.swing.*;

public class MineSwinger extends JFrame {
    private static final MineBoardView boardView = new MineBoardView();


    MineSwinger() {
        super("MineSwinger");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(boardView);
        setVisible(true);

    }

    public static void main(String[] args) {
        new MineSwinger();
    }
}