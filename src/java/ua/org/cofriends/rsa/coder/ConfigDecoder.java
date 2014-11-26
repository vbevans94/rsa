/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa.coder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import ua.org.cofriends.rsa.entity.Config;

public class ConfigDecoder implements Decoder.Text<Config> {

    @Override
    public Config decode(String text) throws DecodeException {
        return ConfigEncoder.GSON.fromJson(text, Config.class);
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
