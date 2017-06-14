package t_industries.monstersandportals.myserver;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ACKClient;
import t_industries.monstersandportals.NetworkClasses.CheatClient;
import t_industries.monstersandportals.NetworkClasses.CheatServer;
import t_industries.monstersandportals.NetworkClasses.ClientName;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.DisconnectedServer;
import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.RandomNumberOne;
import t_industries.monstersandportals.NetworkClasses.RandomNumberZero;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;


/**
 * Created by Michi on 07.04.2017.
 */

public class MyServerListener extends Listener implements Serializable {
    String connText = "verbunden";
    ClientRegister clientRegister;
    String name;
    ForServer forServer;
    UpdateServer updateServer;
    RiskServer riskServer;
    CheatServer cheatServer;

    public MyServerListener(){
    }

    public MyServerListener(UpdateServer updateServer, RiskServer riskServer, CheatServer cheatServer){
        this.updateServer = updateServer;
        this.riskServer = riskServer;
        this.cheatServer = cheatServer;
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
        } else if(object instanceof UpdateServer){
            UpdateServer updateServer = (UpdateServer) object;
            this.updateServer.setPosition(updateServer.getPosition());
            connection.sendTCP(updateServer);
        } else if(object instanceof UpdateClient){
            UpdateClient updateClient = (UpdateClient) object;
        } else if(object instanceof ACKClient){
            ACKClient ackClient = (ACKClient) object;
            System.out.println(ackClient.getText());
            this.updateServer.setReadyForTurnServer(1);
            this.updateServer.setActiveSensorServer(1);
        } else if(object instanceof RiskServer){
            RiskServer riskServer = (RiskServer) object;
            if(riskServer.getText().equalsIgnoreCase("fail")){
                this.riskServer.setFailCounterServer(1);
            }
            this.riskServer.setCheckFieldServer(1);
        } else if(object instanceof RandomNumberZero){
            RandomNumberZero randomNumberZero = (RandomNumberZero) object;
            System.out.println(randomNumberZero.getRandomNumber());
            this.updateServer.setReadyForTurnServer(1);
            this.updateServer.setActiveSensorServer(1);
            this.updateServer.setCheckRandomNrServer(1);
        } else if(object instanceof  RandomNumberOne){
            RandomNumberOne randomNumberOne = (RandomNumberOne) object;
            System.out.println(randomNumberOne.getRandomNumber());
            this.updateServer.setCheckRandomNrServer(1);
        } else if(object instanceof CheatServer){
            CheatServer cheatServer = (CheatServer) object;
            if(cheatServer.getTextCheat().equalsIgnoreCase("detect")){
                this.cheatServer.setDetectCheat(1);
                this.cheatServer.setReadyCheatServer(0);
            } else if(cheatServer.getTextCheat().equalsIgnoreCase("cheaten")){
                this.cheatServer.setClientCheat(1);
                this.riskServer.setCheckFieldServer(1);
                this.cheatServer.setSuccessCheatClient(1);
            } else if(cheatServer.getTextCheat().equalsIgnoreCase("failcheat")){
                this.riskServer.setFailCounterServer(1);
                this.cheatServer.setSuccessCheatClient(1);
            }
        } else if(object instanceof DisconnectedServer){
            DisconnectedServer disconnectedServer = (DisconnectedServer) object;
            System.out.println(disconnectedServer.getTextDisconnect());
            this.updateServer.setIsConnectedServer(0);
        }

    }

}
