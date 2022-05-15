/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkourgame;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import parkourgame.Platform.EndLevelPlatform;

/**
 *
 * @author cientista
 */
public class Game {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();

        JFrame frame = new JFrame("Parkour Game");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas();
        frame.add(canvas);

        final boolean[] w = {false};
        final boolean[] a = {false};
        final boolean[] s = {false};
        final boolean[] d = {false};
        final boolean[] shift = {false};
        final boolean[] space = {false};

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        w[0] = true;
                        break;
                    case KeyEvent.VK_A:
                        a[0] = true;
                        break;
                    case KeyEvent.VK_S:
                        s[0] = true;
                        break;
                    case KeyEvent.VK_D:
                        d[0] = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        shift[0] = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        space[0] = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        w[0] = false;
                        break;
                    case KeyEvent.VK_A:
                        a[0] = false;
                        break;
                    case KeyEvent.VK_S:
                        s[0] = false;
                        break;
                    case KeyEvent.VK_D:
                        d[0] = false;
                        break;
                    case KeyEvent.VK_SHIFT:
                        shift[0] = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        space[0] = false;
                        break;
                }
            }
        });

        final float[] mousePos = {0, 0};

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!canvas.isFocusOwner()) {
                    return;
                }

                int centerX = frame.getX() + (800 / 2);
                int centerY = frame.getY() + (600 / 2);

                int screenX = e.getXOnScreen();
                int screenY = e.getYOnScreen();

                synchronized (mousePos) {
                    mousePos[0] = screenX - centerX;
                    mousePos[1] = centerY - screenY;
                }

                robot.mouseMove(centerX, centerY);
            }
        });

        frame.setVisible(true);

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        canvas.setCursor(blankCursor);
        canvas.requestFocus();

        int frames = 0;
        long target = System.currentTimeMillis() + 1000;
        int fps = -1;
        float tpf = 0.016f;

        Camera cam = new Camera();

        int currentLevel = 0;
        
        Level level = new Level(10);

        while (true) {
            long here = System.nanoTime();

            BufferStrategy st = canvas.getBufferStrategy();
            if (st == null || st.contentsLost()) {
                canvas.createBufferStrategy(2);
                continue;
            }
            
            Platform collision = cam.processKeyboard(
                    level,
                    tpf,
                    w[0],
                    a[0],
                    s[0],
                    d[0],
                    shift[0],
                    space[0]
            );
            
            if (collision != null && collision instanceof EndLevelPlatform) {
                currentLevel++;
                level = new Level(10 + (currentLevel * 5));
                cam.resetPosition();
            }
            
            synchronized (mousePos) {
                cam.processMouseMovement(mousePos[0], mousePos[1]);
            }
            
            if (cam.getPosition().y() < -5f) {
                cam.resetPosition();
            }

            Graphics2D g = (Graphics2D) st.getDrawGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);

            g.setColor(Color.WHITE);
            if (fps == -1) {
                g.drawString("FPS: " + frames, 0, 14);
            } else {
                g.drawString("FPS: " + fps, 0, 14);
            }
            
            g.drawString("Level: "+currentLevel, 0, 28);

            level.render(g, cam);
            
            st.show();
            g.dispose();

            frames++;
            if (System.currentTimeMillis() >= target) {
                target = System.currentTimeMillis() + 1000;
                fps = frames;
                frames = 0;
            }

            tpf = (float) ((System.nanoTime() - here) / 1E9d);
        }
    }

}
