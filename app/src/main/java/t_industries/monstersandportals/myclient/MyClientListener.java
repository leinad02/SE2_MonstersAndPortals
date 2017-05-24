package t_industries.monstersandportals.myclient;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ACKServer;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.RandomACK;
import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.ServerName;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;

/**
 * Created by Michi on 07.04.2017.
 */

public class MyClientListener extends Listener implements Serializable {
    ForClient forClient;
    UpdateClient updateClient;
    RiskClient riskClient;
    public MyClientListener(){}

    public MyClientListener(UpdateClient updateClient, RiskClient riskClient){
        this.updateClient = updateClient;
        this.riskClient = riskClient;
    }

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
        } else if(object instanceof UpdateClient){
            UpdateClient updateClient = (UpdateClient) object;
            this.updateClient.setPosition(updateClient.getPosition());
            connection.sendTCP(updateClient);
        } else if(object instanceof UpdateServer){
            UpdateServer updateServer = (UpdateServer) object;
        } else if(object instanceof ACKServer){
            ACKServer ackServer = (ACKServer) object;
            System.out.println(ackServer.getText());
            this.updateClient.setReadyForTurnClient(1);
            this.updateClient.setActiveSensorClient(1);
        } else if(object instanceof RiskClient){
            RiskClient riskClient = (RiskClient) object;
            if(riskClient.getText().equalsIgnoreCase("fail")){
                this.riskClient.setFailCounterClient(1);
            }
            this.riskClient.setCheckFieldClient(1);
        } else if(object instanceof RandomACK){
            RandomACK randomACK = (RandomACK) object;
            System.out.println(randomACK.getRandomCheck());
            this.updateClient.setCheckRandomNrClient(1);
        }
    }



}
