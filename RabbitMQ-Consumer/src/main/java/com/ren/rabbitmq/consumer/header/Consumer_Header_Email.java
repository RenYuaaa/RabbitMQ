package com.ren.rabbitmq.consumer.header;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author : renjiahui
 * @date : 2020/7/21 23:45
 * @desc :
 */
public class Consumer_Header_Email {

    //邮件的队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

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


        Connection connection = null;
        Channel channel = null;
        try {

            //创建新连接
            connection = connectionFactory.newConnection();

            //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            channel = connection.createChannel();


            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);

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

            //进行交换机和队列绑定
            /**
             * queueBind()参数明细：String queue, String exchange, String routingKey
             * 1、queue 队列名称
             * 2、exchange：交换机名称
             * 3、routingKey 路由Key，作用是交换机根据路由Key的值将消息转发到指定的队列中，在发布订阅模式中该值为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_HEADERS_INFORM, "", header_email);

//            实现消费的方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

                /**
                 * 当接收到消息后此方法将被调用
                 * @param consumerTag 消费者标签，用来识别消费者的，在监听队列时设置channel， basicConsume
                 * @param envelope 信封，通过envelope
                 * @param properties 消息属性
                 * @param body 消息内容
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //消息id，MQ在channel中用来表示消息的id，可用于确认消息已接收
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String message = new String(body, "utf-8");
                    System.out.println("接收到消息：" + message);

                    Map<String, Object> headers = properties.getHeaders();
                    System.out.println(headers.get("inform_emial"));
                }
            };

            //创建消费者
//            QueueingConsumer consumer = new QueueingConsumer(channel);

            //监听队列
            /**
             * basicConsume方法的参数明细：String queue, boolean autoAck, Consumer callback
             * 1、queue：队列名称
             * 2、autoAck：自动回复，当消费者接收到消费后要告诉mq消息已接受。如果将此参数设置为true，表示会自动回复mq；如果设置为false，则要通过编程实现回复
             * 3、callback：消费方法，当消费者接收到消息要执行的方法
             */
            channel.basicConsume(QUEUE_INFORM_EMAIL, true, defaultConsumer);

        } catch (IOException e) {
            System.out.println("接收消息失败" + e);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            //关闭通道
//            try {
//                channel.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                e.printStackTrace();
//            }

            //消费者要保存连接，所以消费者不关闭连接
        }
    }
}
