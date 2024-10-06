package br.com.mangarosa;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.interfaces.imp.MyConsumer;
import br.com.mangarosa.interfaces.imp.MyMessageRepository;
import br.com.mangarosa.interfaces.imp.MyTopic;
import br.com.mangarosa.interfaces.imp.MyProducer;
import br.com.mangarosa.messages.Message;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        // Implementação dos dois tópicos
        Topic queueFastDeliveryItems = new MyTopic("queue/fast-delivery-items");
        Topic queueLongDistanceItems = new MyTopic("queue/long-distance-items");
        
        // Criação dos Producers e dando os seus respectivos tópicos
        Producer FoodDeliveryProducer = new MyProducer("queue/fast-delivery-items");
        Producer PhysicPersonDeliveryProducer = new MyProducer("queue/fast-delivery-items");
        FoodDeliveryProducer.addTopic(queueFastDeliveryItems);
        PhysicPersonDeliveryProducer.addTopic(queueFastDeliveryItems);

        Producer PyMarketPlaceProducer = new MyProducer("queue/long-distance-items");
        Producer FastDeliveryProducer = new MyProducer("queue/long-distance-items");
        PyMarketPlaceProducer.addTopic(queueLongDistanceItems);
        FastDeliveryProducer.addTopic(queueLongDistanceItems);

        // Criação de um Consumer para cada tópico.
        Consumer meuConsumer1 = new MyConsumer("meuconsumer1");
        Consumer meuConsumer2 = new MyConsumer("meuconsumer2");



        // Criando conexão no Redis usando o lettuce
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        // Enviando uma mensagem
        PyMarketPlaceProducer.sendMessage("minha mensagem de teste");
        FoodDeliveryProducer.sendMessage("minha segunda mensagem de teste");




        // Fechando o Redis para impedir erros
        connection.close();
        redisClient.shutdown();
    }
}