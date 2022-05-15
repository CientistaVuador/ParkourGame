/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parkourgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 *
 * @author cientista
 */
public class Platform {

    public static final float SIZE = 0.1f;
    public static final float HEIGHT = 0.02f;

    //recycled math objects
    private static final Matrix4f model = new Matrix4f();
    private static final Matrix4f view = new Matrix4f();
    private static final Matrix4f projection = new Matrix4f();

    private static final Vector4f a = new Vector4f();
    private static final Vector4f b = new Vector4f();
    private static final Vector4f c = new Vector4f();
    private static final Vector4f d = new Vector4f();
    private static final Vector4f e = new Vector4f();

    private static final Vector4f ab = new Vector4f();
    private static final Vector4f bd = new Vector4f();
    private static final Vector4f cd = new Vector4f();
    private static final Vector4f ac = new Vector4f();

    private static final Vector4f ae = new Vector4f();
    private static final Vector4f be = new Vector4f();
    private static final Vector4f ce = new Vector4f();
    private static final Vector4f de = new Vector4f();
    //

    public static class StartLevelPlatform extends Platform {

        public StartLevelPlatform() {
            super(Color.GREEN);
        }
    }

    public static class LevelPlatform extends Platform {

        public LevelPlatform() {
            super(Color.WHITE);
        }
    }

    public static class EndLevelPlatform extends Platform {

        public EndLevelPlatform() {
            super(Color.YELLOW);
        }
    }

    private final Vector3f position = new Vector3f();
    private final Color color;

    public Platform(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Vector3f getPosition() {
        return position;
    }

    private boolean clip(float v) {
        return v > 1.0 || v < 0.0;
    }

    private void drawLine(Graphics2D g, Vector4f a, Vector4f b) {
        if (!a.isFinite() || !b.isFinite()) {
            return;
        }

        float aX = a.x() / a.w();
        float aY = a.y() / a.w();
        float aZ = a.z() / a.w();

        if (clip(aZ)) {
            return;
        }

        float bX = b.x() / b.w();
        float bY = b.y() / b.w();
        float bZ = b.z() / b.w();

        if (clip(bZ)) {
            return;
        }

        int screenXa = (int) (((aX + 1f) / 2f) * 800f);
        int screenYa = (int) (((-aY + 1f) / 2f) * 600f);
        int screenXb = (int) (((bX + 1f) / 2f) * 800f);
        int screenYb = (int) (((-bY + 1f) / 2f) * 600f);

        g.drawLine(screenXa, screenYa, screenXb, screenYb);
    }

    public void render(Graphics2D g, Camera cam) {
        float size = SIZE;

        a.set(-size, 0.0f, -size, 1.0f);
        b.set(size, 0.0f, -size, 1.0f);
        c.set(-size, 0.0f, size, 1.0f);
        d.set(size, 0.0f, size, 1.0f);
        e.set(0.0f, 0.0f, 0.0f, 1.0f);

        a.lerp(b, 0.5f, ab);
        b.lerp(d, 0.5f, bd);
        c.lerp(d, 0.5f, cd);
        a.lerp(c, 0.5f, ac);

        a.lerp(e, 0.5f, ae);
        c.lerp(e, 0.5f, ce);
        d.lerp(e, 0.5f, de);
        b.lerp(e, 0.5f, be);

        model.identity().translate(position);
        cam.getView(view);
        cam.getProjection(projection);

        projection.mul(view).mul(model);

        projection.transform(a);
        projection.transform(b);
        projection.transform(c);
        projection.transform(d);
        projection.transform(e);

        projection.transform(ab);
        projection.transform(bd);
        projection.transform(cd);
        projection.transform(ac);

        projection.transform(ae);
        projection.transform(be);
        projection.transform(ce);
        projection.transform(de);

        g.setColor(color);

        drawLine(g, a, ab);
        drawLine(g, ab, b);

        drawLine(g, b, bd);
        drawLine(g, bd, d);

        drawLine(g, d, cd);
        drawLine(g, cd, c);

        drawLine(g, c, ac);
        drawLine(g, ac, a);

        drawLine(g, a, ae);
        drawLine(g, ae, e);

        drawLine(g, b, be);
        drawLine(g, be, e);

        drawLine(g, d, de);
        drawLine(g, de, e);

        drawLine(g, c, ce);
        drawLine(g, ce, e);
    }

    public boolean checkCollision(Camera cam) {
        float platMaxX = position.x() + SIZE;
        float platMaxY = position.y() + HEIGHT;
        float platMaxZ = position.z() + SIZE;

        float platMinX = position.x() - SIZE;
        float platMinY = position.y() - HEIGHT;
        float platMinZ = position.z() - SIZE;

        float camHeight = 0.07f;
        float camWidth = 0.03f;

        float camMaxX = cam.getPosition().x() + camWidth;
        float camMaxY = cam.getPosition().y() + camHeight;
        float camMaxZ = cam.getPosition().z() + camWidth;

        float camMinX = cam.getPosition().x() - camWidth;
        float camMinY = cam.getPosition().y() - camHeight;
        float camMinZ = cam.getPosition().z() - camWidth;

        return (platMinX <= camMaxX && platMaxX >= camMinX)
                && (platMinY <= camMaxY && platMaxY >= camMinY)
                && (platMinZ <= camMaxZ && platMaxZ >= camMinZ);
    }

}
