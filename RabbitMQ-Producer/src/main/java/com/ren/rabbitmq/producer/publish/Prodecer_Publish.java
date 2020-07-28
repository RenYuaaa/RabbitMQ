package com.ren.rabbitmq.producer.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : renjiahui
 * @date : 2020/7/8 22:43
 * @desc : 发布订阅模式的生产者
 */
public class Prodecer_Publish {

    //email的队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    //短信的队列名称
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    //交换机的名称
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    public static void main(String[] args) {

        //通过连接工厂创建心的连接和MQ建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("39.107.94.251");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //设置虚拟机，一个MQ的服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");

        //建立新连接
        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            //声明一个交换机
            /**
             * exchangeDeclare()参数明细：String exchange, String type
             * 1、交换机的名称
             * 2、交换机的类型
             * fanout: 对应的rabbitMQ的工作模式是：publish/subscribe
             * direct：对用的Routing工作模式
             * topic：对应的Topics工作模式
             * Headers：对应的headers工作模式
             */
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, "fanout");

            //进行交换机和队列绑定
            /**
             * queueBind()参数明细：String queue, String exchange, String routingKey
             * 1、queue 队列名称
             * 2、exchange：交换机名称
             * 3、routingKey 路由Key，作用是交换机根据路由Key的值将消息转发到指定的队列中，在发布订阅模式中该值为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_FANOUT_INFORM, "");
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            String message = "这是发布订阅模式的消息";
            channel.basicPublish(QUEUE_INFORM_EMAIL, "", null, message.getBytes());
            channel.basicPublish(QUEUE_INFORM_SMS, "", null, message.getBytes());

            System.out.println("发送消息" + message);


        } catch (IOException e) {
            System.out.println("创建连接失败");
        } catch (TimeoutException e) {
            System.out.println("创建连接超时");
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
