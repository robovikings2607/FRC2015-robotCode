/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.archwood.frc2607;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author frcdev
 */
public class SmoothedEncoder extends Encoder implements Runnable {
    
    private int numSamples = 20;
    private double[] sampleValues = new double[numSamples];
    private double currentRate;
    
    public SmoothedEncoder(int chA, int chB, boolean reversed, EncodingType type) {
        super(chA, chB, reversed, type);
        setPIDSourceParameter(PIDSourceParameter.kRate);
        new Thread(this).start();
    }
            
    public double getCurrentRate() {
        return currentRate;
    }
    
    public double pidGet() {
        return currentRate;
    }

    public void run() {
        while (true) {
        for (int i = sampleValues.length - 1; i > 0; i--) {
            sampleValues[i] = sampleValues[i-1];
        }
        sampleValues[0] = getRate();
        double[] median = Arrays.copyOf(sampleValues, numSamples);
        Arrays.sort(median);
        currentRate = median[median.length/2];       
        try { Thread.sleep(5); } catch (Exception e) {}
        }
    }   
}
