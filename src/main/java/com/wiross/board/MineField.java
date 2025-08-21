package com.wiross.board;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@Getter
public class MineField extends JButton {
    private final int x;
    private final int y;
    MineField(int x, int y) {
        super("?");
        this.x = x;
        this.y = y;
        setBackground(Color.LIGHT_GRAY);
        setBorderPainted(true);
        setBorder(new LineBorder(Color.BLACK, 1, true));
    }

    public void setClicked() {

    }
}
