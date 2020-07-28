package com.ren.rabbitmq.producer.header;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author : renjiahui
 * @date : 2020/7/21 23:32
 * @desc :
 */
public class Producer_Header {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    //短信的队列名称
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    //交换机的名称
    public static final String EXCHANGE_HEADERS_INFORM = "exchange_headers_inform";


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
            channel.exchangeDeclare(EXCHANGE_HEADERS_INFORM, "headers");

            //header模式需要先设置好指定的键值对
            Map<String, Object> header_email = new Hashtable<String, Object>();
            header_email.put("inform_emial", "email");
            Map<String, Object> header_sms = new Hashtable<String, Object>();
            header_sms.put("infrom_sms", "sms");

            //进行交换机和队列绑定
            /**
             * queueBind()参数明细：String queue, String exchange, String routingKey, Map<String, Object> arguments
             * 1、queue 队列名称
             * 2、exchange：交换机名称
             * 3、routingKey： 路由Key，作用是交换机根据路由Key的值将消息转发到指定的队列中，在发布订阅模式中该值为空字符串
             * 4、arguments：键值对
             */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_HEADERS_INFORM, "", header_email);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_HEADERS_INFORM, "", header_sms);

            /**
             * basicPublish方法的参数明细：String exchange, String routingKey, boolean mandatory, boolean immediate, BasicProperties props, byte[] body
             * 1、exchange：交换机，如果不指定将使用mq的默认交换机，如果不适用，则使用""
             * 2、routingKey，路由键，交换机根据路由键来将消息转发到指定队列。如果使用默认交换机，路由键设置为队列的名称
             * 3、props：消息的属性
             * 4、body：消息内容
             */
            String message = "这是Header模式发送出来的消息";
            Map<String, Object> headers = new Hashtable<String, Object>();
            headers.put("inform_email", "email");
            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();
            properties.headers(headers);
            channel.basicPublish(EXCHANGE_HEADERS_INFORM, "", properties.build(), message.getBytes());
            System.out.println("发送邮件消息：" + message);

//            String message = "这是Header模式发送出来的消息";
//            Map<String, Object> headers = new Hashtable<String, Object>();
//            headers.put("inform_sms", "sms");
//            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties.Builder();
//            properties.headers(headers);
//            channel.basicPublish(EXCHANGE_HEADERS_INFORM, "", properties.build(), message.getBytes());
//            System.out.println("发送短信消息：" + message);


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
