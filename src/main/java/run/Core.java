package run;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Core extends JFrame {
    static Map<String, List<Long>>map;
    static ServerSocket socket;
    static Socket client;
    public Core(){
        long startTime=System.currentTimeMillis();
        Path path=Paths.get(System.getProperty("user.home")+File.separator+"data.json");
        if(!Files.exists(path)) try {
            Files.write(path,"{}".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            map=new Gson().fromJson(new FileReader(System.getProperty("user.home")+File.separator+"data.json"),new TypeToken<Map<String, List<Long>>>() {}.getType());
            if(Collections.frequency(map.entrySet().stream().map(e->e.getKey().substring(0,2)).collect(Collectors.toList()),"01")>1)
            {
                map=new HashMap<>();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                    save(startTime);
                System.out.println("Graphic boys win");


            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            save(startTime);
            System.out.println("Dirty console peasants achieved victory");

        }));
        //processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            socket=new ServerSocket(8331);
            client=socket.accept();
        } catch (IOException ex){
            ex.printStackTrace();
        }


    }
    public void save(long startTime)
    {
        String key=new SimpleDateFormat("ddMMYY").format(new Date(startTime));
        Long data=System.currentTimeMillis()-startTime;
        if(map.containsKey(key))
            map.get(key).add(data);
        else{
            ArrayList<Long> list=new ArrayList<>();
            list.add(data);
            map.put(key,list);
        }
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        try {
            Files.write(Paths.get(System.getProperty("user.home")+ File.separator+"data.json"),gson.toJson(map,new TypeToken<Map<String, List<Long>>>() {}.getType()).getBytes());
        } catch (IOException ex) {
        }
    }
}
