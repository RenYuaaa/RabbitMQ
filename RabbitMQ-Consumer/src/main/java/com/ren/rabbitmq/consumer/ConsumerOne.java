package com.ren.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : renjiahui
 * @date : 2020/7/7 0:30
 * @desc :
 */
public class ConsumerOne {

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
                    System.out.println("接收到消息" + message);
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
            channel.basicConsume(Queue, true, defaultConsumer);



            // 消费者接收消息
//            QueueingConsumer.Delivery delivery = null;
//            try {
//                delivery = consumer.nextDelivery();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            // 获取消息正文
//            String message = new String(delivery.getBody());
//            System.out.println("接收到的消息：" + message);

        } catch (IOException e) {
            System.out.println("接收消息失败" + e);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            //消费者这里如果关闭通道的话，会接收不到生产者的消息。
//            //关闭通道
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
