/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

/**
 *
 * @author AT347526
 */
public interface Scene {
    
    /**
     * Handles input of the state.
     */
    public void input();
    
    /**
     * Updates the state
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta);
    
    /**
     * Renders the state (with interpolation).
     *
     * @param alpha Alpha value, needed for interpolation
     */
    public void render(float alpha);
    
    /**
     * Executed before rendering loop
     */
    public void prepare();
    
    /**
     * Executed after rendering loop
     */
    public void quit();
}
