/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa;

import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import ua.org.cofriends.rsa.algo.RSA;
import ua.org.cofriends.rsa.coder.ConfigDecoder;
import ua.org.cofriends.rsa.coder.ConfigEncoder;
import ua.org.cofriends.rsa.coder.TextDecoder;
import ua.org.cofriends.rsa.entity.Config;

@ServerEndpoint(value = "/calcendpoint", encoders = {ConfigEncoder.class}, decoders = {ConfigDecoder.class, TextDecoder.class})
public class CalcEndpoint {

    @OnMessage
    public void onMessage(Config config, Session session) throws IOException, EncodeException {
        switch (config.command) {
            case ENCODE:
            case DECODE:
                session.getBasicRemote().sendObject(RSA.code(config));
                System.out.println(config.toString());
                break;

            default:
            case GENERATE:
                session.getBasicRemote().sendObject(RSA.generate());
                break;
        }
    }
}
