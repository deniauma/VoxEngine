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
    public Vector3f right;
    public Vector3f up;
    private boolean isUpdated = false;
    private float pitch = 0f;
    private float yaw = 0f;
    private Vector3f worldUp = new Vector3f(0f, 0f, 1f);
    
    public Camera(float posX, float posY, float posZ, float viewX, float viewY, float viewZ) {
        position = new Vector3f(posX, posY, posZ);
        view = new Vector3f(viewX, viewY, viewZ);
        up = worldUp;
        right = new Vector3f();
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
    
    public static Vector3f calculateNormalizedDirection(Vector3f u, Vector3f v) {
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
        if(hAngle != 0 || vAngle != 0) {
            isUpdated = true;
            this.yaw += hAngle;
            this.pitch += vAngle;
            if (this.pitch > 89) {
                this.pitch = 89;
            }
            if (this.pitch < -89) {
                this.pitch = -89;
            }
            Vector3f direction = new Vector3f();
            direction.x = (float) (Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
            direction.y = (float) (Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
            direction.z = (float) (Math.sin(Math.toRadians(this.pitch)));
            direction.normalize();
            direction.cross(this.worldUp, this.right);
            //this.right.cross(direction, this.up);
            position.add(direction, view);
            
            /*Vector3f direction = calculateNormalizedDirection();
            direction.z = 0;
            System.out.print("Direction: "+(int)view.x+" "+(int)view.y+" "+(int)view.z);
            new Matrix4f().rotate((float) Math.toRadians(this.pitch), 1f, 0f, 0f)
                        .rotate((float) Math.toRadians(360 - hAngle), 0f, 0f, 1f)
                        .transformPoint(direction);
            position.add(direction, view);
            direction.cross(this.worldUp, this.right);
            this.right.cross(direction, this.up);
            System.out.println(", new direction: "+(int)view.x+" "+(int)view.y+" "+(int)view.z);*/
        }
        
        /*if(hAngle != 0) {
            Vector3f newView = new Vector3f(view);
            //newView.normalize();
            Vector3f center = new Vector3f(position);
            new Matrix4f().translate(center)
                    .rotate((float) Math.toRadians(360 - hAngle), 0f, 0f, 1f)
                    .translate(center.negate())
                    .transformPoint(newView);
            view.set(newView);
            //view.normalize();
            isUpdated = true;
        }
        
        if(vAngle != 0) {
            this.pitch += vAngle;
            if (this.pitch > 89) {
                this.pitch = 89;
                isUpdated = true;
            }
            if (this.pitch < -89) {
                this.pitch = -89;
                isUpdated = true;
            }
            Vector3f direction = calculateNormalizedDirection();
            direction.z = 0;
            
            Vector3f newView = new Vector3f(view);
            newView.z = position.z;
            newView.normalize();
            Vector3f center = new Vector3f(position);
            new Matrix4f().rotate((float) Math.toRadians(this.pitch), 1f, 0f, 0f)
                    .transformPoint(direction);
            position.add(direction, view);
            //view.set(newView);
            System.out.println("Pitch: "+pitch+", view= "+view);
        }*/
    }
    
    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, view, up);
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
