package com.imdevil.scrcpy.ui;

import com.imdevil.scrcpy.AndroidMotionEvent;
import com.imdevil.scrcpy.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class KeyMouseManager implements KeyMouseComponentListener {

    public static final int VK_VISION = 0xF003;
    public static final int VK_MOUSE_LEFT = 0xF004;
    public static final int VK_MOUSE_MID = 0xF005;
    public static final int VK_MOUSE_RIGHT = 0xF006;
    public static final int VK_MOUSE_SIDE = 0xF007;
    public static final int VK_WALK = 0xF008;

    public static final int MOUSE_LEFT_BUTTON = 1;
    public static final int MOUSE_MID_BUTTON = 2;
    public static final int MOUSE_RIGHT_BUTTON = 3;
    public static final int MOUSE_SIDE_BUTTON = 4;

    private final AndroidMotionEvent mDevice;

    private final Stack<Integer> mAvailablePointerIds = new Stack<>();
    // <KeyCode, PointerId>
    private final HashMap<Integer, Integer> mDownKeys = new HashMap<>();

    public KeyMouseManager(OutputStream outputStream) {
        mDevice = new AndroidMotionEvent(outputStream);

        for (int i = 10; i >= 0; i--) {
            mAvailablePointerIds.push(i);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (isWheelEvent(keyCode)) {
            processWheelDown(keyCode);
            return;
        }

        if (!mDownKeys.containsKey(keyCode)) {
            Point point = fromKeyCode(keyCode);
            if (point == null) {
                return;
            }
            downKey(keyCode, point);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (isWheelEvent(keyCode)) {
            processWheelUp(keyCode);
            return;
        }

        Point point = fromKeyCode(keyCode);

        // if already DOWN, UP
        if (mDownKeys.containsKey(keyCode)) {
            upKey(keyCode, point);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int keyCode = mouseKeyCode(e.getButton());
        Point point = fromKeyCode(keyCode);

        // it`s already down, up first
        if (mDownKeys.containsKey(keyCode)) {
            upKey(keyCode, point);
        }

        downKey(keyCode, point);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int keyCode = mouseKeyCode(e.getButton());
        Point point = fromKeyCode(keyCode);

        // it`s already down, up
        if (mDownKeys.containsKey(keyCode)) {
            upKey(keyCode, point);
        }
    }

    public void mouseDown(Point p) {
        pointerDown(new Point(p.x * 2, p.y * 2));
    }

    public void mouseMoved(Point p) {
        pointerMoved(new Point(p.x * 2, p.y * 2));
    }

    public void mouseUp(Point p) {
        if (p == null) return;
        pointerUp(new Point(p.x * 2, p.y * 2));
    }

    private Point pointerDown = null;
    private Point lastPointer = null;

    private void pointerDown(Point p) {
        pointerDown = p;
    }

    private void pointerMoved(Point p) {
        if (pointerDown == null) {
            System.out.println("receive MOVE, but there is no DOWN before!");
            return;
        }

        // if there is no DOWN before, first DOWN
        if (!mDownKeys.containsKey(VK_VISION)) {
            downKey(VK_VISION, p);
        }

        // finally we can send move safe.
        moveKey(VK_VISION, p);
        lastPointer = p;
    }

    private void pointerUp(Point p) {
        if (mDownKeys.containsKey(VK_VISION)) {
            upKey(VK_VISION, p);
        }

        pointerDown = null;
        lastPointer = null;
    }

    private Point fromKeyCode(int keyCode) {
        System.out.println("key " + keyCode + " pressed");
        Point point = null;
        switch (keyCode) {
            case KeyEvent.VK_W:
                point = new Point(403, 1087);
                break;
            case KeyEvent.VK_A:
                point = new Point(292, 1198);
                break;
            case KeyEvent.VK_S:
                point = new Point(415, 1327);
                break;
            case KeyEvent.VK_D:
                point = new Point(516, 1214);
                break;
            case KeyEvent.VK_SPACE:
                point = new Point(2458, 1200);
                break;
            case VK_MOUSE_LEFT:
                point = new Point(476, 555);
                break;
            case VK_MOUSE_RIGHT:
                point = new Point(2191, 673);
                break;
            case KeyEvent.VK_R:
                point = new Point(1990, 1514);
                break;
        }
        return point;
    }

    private int mouseKeyCode(int button) {
        switch (button) {
            case MOUSE_LEFT_BUTTON:
                return VK_MOUSE_LEFT;
            case MOUSE_MID_BUTTON:
                return VK_MOUSE_MID;
            case MOUSE_RIGHT_BUTTON:
                return VK_MOUSE_RIGHT;
            case MOUSE_SIDE_BUTTON:
                return VK_MOUSE_SIDE;
        }
        return -1;
    }


    private final Set<Integer> mWheelKeyDowns = new HashSet<>(4);

    private static final int W = 0;
    private static final int A = 1;
    private static final int S = 2;
    private static final int D = 3;
    private static final int W_A = 4;
    private static final int W_D = 5;
    private static final int S_A = 6;
    private static final int S_D = 7;

    private boolean isWheelEvent(int keyCode) {
        return keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D;
    }

    private void processWheelDown(int keyCode) {
        if (mWheelKeyDowns.contains(keyCode)) {
            return;
        }

        mWheelKeyDowns.add(keyCode);

        int op;
        if (keyCode == KeyEvent.VK_W) {
            if (mWheelKeyDowns.contains(KeyEvent.VK_A)) {
                op = W_A;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_S)) {
                op = W;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_D)) {
                op = W_D;
            } else {
                op = W;
            }
        } else if (keyCode == KeyEvent.VK_A) {
            if (mWheelKeyDowns.contains(KeyEvent.VK_W)) {
                op = W_A;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_S)) {
                op = S_A;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_D)) {
                op = A;
            } else {
                op = A;
            }
        } else if (keyCode == KeyEvent.VK_S) {
            if (mWheelKeyDowns.contains(KeyEvent.VK_W)) {
                op = S;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_A)) {
                op = S_A;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_D)) {
                op = S_D;
            } else {
                op = S;
            }
        } else if (keyCode == KeyEvent.VK_D) {
            if (mWheelKeyDowns.contains(KeyEvent.VK_W)) {
                op = W_D;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_A)) {
                op = D;
            } else if (mWheelKeyDowns.contains(KeyEvent.VK_S)) {
                op = S_D;
            } else {
                op = D;
            }
        } else {
            op = -1;
        }
        if (op == -1) {
            System.out.println("something wrong!!");
            return;
        }

        // up
        wheelUp();

        wheelOp(op);
    }

    private void processWheelUp(int keyCode) {
        if (!mWheelKeyDowns.contains(keyCode)) {
            return;
        }
        int op;

        mWheelKeyDowns.remove(keyCode);
        if (mWheelKeyDowns.contains(KeyEvent.VK_W)) {
            op = W;
        } else if (mWheelKeyDowns.contains(KeyEvent.VK_A)) {
            op = A;
        } else if (mWheelKeyDowns.contains(KeyEvent.VK_S)) {
            op = S;
        } else if (mWheelKeyDowns.contains(KeyEvent.VK_D)) {
            op = D;
        } else {
            op = -1;
        }

        // up
        wheelUp();

        if (op == -1) {
            return;
        }
        wheelOp(op);
    }

    private static final Point wheelCenter = new Point(410, 1188);
    private static final Point wheelW = new Point(410, 1188 - 200);
    private static final Point wheelA = new Point(410 - 200, 1188);
    private static final Point wheelS = new Point(410, 1188 + 200);
    private static final Point wheelD = new Point(410 + 200, 1188);
    private static final Point wheelWA = new Point(410 - 200, 1188 - 200);
    private static final Point wheelWD = new Point(410 + 200, 1188 - 200);
    private static final Point wheelSA = new Point(410 - 200, 1188 + 200);
    private static final Point wheelSD = new Point(410 + 200, 1188 + 200);

    private static HashMap<Integer, Point> opToPoint = new HashMap<>();

    static {
        opToPoint.put(W, wheelW);
        opToPoint.put(A, wheelA);
        opToPoint.put(S, wheelS);
        opToPoint.put(D, wheelD);
        opToPoint.put(W_A, wheelWA);
        opToPoint.put(W_D, wheelWD);
        opToPoint.put(S_A, wheelSA);
        opToPoint.put(S_D, wheelSD);
    }

    private Point lastWheel = null;

    private void wheelOp(int op) {
        Point end = opToPoint.get(op);
        if (end == null) {
            System.out.println("somethings wrong when opToPoint op = " + op);
            return;
        }
        int pointerId = mAvailablePointerIds.pop();
        mDownKeys.put(VK_WALK, pointerId);
        mDevice.scroll(pointerId, wheelCenter, end);
        lastWheel = end;
    }

    private void wheelUp() {
        if (lastWheel != null) {
            System.out.println("wheelUp " + MathUtils.toString(lastWheel));
            if (mDownKeys.containsKey(VK_WALK)) {
                upKey(VK_WALK, lastWheel);
            }
            lastWheel = null;
        } else {
            System.out.println("lastWheel == null, can not up!");
        }
    }

    private void downKey(int keyCode, Point p) {
        int pointerId = mAvailablePointerIds.pop();
        mDownKeys.put(keyCode, pointerId);
        mDevice.sendTouchDown(pointerId, p);
    }

    private void moveKey(int keyCode, Point p) {
        int pointerId = mDownKeys.get(keyCode);
        mDevice.sendTouchMove(pointerId, p);
    }

    private void upKey(int keyCode, Point p) {
        int pointerId = mDownKeys.get(keyCode);
        mDownKeys.remove(keyCode);
        mAvailablePointerIds.push(pointerId);
        mDevice.sendTouchUp(pointerId, p);
    }
}
