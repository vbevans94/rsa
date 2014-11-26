/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa.coder;

import com.google.gson.Gson;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import ua.org.cofriends.rsa.entity.Config;

public class ConfigEncoder implements Encoder.Text<Config> {
    
    public static final Gson GSON = new Gson();

    @Override
    public String encode(Config config) throws EncodeException {
        return GSON.toJson(config);
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}
