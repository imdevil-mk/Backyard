package com.imdevil.scrcpy;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AndroidMotionEvent {
    public static final int MOTION_EVENT_LENGTH = 28;

    public static final int TYPE_INJECT_TOUCH_EVENT = 2;

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_UP = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_CANCEL = 3;
    public static final int ACTION_OUTSIDE = 4;
    public static final int ACTION_POINTER_DOWN = 5;
    public static final int ACTION_POINTER_UP = 6;


    private OutputStream controlOutput;

    public AndroidMotionEvent(OutputStream outputStream) {
        controlOutput = outputStream;
    }

    public void sendTouchDown(int pointerId, Point point) {
        press(pointerId, point);
    }

    public void sendTouchMove(int pointerId, Point point) {
        move(pointerId, point);
    }

    public void sendTouchUp(int pointerId, Point point) {
        release(pointerId, point);
    }

    public void press(int pointerId, Point point) {
        byte[] data = new byte[MOTION_EVENT_LENGTH];

        composeAction(ACTION_DOWN, pointerId, point, 0, data);

        sendCommand(data);
    }

    public void scroll(int pointerId, Point start, Point end) {
        List<Point> points = spiltLine(start, end, 15, 15);
        byte[] data = new byte[MOTION_EVENT_LENGTH * points.size()];
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                composeAction(ACTION_DOWN, pointerId, points.get(i), i, data);
            } else {
                composeAction(ACTION_MOVE, pointerId, points.get(i), i, data);
            }
        }
        sendCommand(data);
    }

    public void move(int pointerId, Point point) {
        byte[] data = new byte[MOTION_EVENT_LENGTH];

        composeAction(ACTION_MOVE, pointerId, point, 0, data);

        sendCommand(data);
    }

    public void release(int pointerId, Point point) {
        byte[] data = new byte[MOTION_EVENT_LENGTH];

        composeAction(ACTION_UP, pointerId, point, 0, data);

        sendCommand(data);
    }

    public void click(int pointerId, Point point) {
        byte[] data = new byte[MOTION_EVENT_LENGTH * 2];

        composeAction(ACTION_DOWN, pointerId, point, 0, data);
        composeAction(ACTION_UP, pointerId, point, 1, data);

        sendCommand(data);
    }

    private void composeAction(int action, int pointerId, Point point, int index, byte[] bucket) {

        ByteBuffer buffer = ByteBuffer.wrap(bucket, index * MOTION_EVENT_LENGTH, MOTION_EVENT_LENGTH);

        buffer.put((byte) AndroidMotionEvent.TYPE_INJECT_TOUCH_EVENT);
        buffer.put((byte) action);

        buffer.putLong(pointerId);

        buffer.putInt(point.x);
        buffer.putInt(point.y);
        buffer.putShort((short) 2560);
        buffer.putShort((short) 1600);

        buffer.putShort((short) 2560);
        buffer.putInt(0);
    }

    private void sendCommand(byte[] command) {
        if (controlOutput == null) return;
        try {
            controlOutput.write(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Point> spiltLine(Point start, Point end, int xStep, int yStep) {
        List<Point> points;
        //it is not vertical
        if (end.x - start.x != 0) {
            int dx = end.x - start.x;
            if (dx < 0) {
                xStep = -xStep;
            }
            int count = Math.abs(dx / xStep) + 2;
            System.out.println("spiltLine: " + count + " point");
            points = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int x = i * xStep + start.x;
                if (i == count - 1) {
                    x = end.x;
                }
                int y = fx(start, end, x);
                points.add(new Point(x, y));
            }
        } else {
            int dy = end.y - start.y;
            if (dy < 0) {
                yStep = -yStep;
            }
            int count = Math.abs(dy / yStep) + 2;
            System.out.println("spiltLine: " + count + " point");
            points = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int y = i * yStep + start.y;
                if (i == count - 1) {
                    y = end.y;
                }
                points.add(new Point(end.x, y));
            }
        }
        return points;
    }

    private int fx(Point start, Point end, int x) {
        // 计算直线的斜率和截距
        double k = (double) (end.y - start.y) / (end.x - start.x);
        double b = start.y - k * start.x;

        // 计算对应的 y 坐标
        int y = (int) Math.round(k * x + b);

        return y;
    }

    private int fy(Point start, Point end, int y) {
        // 计算直线的斜率和截距
        double k = (double) (end.y - start.y) / (end.x - start.x);
        double b = start.y - k * start.x;

        // 计算对应的 y 坐标
        int x = (int) Math.round((y - b) / k);

        return x;
    }
}
