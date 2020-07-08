package com.ren.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : renjiahui
 * @date : 2020/7/7 0:31
 * @desc :
 */
public class ProducerOne {

    private static final String Queue = "hellorabbitmq";

    public static void main(String[] args) {

        //通过连接工厂创建心的连接和MQ建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.96.97.219");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //设置虚拟机，一个MQ的服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");

        //建立新连接
        Connection connection = null;
        Channel channel = null;
        try {
            //创建新连接
            connection = connectionFactory.newConnection();

            //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            channel = connection.createChannel();

            //声明队列
            /**
             * queueDeclare方法的参数明细：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
             * 1、queue：队列名称
             * 2、durable：是否持久化。如果持久化，则mq重启后队列还在
             * 3、exclusive：是否独占连接，队列只允许在改连接中访问，如果连接关闭队列自动删除；如果将此参数设置为true，可用于临时队列的创建
             * 4、autoDelete：是否自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实验临时队列
             * 5、arguments：参数，可以设置一个队列的扩展参数，比如可以设置存货时间
             */
            channel.queueDeclare(Queue, true, false, false, null);

            //发送消息
            /**
             * basicPublish方法的参数明细：String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body
             * 1、exchange：交换机，如果不指定将使用mq的默认交换机，如果不适用，则使用""
             * 2、routingKey，路由键，交换机根据路由键来将消息转发到指定队列。如果使用默认交换机，路由键设置为队列的名称
             * 3、props：消息的属性
             * 4、body：消息内容
             */
            String message = "Hello RabbitMQ, 这是一个测试";
            channel.basicPublish("", Queue, null, message.getBytes());
            System.out.println("发送消息" + message + "到RabbitMQ中");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            //关闭通道
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            //关闭连接
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
