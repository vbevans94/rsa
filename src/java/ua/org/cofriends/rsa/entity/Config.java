/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa.entity;

public class Config {
    
    public final String p;
    
    public final String q;
    
    public final Command command;
    
    public final String text;

    public Config(String p, String q, String text, Command command) {
        this.p = p;
        this.q = q;
        this.command = command;
        this.text = text;
    }

    public Config() {
        this(null, null, null, null);
    }

    @Override
    public String toString() {
        return "Config{" + "p=" + p + ", q=" + q + ", command=" + command + ", text=" + text + '}';
    }

    public enum Command {
        
        GENERATE, ENCODE, DECODE, ERROR
    }
}
