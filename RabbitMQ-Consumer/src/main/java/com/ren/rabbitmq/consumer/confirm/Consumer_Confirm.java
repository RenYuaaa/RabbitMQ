package com.ren.rabbitmq.consumer.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author : renjiahui
 * @date : 2020/7/28 22:56
 * @desc : 消息确认机制
 */
public class Consumer_Confirm {

    public static void main(String[] args) throws Exception {

        //创建连接
        ConnectionFactory connectionFactory  = new ConnectionFactory();
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

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.#";
        String queueName = "test_confirm_queue";

        //声明交换机和队列，然后进行绑定蛇者，最后指定路由Key
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //实现消费的方法
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("消费端收到的消息：" + message);
        }


    }
}
