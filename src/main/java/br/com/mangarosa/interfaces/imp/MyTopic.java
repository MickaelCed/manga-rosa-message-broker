package br.com.mangarosa.interfaces.imp;

import java.util.ArrayList;
import java.util.List;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class MyTopic implements Topic {
    private final String name;  // Nome
    private List<Consumer> consumerList;  // Lista contendo os consumidores inscritos nesse tópico 
    MessageRepository repository = new MyMessageRepository();


    public MyTopic(String name){
        this.name = name;
        this.consumerList = new ArrayList<>();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void addMessage(Message message) {
        repository.append(this.name, message);
    }

    @Override
    public void subscribe(Consumer consumer) {
        // Add um consumer a this.consumerList
        if (this.consumerList.contains(consumer)){
            throw new IllegalArgumentException("Consumer já está presente na lista");
        } else if (consumer == null) {
            throw new IllegalArgumentException("Consumer nulo");
        };
        this.consumerList.add(consumer);
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        // Remove um consumer de this.consumerList
        if (!this.consumerList.contains(consumer)){
            throw new IllegalArgumentException("Consumer não está presente na lista");
        } else if (consumer == null) {
            throw new IllegalArgumentException("Consumer nulo");
        };
        this.consumerList.remove(consumer);
    }

    @Override
    public List<Consumer> consumers() {
        return this.consumerList;
    }

    @Override
    public MessageRepository getRepository() {
        return repository;
    }
}
