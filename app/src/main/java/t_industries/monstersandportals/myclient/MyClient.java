package t_industries.monstersandportals.myclient;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import t_industries.monstersandportals.NetworkClasses.ClientName;
import t_industries.monstersandportals.NetworkClasses.ClientRegister;
import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.NetworkClasses.LoginRequest;
import t_industries.monstersandportals.NetworkClasses.LoginResponse;
import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.ServerName;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;

import java.io.IOException;
import java.io.Serializable;

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

    public void connectNew(String ip, UpdateClient updateClient){
        try {
            client.start();
            client.connect(TIMEOUT, ip, TCP_PORT, UDP_PORT);
            client.addListener(new MyClientListener(updateClient));
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

    public void sendPosition(int rolledNr, UpdateClient updateClient){
        UpdateServer updateServer = new UpdateServer();
        updateServer.setPosition(rolledNr);
        client.sendTCP(updateServer);
        updateClient.setReadyForTurnClient(0);
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
    }
}
