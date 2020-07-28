package com.ren.rabbitmq.producer.returnlistener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author : renjiahui
 * @date : 2020/7/29 0:18
 * @desc : Return消费机制
 */
public class Producer_Return {

    public static void main(String[] args) throws Exception {
        //创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("39.107.94.251");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        //获取connection
        Connection connection = connectionFactory.newConnection();

        //创建信道
        Channel channel = connection.createChannel();

        String exchange = "test_return_exchange";
        String routingKey = "return.sava";
        String routingKeyError = "abc.save";

        String message = "Hello RabbitMQ Return Message";


        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println("---------- handle return ----------");
                System.out.println("replyCode：" + replyCode);
                System.out.println("replyText：" + replyText);
                System.out.println("exchange：" + exchange);
                System.out.println("routingKey：" + routingKey);
                System.out.println("properties：" + basicProperties);
                System.out.println("body：" + new String(bytes));
            }
        });

        channel.basicPublish(exchange, routingKeyError, true, null, message.getBytes());


//        channel.basicPublish(exchange, routingKeyError, true, null, message.getBytes());

    }
}
