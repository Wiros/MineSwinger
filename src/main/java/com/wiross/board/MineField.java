package com.wiross.board;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class MineField extends JButton {
    private static final String INIT_TEXT = "?";
    private static final String FLAG_TEXT = "v/";
    private static final String BOMB_TEXT = "X";
    private static final String EMPTY_TEXT = "";
    private static final Color INIT_COLOR = Color.LIGHT_GRAY;
    private static final Color FLAG_BG_COLOR = Color.BLUE;

    private final int xPos;
    private final int yPos;

    MineField(int xPos, int yPos) {
        super(INIT_TEXT);
        this.xPos = xPos;
        this.yPos = yPos;
        setBackground(INIT_COLOR);
    }

    public boolean wasClickedBeforeOrChecked() {
        return getText().equals(INIT_TEXT) || getText().equals(FLAG_TEXT);
    }

    public void setUnsetFlag() {
        String current = getText();
        if (current.equals(FLAG_TEXT)) {
            setText(INIT_TEXT);
            setBackground(INIT_COLOR);
        } else if (current.equals(INIT_TEXT)) {
            setText(FLAG_TEXT);
            setBackground(FLAG_BG_COLOR);
        }
    }

    public void setAfterClick(int bombsAround) {
        String text = bombsAround == -1 ? BOMB_TEXT : bombsAround == 0 ? EMPTY_TEXT : Integer.toString(bombsAround);
        if (bombsAround == -1) {
            setBackground(Color.RED);
        } else {
            setBackground(Color.WHITE);
        }
        setText(text);
        Color textColor = switch (bombsAround) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.YELLOW;
            case 4 -> Color.ORANGE;
            case 5,6,7,8,9-> Color.RED;
            default -> Color.BLACK;
        };
        setForeground(textColor);
    }
}
