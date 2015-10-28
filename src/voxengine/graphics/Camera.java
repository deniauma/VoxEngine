/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics;

import voxengine.math.joml.Matrix3f;
import voxengine.math.joml.Matrix4f;
import voxengine.math.joml.Vector3f;

/**
 *
 * @author Deniauma
 */
public class Camera {
    
    public Vector3f position;
    public Vector3f view;
    
    private float previousAngle = 0f;
    
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
    
    private Vector3f calculateDirection() {
        Vector3f direction = new Vector3f();
        view.sub(position, direction);
        return direction;
    } 
    
    public void moveForward(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f mvm = new Vector3f(direction.x * move, direction.y * move, direction.z * move);
        position.add(mvm);
        view.add(mvm);
    }
    
    public void moveBackward(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f mvm = new Vector3f(direction.x * move, direction.y * move, direction.z * move);
        position.sub(mvm);
        view.sub(mvm);
    }
    
    public void moveRight(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f rightDirection = new Vector3f(direction.y, -direction.x, direction.z);
        Vector3f mvm = new Vector3f(rightDirection.x * move, rightDirection.y * move, rightDirection.z * move);
        position.add(mvm);
        view.add(mvm);
    }
    
    public void moveLeft(float move) {
        Vector3f direction = calculateNormalizedDirection();
        Vector3f leftDirection = new Vector3f(-direction.y, direction.x, direction.z);
        Vector3f mvm = new Vector3f(leftDirection.x * move, leftDirection.y * move, leftDirection.z * move);
        position.add(mvm);
        view.add(mvm);
    }
    
    public void turnRight(float angle) {
        if(angle != previousAngle) {
            System.out.println("Angle: " + angle);
            Vector3f newView = new Vector3f(view);
            new Matrix4f().translate(position)
                    .rotate((float) Math.toRadians(angle), 0f, 0f, 1f)
                    .translate(position.negate())
                    .transformPoint(newView);
            System.out.println("Camera position: " + position.x + " " + position.y + " " + position.z);
            System.out.println("Previous view: " + view.x + " " + view.y + " " + view.z + " New view: " + newView.x + " " + newView.y + " " + newView.z);
            view.set(newView);
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
    
}
