package com.ren.rabbitmq.producer.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author : renjiahui
 * @date : 2020/7/30 0:11
 * @desc : 消息的限流策略
 */
public class Producer_Limit {

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

        String exchange = "test_qos_exchange";
        String routingKey = "qos.sava";

        String message = "Hello RabbitMQ Qos Message";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchange, routingKey, true, null, message.getBytes());
        }


    }
}
