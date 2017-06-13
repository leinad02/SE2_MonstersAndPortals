package t_industries.monstersandportals.myserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ACKClient;
import t_industries.monstersandportals.NetworkClasses.ACKServer;
import t_industries.monstersandportals.NetworkClasses.CheatClient;
import t_industries.monstersandportals.NetworkClasses.CheatServer;
import t_industries.monstersandportals.NetworkClasses.ClientName;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.DisconnectedClient;
import t_industries.monstersandportals.NetworkClasses.DisconnectedServer;
import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.RandomACK;
import t_industries.monstersandportals.NetworkClasses.RandomNumberOne;
import t_industries.monstersandportals.NetworkClasses.RandomNumberZero;
import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.ServerName;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;

/**
 * Created by Michi on 07.04.2017.
 */

public class MyServer implements Serializable{
    private int TCP_PORT, UDP_PORT;
    private Server server;
    private Kryo kryo;
    ClientRegister clientRegister;

    public MyServer(int tcp, int udp){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;

        server = new Server();
        clientRegister = new ClientRegister();
        kryo = server.getKryo();
        registerKryoClasses();
    }

    public void startServer(String name, ForServer forServer){
        server.start();
            try {
                server.bind(TCP_PORT, UDP_PORT);
                MyServerListener listener = new MyServerListener(clientRegister, name, forServer);
                server.addListener(listener);
                while(!clientRegister.isLogin()){
                    System.out.println(clientRegister.isLogin());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        ServerName serverName = new ServerName();
        serverName.setNameFromServer(name);
        server.sendToAllTCP(serverName);
        }

    public void startServerNew(UpdateServer updateServer, RiskServer riskServer, CheatServer cheatServer){
        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
            MyServerListener listener = new MyServerListener(updateServer, riskServer, cheatServer);
            server.addListener(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer(){
        server.stop();
    }

    public void sendPosition(int rolledNr){
        UpdateClient updateClient = new UpdateClient();
        updateClient.setPosition(rolledNr);
        server.sendToAllTCP(updateClient);
    }

    public void sendACK(UpdateServer updateServer){
        ACKServer ackServer = new ACKServer();
        ackServer.setText("fertig");
        server.sendToAllTCP(ackServer);
        updateServer.setReadyForTurnServer(0);
    }

    public void sendRiskField(){
        RiskClient riskClient = new RiskClient();
        riskClient.setCheckFieldClient(1);
        riskClient.setText("success");
        server.sendToAllTCP(riskClient);
    }

    public void sendRiskFail(){
        RiskClient riskClient = new RiskClient();
        riskClient.setText("fail");
        server.sendToAllTCP(riskClient);
    }

    public void sendCheatMessage(){
        CheatClient cheatClient = new CheatClient();
        cheatClient.setTextCheat("cheaten");
        server.sendToAllTCP(cheatClient);
    }

    public void sendCheatMessageFail(){
        CheatClient cheatClient = new CheatClient();
        cheatClient.setTextCheat("failcheat");
        server.sendToAllTCP(cheatClient);
    }

    public void sendDetectMessage(CheatServer cheatServer){
        CheatClient cheatClient = new CheatClient();
        cheatClient.setTextCheat("detect");
        server.sendToAllTCP(cheatClient);
        cheatServer.setAllowFurtherClient(0);
    }

    public void sendACKRandom(){
        RandomACK randomACK = new RandomACK();
        randomACK.setRandomCheck(1);
        server.sendToAllTCP(randomACK);
    }

    public void sendEndConnection(UpdateServer updateServer){
        DisconnectedClient disconnectedClient = new DisconnectedClient();
        disconnectedClient.setTextDisconnect("disconnect");
        server.sendToAllTCP(disconnectedClient);
        updateServer.setIsConnectedServer(0);
    }

    private void registerKryoClasses(){
        kryo.register(LoginRequest.class);
        kryo.register(LoginResponse.class);
        kryo.register(ClientRegister.class);
        kryo.register(ClientName.class);
        kryo.register(ServerName.class);
        kryo.register(ForServer.class);
        kryo.register(ForClient.class);
        kryo.register(Message.class);
        kryo.register(UpdateClient.class);
        kryo.register(UpdateServer.class);
        kryo.register(ACKClient.class);
        kryo.register(ACKServer.class);
        kryo.register(RiskServer.class);
        kryo.register(RiskClient.class);
        kryo.register(RandomNumberOne.class);
        kryo.register(RandomNumberZero.class);
        kryo.register(RandomACK.class);
        kryo.register(CheatServer.class);
        kryo.register(CheatClient.class);
        kryo.register(DisconnectedClient.class);
        kryo.register(DisconnectedServer.class);
    }

}
