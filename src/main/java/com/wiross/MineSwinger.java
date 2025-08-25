package com.wiross;

import com.wiross.panel.GamePanel;

import javax.swing.*;

public class MineSwinger extends JFrame {

    private final GamePanel gamePanel;

    MineSwinger() {
        super("MineSwinger");
        setSize(600, 400);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);

        setJMenuBar(createJMenuBar());

        setVisible(true);

    }

    private JMenuBar createJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu newGameMenu = new JMenu("New game");

        JMenuItem tutorialGame = new JMenuItem("New tutorial game.");
        tutorialGame.addActionListener((e) -> gamePanel.restartGame(3, 3, 2));

        JMenuItem simpleGame = new JMenuItem("New simple game.");
        simpleGame.addActionListener((e) -> gamePanel.restartGame(10, 10, 10));

        JMenuItem mediumGame = new JMenuItem("New medium game.");
        mediumGame.addActionListener((e) -> gamePanel.restartGame(15, 15, 35));

        JMenuItem hardGame = new JMenuItem("New hard game.");
        hardGame.addActionListener((e) -> gamePanel.restartGame(20, 20, 80));

        JMenuItem impossibleGame = new JMenuItem("Impossible.");
        impossibleGame.addActionListener((e) -> gamePanel.restartGame(20, 20, 300));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        newGameMenu.add(tutorialGame);
        newGameMenu.add(simpleGame);
        newGameMenu.add(mediumGame);
        newGameMenu.add(hardGame);
        newGameMenu.add(impossibleGame);


        gameMenu.add(newGameMenu);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        jMenuBar.add(gameMenu);
        return jMenuBar;
    }

    public static void main(String[] args) {
        new MineSwinger();
    }
}