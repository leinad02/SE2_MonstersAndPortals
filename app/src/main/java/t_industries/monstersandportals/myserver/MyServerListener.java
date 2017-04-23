package t_industries.monstersandportals.myserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ClientName;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.ServerName;


/**
 * Created by Michi on 07.04.2017.
 */

public class MyServerListener extends Listener implements Serializable {
    String connText = "verbunden";
    ClientRegister clientRegister;
    String name;
    ForServer forServer;

    public MyServerListener(){

    }

    public MyServerListener(ClientRegister clientRegister, String name, ForServer forServer) {
        this.clientRegister = clientRegister;
        this.name = name;
        this.forServer = forServer;
    }

    @Override
    public void received(Connection connection, Object object){
        if(object instanceof ClientName){
            ClientName clientname = (ClientName) object;
            this.forServer.setName(clientname.getNameFromClient());
        } else if(object instanceof LoginRequest){
            LoginRequest request = (LoginRequest) object;
            if(request.getMessageText().matches(connText)){
                System.out.println("Juchuuuu: " + clientRegister.isLogin());
                this.clientRegister.setLogin(true);
                connection.sendTCP(this.clientRegister);
            } else {
                System.out.println(request.getMessageText());
                LoginResponse response = new LoginResponse();
                response.setResponseText("Super!!!");
                connection.sendTCP(response);
            }
        } else if(object instanceof Message){
            Message message = (Message) object;
            System.out.println(message.getMessage());
            LoginResponse response = new LoginResponse();
            response.setResponseText("Testeintrag");
            connection.sendTCP(response);
        }
    }

}
