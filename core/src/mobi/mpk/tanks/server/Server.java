package mobi.mpk.tanks.server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: US
 * Date: 19.04.16
 * Time: 22:47
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable {

    private DatagramSocket socket = null;
    private int serverPort = 4444;
    private final int NUMBER_OF_BOTS = 10;
    private ArrayList<PseudoTank> botList = new ArrayList<PseudoTank>();
    private Player p1 = null;
    private Player p2 = null;

    public Server() {

        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            botList.add(new PseudoTank(new Vector2(BotTank.rn.nextInt(500), BotTank.rn.nextInt(500)), new Vector2(BotTank.rn.nextInt(4) - 1.5f, 0)));
        }

        try {
            socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void run() {


        try {
            while (true) {
                if (p1 == null) {      // много одинакового кода, возможно надо будет выносить в методы суперкласа для классов клиента и сервера

                    byte[] incomingData = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(incomingData, 2048);
                    socket.receive(packet);
                    incomingData = packet.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
                    ObjectInputStream obj = new ObjectInputStream(in);
                    try {
                        Packet receivedData = (Packet) obj.readObject();

                        if (receivedData.getMsg().trim().equals("ready")) {

                            p1 = new Player(packet.getPort(), packet.getAddress(), socket);

                            sendPacket(socket, packet.getAddress(), packet.getPort(), new Packet("", "p1", 0, 0, 0, null, botList));


                            System.out.println("Player1 is ready");
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    if (p2 == null) {     // много одинакового кода, возможно надо будет выносить в методы суперкласа для классов клиента и сервера
                        byte[] incomingData = new byte[2048];
                        DatagramPacket packet = new DatagramPacket(incomingData, 2048);
                        socket.receive(packet);
                        incomingData = packet.getData();
                        ByteArrayInputStream in = new ByteArrayInputStream(incomingData);
                        ObjectInputStream obj = new ObjectInputStream(in);
                        try {
                            Packet receivedData = (Packet) obj.readObject();

                            if (receivedData.getMsg().trim().equals("ready")) {

                                p2 = new Player(packet.getPort(), packet.getAddress(), socket);

                                sendPacket(socket, packet.getAddress(), packet.getPort(), new Packet("", "p2", 0, 0, 0, null, botList));

                                System.out.println("Player2 is ready");
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (p1 != null && p2 != null) {

                    for (int i = 0; i < botList.size() ; i++) {
                       botList.get(i).update();
                    }

                    TimeUnit.MICROSECONDS.sleep(20);

                    Packet newData = getPacket(socket);
                    newData.setBotSet(botList);

                    if (newData.getWhoSent().trim().equals("p1")) {
                        sendPacket(socket, p2.getPlayerAddress(), p2.getPlayerPort(), newData);
                    }

                    if (newData.getWhoSent().trim().equals("p2")) {
                        sendPacket(socket, p1.getPlayerAddress(), p1.getPlayerPort(), newData);
                    }



                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // надо будет выносить в методы суперкласа, от него будут наследоваться классы клиент и сервер (те же методы в сервере)
    public Packet getPacket(DatagramSocket socket) {

        byte[] incomingData = new byte[2048];
        DatagramPacket packet = new DatagramPacket(incomingData, 2048);

        try {
            socket.receive(packet);
            incomingData = packet.getData();

            byte[] limitedData = new byte[packet.getLength()]; // что бы пакет был поменьше если использую далее
            for (int i = 0; i < packet.getLength(); i++) {
                limitedData[i] = incomingData[i];
            }
            ByteArrayInputStream in = new ByteArrayInputStream(limitedData);
            ObjectInputStream obj = new ObjectInputStream(in);
            return (Packet) obj.readObject();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    // надо будет выносить в методы суперкласа, от него будут наследоваться классы клиент и сервер (те же методы в сервере)
    public void sendPacket(DatagramSocket socket, InetAddress address, int serverPort, Packet dataPacket) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(dataPacket);
            oos.flush();

            byte[] buf = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, serverPort);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
