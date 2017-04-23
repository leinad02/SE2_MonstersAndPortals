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

    public void connectNew(String ip){
        try {
            client.start();
            client.connect(TIMEOUT, ip, TCP_PORT, UDP_PORT);
            client.addListener(new MyClientListener());
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

    private void registerKryoClasses(){
        kryo.register(LoginRequest.class);
        kryo.register(LoginResponse.class);
        kryo.register(ClientRegister.class);
        kryo.register(ClientName.class);
        kryo.register(ServerName.class);
        kryo.register(ForServer.class);
        kryo.register(ForClient.class);
        kryo.register(Message.class);
    }

    public void sendWelcomeMessage() {
        Message message = new Message();
        message.setMessage("Wir spielen jetzt zusammen");
        client.sendTCP(message);
    }
}
