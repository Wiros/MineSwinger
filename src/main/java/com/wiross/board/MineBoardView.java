package com.wiross.board;

import com.wiross.logic.FieldValue;
import com.wiross.logic.MineBoardState;
import com.wiross.logic.MineBoardStateImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MineBoardView extends JPanel implements ActionListener {
    private final List<MineField> mineFields;
    private final int initX;
    private final int initY;
    private final MineBoardState mineBoardState;

    public MineBoardView() {
        this(9, 9);
    }

    public MineBoardView(int x, int y) {
        super();
        initX = x;
        initY = y;
        mineFields = new ArrayList<>(x * y);
        IntStream.range(0, x).forEach(nx ->
                IntStream.range(0, y).forEach(ny -> mineFields.add(new MineField(nx, ny))));
        setLayout(new GridLayout(initY, initX, 1, 1));
        mineFields.forEach(this::add);
        mineFields.forEach(button -> button.addActionListener(this));

        mineBoardState = new MineBoardStateImpl(x, y, 10);
    }

    public MineField getField(int x, int y) {
        return mineFields.get(initY * x + y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof MineField mineField) {
            checkField(mineField.getX(), mineField.getY(), mineField, 0);
        }
    }

    private void checkField(int x, int y, MineField mineField, int checkNumber) {
        int bombsAround = mineBoardState.countBombsAround(x, y);
        String text = bombsAround == -1 ? "X" : bombsAround == 0 ? " " : Integer.toString(bombsAround);
        if (bombsAround == -1) {
            mineField.setBackground(Color.RED);
        } else {
            mineField.setBackground(Color.WHITE);
        }
        mineField.setText(text);
        Color textColor = switch (bombsAround) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.YELLOW;
            case 4 -> Color.ORANGE;
            case 5,6,7,8,9-> Color.RED;
            default -> Color.BLACK;
        };
        mineField.setForeground(textColor);

//        if (0 == bombsAround && checkNumber < 10) {
        if (0 == bombsAround && checkNumber < 1) {
            for (int a = x - 1; a < x + 2; ++a) {
                for (int b = y - 1; b < y + 2; ++b) {
                    if (a >= 0 && b >= 0 && a < initX && b < initY) {
                        System.out.println(getField(a, b));
                        checkField(a, b, getField(a, b), ++checkNumber);
                    }
                }
            }
        }
    }
}
