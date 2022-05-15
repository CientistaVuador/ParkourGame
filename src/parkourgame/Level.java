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
import parkourgame.Platform.StartLevelPlatform;

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
            platforms[i] = new LevelPlatform();
            platforms[i].getPosition().set(Math.random() / 2, 0, i / 2f);
        }
    }
    
    public void render(Graphics2D g, Camera cam) {
        for (int i = 0; i < platforms.length; i++) {
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
