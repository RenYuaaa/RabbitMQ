package com.ren.rabbitmq.consumer.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ren.rabbitmq.consumer.customize.MyConsumer;

/**
 * @author : renjiahui
 * @date : 2020/7/30 0:12
 * @desc : 消费的限流策略
 */
public class Consumer_Limit {

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
        String routingKey = "qos.#";
        String queueName = "test_qos_queuq";

        channel.exchangeDeclare(exchange, "topic", true, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchange, routingKey);


        channel.basicQos(0, 1, false);

        //限流方式中，autoAck一定要设置为false才生效
        channel.basicConsume(queueName, false, new MyConsumer(channel));

    }
}
