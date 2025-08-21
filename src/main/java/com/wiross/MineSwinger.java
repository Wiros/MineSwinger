package com.wiross;

import com.wiross.board.MineBoardView;

import javax.swing.*;

public class MineSwinger extends JFrame {

    MineSwinger() {
        super("MineSwinger");
        setSize(600, 400);
        setResizable(false);

        add(new MineBoardView());
        setVisible(true);
    }

    public static void main(String[] args) {
        new MineSwinger();
    }
}