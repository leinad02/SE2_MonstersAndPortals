package t_industries.monstersandportals.myclient;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.ServerName;

/**
 * Created by Michi on 07.04.2017.
 */

public class MyClientListener extends Listener implements Serializable {
    ForClient forClient;
    public MyClientListener(){}

    public MyClientListener(ForClient forClient) {
        this.forClient = forClient;
    }


    /*@Override
    public void connected(Connection connection){
        LoginRequest req = new LoginRequest();
        req.setMessageText("Ich möchte mich verbinden!");
        connection.sendTCP(req);
    }*/

    @Override
    public void received(Connection connection, Object object) {
        System.out.println("Message from server to client: ");
        if (object instanceof LoginResponse) {
            LoginResponse response = (LoginResponse) object;
            System.out.println(response.getResponseText());
        } else if(object instanceof ClientRegister){
            ClientRegister clientRegister = (ClientRegister) object;
            System.out.println(clientRegister.isLogin());
        } else if(object instanceof ServerName){
            ServerName serverName = (ServerName) object;
            this.forClient.setName(serverName.getNameFromServer());
        }
    }



}
