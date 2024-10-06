package br.com.mangarosa.interfaces.imp;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MyMessageRepository  implements MessageRepository{
    // Redis
    private RedisClient redisClient = RedisClient.create("redis://localhost:6379"); // change to reflect your environment
    private StatefulRedisConnection<String, String> connection = redisClient.connect();
    private RedisCommands<String, String> syncCommands = connection.sync();
    
    private Map<String, List<Message>> messages;

    public MyMessageRepository(){
        this.messages = new HashMap<>();
    }

    @Override
    public void append(String topic, Message message) {
        if(this.messages.containsKey(topic)){
            // Recebemos o valor da lista associada ao valor do topic, adicionamos à essa lista
            List<Message> messageList = this.messages.get(topic);
            messageList.add(message);

        } else {
            // Criar uma nova lista e adicionar ao Map contendo o novo valor de topic e a lista criada.
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            this.messages.put(topic, messageList);
            
            // Agora criamos a lista no Redis
            String messageId = syncCommands.xadd(topic,  "message", message.getMessage());
            System.out.println("Mensagem enviada para topic : " + topic);
            message.setId(messageId);
        }
    }

    @Override
    public void consumeMessage(String topic, String messageId) {
        List<Message> messageList = this.messages.get(topic);
        for (Message message : messageList) {
            if (messageId == message.getId();) {
                message.setConsumed(true);
            }
        }
    }
    

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        if (this.messages.containsKey(topic)) {
            List<Message> messageList = this.messages.get(topic).stream()
                .filter(message -> !message.isConsumed())
                .collect(Collectors.toList()); 
            return messageList;
        }
        return List.of();
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        if (this.messages.containsKey(topic)) {
            List<Message> messageList = this.messages.get(topic).stream()
                .filter(message -> message.isConsumed())
                .collect(Collectors.toList()); 
            return messageList;
        }
        return List.of();
    }
}