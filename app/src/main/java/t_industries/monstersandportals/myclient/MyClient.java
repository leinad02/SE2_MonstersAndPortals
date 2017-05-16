package t_industries.monstersandportals.myclient;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ACKClient;
import t_industries.monstersandportals.NetworkClasses.ACKServer;
import t_industries.monstersandportals.NetworkClasses.ClientName;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.ServerName;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;

/**
 * Created by Michi on 07.04.2017.
 */

public class MyClient implements Serializable{
    private int TCP_PORT, UDP_PORT, TIMEOUT;
    private Client client;
    private Kryo kryo;

    public MyClient(int tcp, int udp, int timeout){
        this.TCP_PORT = tcp;
        this.UDP_PORT = udp;
        this.TIMEOUT = timeout;

        client = new Client();
        kryo = client.getKryo();
        registerKryoClasses();
    }

    public void connect(String serverIp, String name, ForClient forClient){
        try {
            client.start();
            client.connect(TIMEOUT, serverIp, TCP_PORT, UDP_PORT);
            client.addListener(new MyClientListener(forClient));
            ClientName clientName = new ClientName();
            clientName.setNameFromClient(name);
            client.sendTCP(clientName);
            LoginRequest req = new LoginRequest();
            req.setMessageText("verbunden");
            client.sendTCP(req);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while(forClient.getName() == null){
            System.out.println("Bitte warten");
        }

    }

    public void connectNew(String ip, UpdateClient updateClient, RiskClient riskClient){
        try {
            client.start();
            client.connect(TIMEOUT, ip, TCP_PORT, UDP_PORT);
            client.addListener(new MyClientListener(updateClient, riskClient));
            LoginRequest req = new LoginRequest();
            req.setMessageText("Hallo");
            client.sendTCP(req);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        client.stop();
    }

    public void sendWelcomeMessage() {
        Message message = new Message();
        message.setMessage("Wir spielen jetzt zusammen");
        client.sendTCP(message);
    }

    public void sendPosition(int rolledNr){
        UpdateServer updateServer = new UpdateServer();
        updateServer.setPosition(rolledNr);
        client.sendTCP(updateServer);
    }

    public void sendACK(UpdateClient updateClient){
        ACKClient ackClient = new ACKClient();
        ackClient.setText("fertig");
        client.sendTCP(ackClient);
        updateClient.setReadyForTurnClient(0);
    }

    public void sendRiskField(){
        RiskServer riskServer = new RiskServer();
        riskServer.setCheckFieldServer(1);
        riskServer.setText("success");
        client.sendTCP(riskServer);
    }

    public void sendRiskFail(){
        RiskServer riskServer = new RiskServer();
        riskServer.setText("fail");
        client.sendTCP(riskServer);
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
    }
}
