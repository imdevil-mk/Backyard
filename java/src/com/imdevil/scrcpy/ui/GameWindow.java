package com.imdevil.scrcpy.ui;

import com.imdevil.scrcpy.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.OutputStream;

public class GameWindow extends JPanel implements KeyMouseComponentListener {

    private JFrame frame;
    private Robot robot;
    private Point previousPoint;

    private KeyMouseManager mDevice;

    private int monitorWidth;
    private int monitorHeight;

    private final int windowWidth = 1280;
    private final int windowHeight = 800;
    private final int scrollGap = 100;

    private boolean needDrawPath = false;

    public void init(OutputStream outputStream) {

        frame = new JFrame("Drawing Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);

        // Set the default location of the window to the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        monitorWidth = screenSize.width;
        monitorHeight = screenSize.height;
        System.out.println("monitor = " + monitorWidth + "*" + monitorHeight);
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        frame.setLocation(centerX - frame.getWidth() / 2, centerY - frame.getHeight() / 2);

        mDevice = new KeyMouseManager(outputStream);

        frame.addComponentListener(this);

        frame.add(this);

        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            clearCanvas();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_QUOTE) {
            needDrawPath = !needDrawPath;
            if (needDrawPath) {
                clearCanvas();
                resetMouseToAnchor();
            }
        }
        mDevice.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mDevice.keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mDevice.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mDevice.mouseReleased(e);
    }

    Timer upTimer = new Timer(500, e -> {
        System.out.println("mouseUp: " + MathUtils.toString(previousPoint));
        resetMouse();
    });

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!needDrawPath) return;

        Point currentPoint = e.getPoint();

        if (previousPoint == null) {
            System.out.println("mouseDown: " + MathUtils.toString(currentPoint));
            mDevice.mouseDown(currentPoint);
        } else {
            int gap = 20;
            if (Math.abs(previousPoint.x - currentPoint.x) > gap || Math.abs(previousPoint.y - currentPoint.y) > gap) {
                System.out.println("mouseMoved: " + MathUtils.toString(currentPoint) + " <--- " + MathUtils.toString(previousPoint));
            }
        }

        if (!checkMouseInRange(currentPoint)) {
            resetMouse();
            return;
        }

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setColor(Color.BLACK);
        if (previousPoint != null) {
            g2.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y);
            mDevice.mouseMoved(currentPoint);
            upTimer.stop();
            upTimer.start();
        }
        previousPoint = currentPoint;
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (needDrawPath && e.getID() == MouseEvent.MOUSE_EXITED) {
            System.out.println("MOUSE_EXITED-------------" + e.getPoint());
            resetMouse();
        }
    }

    public void resetMouse() {
        upTimer.stop();
        mDevice.mouseUp(previousPoint);
        clearCanvas();
        resetMouseToAnchor();
    }

    private void clearCanvas() {
        Graphics g = getGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        previousPoint = null;
    }

    private void resetMouseToAnchor() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
        Point windowPosition = new Point(frame.getLocationOnScreen().x, frame.getLocationOnScreen().y);
        //System.out.println("windowPosition = " + MathUtils.toString(windowPosition));
        Point center = new Point(windowPosition.x + frame.getWidth() / 2, windowPosition.y + frame.getHeight() / 2);
        //中点向右偏移确保一直滑动的是方向
        center.x += 100;
        center.y -= 100;
        //System.out.println("center: " + MathUtils.toString(center));
        //由于Windows缩放，还需要变换得到缩放后的位置,本机缩放是1.25
        center.x /= 1.25;
        center.y /= 1.25;
        System.out.println("---------reset Mouse: " + MathUtils.toString(center));
        robot.mouseMove(-1, -1);
        robot.mouseMove(center.x, center.y);
    }

    private boolean checkMouseInRange(Point point) {
        return point.x > scrollGap && point.x < windowWidth - scrollGap
                && point.y > scrollGap && point.y < windowHeight - scrollGap;
    }
}
