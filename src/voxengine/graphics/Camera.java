/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics;

import voxengine.math.joml.Matrix4f;
import voxengine.math.joml.Vector3f;

/**
 *
 * @author Deniauma
 */
public class Camera {
    
    public Vector3f position;
    public Vector3f view;
    private boolean isUpdated = false;
    
    public Camera(float posX, float posY, float posZ, float viewX, float viewY, float viewZ) {
        position = new Vector3f(posX, posY, posZ);
        view = new Vector3f(viewX, viewY, viewZ);
    }
    
    public Camera(Vector3f position, Vector3f view) {
        this.position = position;
        this.view = view;
    }
    
    private Vector3f calculateNormalizedDirection() {
        Vector3f direction = new Vector3f();
        view.sub(position, direction);
        direction.normalize();
        return direction;
    }
    
    private Vector3f calculateNormalizedDirection(Vector3f u, Vector3f v) {
        Vector3f direction = new Vector3f();
        v.sub(u, direction);
        direction.normalize();
        return direction;
    }
    
    public void moveForward(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f mvm = new Vector3f(direction.x * move, direction.y * move, direction.z * move);
        position.add(mvm);
        view.add(mvm);
        isUpdated = true;
    }
    
    public void moveBackward(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f mvm = new Vector3f(direction.x * move, direction.y * move, direction.z * move);
        position.sub(mvm);
        view.sub(mvm);
        isUpdated = true;
    }
    
    public void moveRight(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f rightDirection = new Vector3f(direction.y, -direction.x, 0);
        Vector3f mvm = new Vector3f(rightDirection.x * move, rightDirection.y * move, rightDirection.z * move);
        position.add(mvm);
        view.add(mvm);
        isUpdated = true;
    }
    
    public void moveLeft(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f leftDirection = new Vector3f(-direction.y, direction.x, 0);
        Vector3f mvm = new Vector3f(leftDirection.x * move, leftDirection.y * move, leftDirection.z * move);
        position.add(mvm);
        view.add(mvm);
        isUpdated = true;
    }
    
    public void turn(float hAngle, float vAngle) {
        if(hAngle != 0) {
            Vector3f newView = new Vector3f(view);
            Vector3f center = new Vector3f(position);
            new Matrix4f().translate(center)
                    .rotate((float) Math.toRadians(360 - hAngle), 0f, 0f, 1f)
                    .translate(center.negate())
                    .transformPoint(newView);
            view.set(newView);
            isUpdated = true;
        }
        
        if(vAngle != 0) {
            Vector3f newView = new Vector3f(view);
            Vector3f center = new Vector3f(position);
            new Matrix4f().translate(center)
                    .rotate((float) Math.toRadians(360 - vAngle), 1f, 0f, 0f)
                    .translate(center.negate())
                    .transformPoint(newView);
            Vector3f baseDirection = calculateNormalizedDirection();
            baseDirection.z = 0;
            Vector3f newDirection = calculateNormalizedDirection(position, newView);
            float maxAngle = (float) Math.toDegrees(newDirection.angle(baseDirection));
            System.out.println("Max angle: "+maxAngle);
            if(maxAngle <= 80) {
                view.set(newView);
                isUpdated = true;
            }
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getView() {
        return view;
    }

    public void setView(Vector3f view) {
        this.view = view;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
    
}
