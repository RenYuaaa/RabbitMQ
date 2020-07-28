package com.ren.rabbitmq.producer.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author : renjiahui
 * @date : 2020/7/28 22:55
 * @desc : 消息确认机制
 */
public class Producer_Confirm {

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

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        //发送一条消息
        String message = "Hello RabbitMQ Send Confirm Message!";
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());

        //添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {

            //没有收到消费端的应答
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("---------- ack! ----------");
            }

            //收到消费端的响应
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("---------- no ack! ----------");
            }
        });
    }
}
