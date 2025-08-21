package com.wiross.board;

import com.wiross.logic.GameState;
import com.wiross.logic.MineBoardState;
import com.wiross.logic.MineBoardStateImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class MineBoardView extends JPanel implements MouseListener {
    private List<MineField> mineFields;
    private final int initX;
    private final int initY;
    private MineBoardState mineBoardState;
    private final Consumer<GameState> stateChangeConsumer;

    public MineBoardView(Consumer<GameState> stateChangeConsumer) {
        this(9, 9, stateChangeConsumer);
    }

    public MineBoardView(int x, int y, Consumer<GameState> stateChangeConsumer) {
        super();
        this.initX = x;
        this.initY = y;
        this.stateChangeConsumer = stateChangeConsumer;

        initField(x, y);
    }

    private void initField(int x, int y) {
        this.mineFields = new ArrayList<>(x * y);

        IntStream.range(0, x).forEach(nx ->
                IntStream.range(0, y).forEach(ny -> mineFields.add(new MineField(nx, ny))));

        setLayout(new GridLayout(y, x, 1, 1));
        mineFields.forEach(button -> button.addMouseListener(this));
        mineFields.forEach(this::add);

        this.mineBoardState = new MineBoardStateImpl(x, y, 10, stateChangeConsumer);
    }

    public MineField getField(int x, int y) {
        return mineFields.get(initY * x + y);
    }


    private void clickedOnField(int x, int y, MineField mineField, int checkNumber) {
        if (!mineBoardState.getGameState().hasEnded() && mineField.wasClickedBeforeOrChecked()) {
            int bombsAround = mineBoardState.countBombsAroundAndUncover(x, y);
            mineField.setAfterClick(bombsAround);

            if (0 == bombsAround && checkNumber < initX + initY) {
                for (int a = x - 1; a < x + 2; ++a) {
                    for (int b = y - 1; b < y + 2; ++b) {
                        if (a >= 0 && b >= 0 && a < initX && b < initY) {
                            clickedOnField(a, b, getField(a, b), 1 + checkNumber);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof MineField mineField) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                clickedOnField(mineField.getXPos(), mineField.getYPos(), mineField, 0);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (!mineBoardState.getGameState().hasEnded()) {
                    mineField.setUnsetFlag();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // empty
    }

    public void reset(int x, int y, int bombs, Consumer<GameState> stateChangeConsumer) {
        mineBoardState = new MineBoardStateImpl(x, y, bombs, stateChangeConsumer);
        mineFields.forEach(field -> new MineField(field.getXPos(), field.getYPos()));
    }
}
