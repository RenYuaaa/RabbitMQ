package com.ren.rabbitmq.producer.ack;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : renjiahui
 * @date : 2020/8/2 19:35
 * @desc : 消费端的ACK
 */
public class Producer_ACK {

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

        //指定消息投递模式：消息确认模式
        channel.confirmSelect();

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.save";



        for (int i = 0; i < 5; i++) {

            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("num", i);

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    //设置消息在队列中的过期时间
                    .expiration("10000")
                    .headers(headers)
                    .build();

            //发送一条消息
            String message = "Hello RabbitMQ ACK Message " + i;
            channel.basicPublish(exchangeName, routingKey, true, properties, message.getBytes());
        }

    }
}
