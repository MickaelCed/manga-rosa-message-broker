package br.com.mangarosa.interfaces.imp;

import java.util.ArrayList;
import java.util.List;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class MyProducer implements Producer {
    private List<Topic> topicList;
    private String name;

    public MyProducer(String name){
        this.topicList = new ArrayList<>();
        this.name = name;
    }

    @Override
    public void addTopic(Topic topic) {
        this.topicList.add(topic);
    }

    @Override
    public void removeTopic(Topic topic) {
        if (this.topicList.contains(topic)) {
            this.topicList.remove(topic);
        } else {
            throw new IllegalArgumentException("Tópico não está associado a esse Produtor.");
        }
        
    }

    @Override
    public void sendMessage(String message) {
        // Enviar para os Topics associados a esse Producer.
        if (!topicList.isEmpty()) {
            Message minhaMessage = new Message(this, message);
            for (Topic topic : topicList) {
                topic.addMessage(minhaMessage);
            }
        } else {
            System.out.println("Nenhum Tópico associado a esse Produtor.");
        }
    }

    @Override
    public String name() {
        return name;
    }

}
