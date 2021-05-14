package com.hrapovick.demo;

import com.hrapovick.demo.domain.Warrior;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PrepareToBattle implements JavaDelegate {

    @Value("${maxWarriors}")
    private int maxWarriors;

    @Value("${url}")
    private String url;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        int warriors = (int) delegateExecution.getVariable("warriors");
        int enemyWarriors = new Random().nextInt(100);

        maxWarriors = maxWarriors == 0 ? 100 : maxWarriors;

        // Error boundary event
        if (warriors < 1 || warriors > maxWarriors) {
            throw new BpmnError("warriorsError");
        }

        List<Warrior> army = new ArrayList<>();

        for(int i = 0; i <= warriors; i++) {
            army.add(create());
        }

        System.out.println(String.format("Prepare to battle! Enemy army = %s vs. our army %s", enemyWarriors, warriors));
        delegateExecution.setVariable("army", army);
        delegateExecution.setVariable("enemyWarriors", enemyWarriors);

    }

    private Warrior create(){

        Warrior warrior = new Warrior();

        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest httpRequest = httpConnector.createRequest()
                .get()
                .url(url);
        Map<String, String> headers = new HashMap<>();
        httpRequest.setRequestParameter("headers", headers);

        headers.put("Content-type", "application/json");

        HttpResponse response = httpRequest.execute();
        if (response.getStatusCode() == 200) {
            SpinJsonNode node = Spin.JSON(response.getResponse());
            warrior.setTitle(node.prop("name").stringValue());
            warrior.setName(node.prop("title").stringValue());
            warrior.setHp((Integer) node.prop("hp").numberValue());
            warrior.setIsAlive(true);
        }
        response.close();
        return warrior;
    }
}
