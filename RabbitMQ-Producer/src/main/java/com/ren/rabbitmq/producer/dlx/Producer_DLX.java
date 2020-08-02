package com.ren.rabbitmq.producer.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : renjiahui
 * @date : 2020/8/2 20:27
 * @desc : 死信队列
 */
public class Producer_DLX {

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

        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";
        String message = "Hello RabbitMQ dlx Message";

        for (int i = 0; i < 5; i++) {

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    //设置消息在队列中的过期时间
                    .expiration("10000")
                    .build();

            //发送一条消息
            channel.basicPublish(exchangeName, routingKey, true, properties, message.getBytes());
        }


    }
}
