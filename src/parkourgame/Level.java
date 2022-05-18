/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parkourgame;

import java.awt.Color;
import java.awt.Graphics2D;
import org.joml.Vector3f;
import parkourgame.Platform.EndLevelPlatform;
import parkourgame.Platform.LevelPlatform;
import parkourgame.Platform.MoveableCirclePlatform;
import parkourgame.Platform.MoveableXPlatform;
import parkourgame.Platform.MoveableYPlatform;
import parkourgame.Platform.StartLevelPlatform;
import parkourgame.Platform.UpdatablePlatform;

/**
 *
 * @author Cien
 */
public class Level {

    private final Platform[] platforms;

    public Level(int size) {
        if (size < 2) {
            size = 2;
        }
        this.platforms = new Platform[size];

        platforms[0] = new StartLevelPlatform();
        platforms[0].getPosition().set(0, 0, 0);

        platforms[platforms.length - 1] = new EndLevelPlatform();
        platforms[platforms.length - 1].getPosition().set(0, 0, (platforms.length - 1) / 2f);

        for (int i = 1; i < platforms.length - 1; i++) {
            if (Math.random() < 0.5) {
                platforms[i] = new LevelPlatform();
                platforms[i].getPosition().set(Math.random() / 2, 0, i / 2f);
            }
        }
        for (int i = 1; i < platforms.length - 1; i++) {
            if (platforms[i] == null) {
                if (!(platforms[i - 1] instanceof UpdatablePlatform)) {
                    int platformType = (int) (Math.random() * 3);
                    switch (platformType) {
                        case 0:
                            platforms[i] = new MoveableXPlatform();
                            break;
                        case 1:
                            platforms[i] = new MoveableYPlatform();
                            break;
                        case 2:
                            platforms[i] = new MoveableCirclePlatform(); 
                            break;
                        default:
                            platforms[i] = new Platform(Color.ORANGE);
                            break;
                    }
                    platforms[i].getPosition().set(Math.random() / 2, 0, i / 2f);
                } else {
                    platforms[i] = new LevelPlatform();
                    platforms[i].getPosition().set(Math.random() / 2, 0, i / 2f);
                }
            }
        }
    }

    public void render(Graphics2D g, Camera cam, float tpf) {
        for (int i = 0; i < platforms.length; i++) {
            if (platforms[i] instanceof UpdatablePlatform) {
                ((UpdatablePlatform) (platforms[i])).update(tpf);
            }
            platforms[i].render(g, cam);
        }
    }

    public Platform getCollision(Camera cam) {
        for (int i = 0; i < platforms.length; i++) {
            Platform platform = platforms[i];

            if (platform.checkCollision(cam)) {
                return platform;
            }
        }
        return null;
    }

}
