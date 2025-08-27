package com.wiross.board;

import com.wiross.logic.BoardStateHelperImpl;
import com.wiross.logic.MineBoardState;
import com.wiross.logic.MineBoardStateImpl;
import com.wiross.panel.LeftBombsCounterUpdateEvent;
import com.wiross.panel.GamePanelUpdateEvent;
import com.wiross.utilities.RandomProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class MineBoardView extends JPanel implements MouseListener {
    private final int limitOnCheckingEmpty;
    private List<MineField> mineFields;
    private int initX;
    private int initY;
    private MineBoardState mineBoardState;
    private final Consumer<GamePanelUpdateEvent> gamePanelUpdateConsumer;
    private int currentBombsUnflagged;

    public MineBoardView(Consumer<GamePanelUpdateEvent> gamePanelUpdateConsumer) {
        this(9, 9, 10, gamePanelUpdateConsumer);
    }

    public MineBoardView(int x, int y, int bombs, Consumer<GamePanelUpdateEvent> gamePanelUpdateConsumer) {
        super();
        this.initX = x;
        this.initY = y;
        this.limitOnCheckingEmpty = 2 * (x + y);
        this.gamePanelUpdateConsumer = gamePanelUpdateConsumer;

        reset(x, y, bombs, gamePanelUpdateConsumer);
    }

    private void initField(int x, int y) {
        removeAll();
        if (null != mineFields) {
            mineFields.clear();
        }
        this.mineFields = new ArrayList<>(x * y);

        IntStream.range(0, x).forEach(nx ->
                IntStream.range(0, y).forEach(ny -> mineFields.add(new MineField(nx, ny))));

        setLayout(new GridLayout(y, x, 1, 1));
        mineFields.forEach(button -> button.addMouseListener(this));
        mineFields.forEach(this::add);
    }

    public MineField getField(int x, int y) {
        return mineFields.get(initY * x + y);
    }


    private void clickedOnField(int x, int y, MineField mineField, int checkNumber) {
        if (!mineBoardState.getGameState().hasEnded() && mineField.wasClickedBeforeOrChecked()
            && !mineField.isFlagged()) {
            int bombsAround = mineBoardState.countBombsAroundAndUncover(x, y);
            mineField.setAfterClick(bombsAround);

            System.out.println("Clicked: " + bombsAround);

            if (0 == bombsAround && checkNumber < limitOnCheckingEmpty) {
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
                    handleFlagging(mineField);
                }
            }
        }
    }

    private void handleFlagging(MineField mineField) {
        boolean flagSet = mineField.setUnsetFlag();
        if (flagSet) {
            if (currentBombsUnflagged == 0) {
                mineField.setUnsetFlag();
            } else {
                --currentBombsUnflagged;
            }
        } else {
            ++currentBombsUnflagged;
        }
        gamePanelUpdateConsumer.accept(new LeftBombsCounterUpdateEvent(currentBombsUnflagged));
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

    public void reset(int x, int y, int bombs, Consumer<GamePanelUpdateEvent> stateChangeConsumer) {
        System.out.println("MineBoardView.reset()");
        mineBoardState = new MineBoardStateImpl(x, y, bombs, stateChangeConsumer,
                new RandomProvider(), new BoardStateHelperImpl(x, y));

        initX = x;
        initY = y;

        initField(x, y);

        revalidate();
        repaint();

        currentBombsUnflagged = bombs;
        gamePanelUpdateConsumer.accept(new LeftBombsCounterUpdateEvent(currentBombsUnflagged));
    }
}
