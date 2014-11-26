/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa.coder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class TextDecoder implements Decoder.Text<String> {

    @Override
    public String decode(String text) throws DecodeException {
        return text;
    }

    @Override
    public boolean willDecode(String text) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
    
    
    
}
