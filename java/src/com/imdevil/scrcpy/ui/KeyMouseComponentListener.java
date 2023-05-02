package com.imdevil.scrcpy.ui;

import java.awt.event.*;

public interface KeyMouseComponentListener extends MouseListener, MouseMotionListener, KeyListener , ComponentListener{
    @Override
    default void keyTyped(KeyEvent e) {

    }

    @Override
    default void keyPressed(KeyEvent e) {

    }

    @Override
    default void keyReleased(KeyEvent e) {

    }

    @Override
    default void mouseClicked(MouseEvent e) {

    }

    @Override
    default void mousePressed(MouseEvent e) {

    }

    @Override
    default void mouseReleased(MouseEvent e) {

    }

    @Override
    default void mouseEntered(MouseEvent e) {

    }

    @Override
    default void mouseExited(MouseEvent e) {

    }

    @Override
    default void mouseDragged(MouseEvent e) {

    }

    @Override
    default void mouseMoved(MouseEvent e) {

    }

    @Override
    default void componentResized(ComponentEvent e) {

    }

    @Override
    default void componentMoved(ComponentEvent e) {

    }

    @Override
    default void componentShown(ComponentEvent e) {

    }

    @Override
    default void componentHidden(ComponentEvent e) {

    }
}
