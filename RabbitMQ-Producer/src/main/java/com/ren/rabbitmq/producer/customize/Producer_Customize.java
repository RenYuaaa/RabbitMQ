package com.ren.rabbitmq.producer.customize;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author : renjiahui
 * @date : 2020/7/29 23:34
 * @desc : 自定义消费者
 */
public class Producer_Customize {

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

        String exchange = "test_customize_exchange";
        String routingKey = "customize.sava";

        String message = "Hello RabbitMQ Customize Message";
        channel.basicPublish(exchange, routingKey, true, null, message.getBytes());

//        System.out.println("生产端发送消息：" + message);

    }
}
