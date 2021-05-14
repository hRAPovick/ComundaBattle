package com.hrapovick.demo.controller;

import com.hrapovick.demo.domain.Warrior;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class JsonGenerator {

    @Value("${fileUrl}")
    private String path;

    List<String[]> people;
    private static final int NAME = 0;
    private static final int TITLE = 1;

    /**
     * Данный контроллер выдает объект Warrior в виде json
     * из List people.
     * @return объект Warrior.
     */
    @GetMapping("warrior")
    @Scope("prototype")
    public Warrior generator(){

        Warrior warrior = new Warrior();
        String[] body = people.get(new Random().nextInt(199));
        warrior.setName(body[NAME]);
        warrior.setTitle(body[TITLE]);
        warrior.setHp(new Random().nextInt(10000));
        warrior.setIsAlive(true);

        return warrior;
    }

    /**
     * Данный метод заполняет List people из файла path.
     * @throws URISyntaxException from File method.
     */
    @PostConstruct
    public void init() throws URISyntaxException {
        people = new ArrayList<>();
        URL dirURL = JsonGenerator.class.getResource(path);
        assert dirURL != null;
        File file = new File(dirURL.toURI());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                people.add(line.split("/"));
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
