package br.com.mangarosa.interfaces.imp;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

public class MyConsumer implements Consumer {
    private final String name;

    public MyConsumer(String name){
        this.name = name;
    }

    @Override
    public boolean consume(Message message) {  
        // Marcar mensagem como consumida (true)
        message.setConsumed(true);
        return message.isConsumed();
    }

    @Override
    public String name() {
        return name;
    }
}
