package t_industries.monstersandportals.myserver;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
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

    public void startServerNew(UpdateServer updateServer){
        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
            MyServerListener listener = new MyServerListener(updateServer);
            server.addListener(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer(){
        server.stop();
    }

    public void sendPosition(int rolledNr, UpdateServer updateServer){
        UpdateClient updateClient = new UpdateClient();
        updateClient.setPosition(rolledNr);
        server.sendToAllTCP(updateClient);
        updateServer.setReadyForTurnServer(0);
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
