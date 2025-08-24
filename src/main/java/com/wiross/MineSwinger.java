package com.wiross;

import com.wiross.panel.GamePanel;

import javax.swing.*;

public class MineSwinger extends JFrame {


    MineSwinger() {
        super("MineSwinger");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new GamePanel());
        setVisible(true);

    }

    public static void main(String[] args) {
        new MineSwinger();
    }
}