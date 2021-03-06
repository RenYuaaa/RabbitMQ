package com.ren.rabbitmq.consumer.customize;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author : renjiahui
 * @date : 2020/7/29 23:41
 * @desc : 自定义的消费者类
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("---------- comsume message ----------");
        System.out.println("consumerTag：" + consumerTag);
        System.out.println("envelope：" + envelope);
        System.out.println("properties：" + properties);
        System.out.println("body：" + new String(body));

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //ack里面用
//        if ((Integer)properties.getHeaders().get("num") == 0) {
//            //最后一个参数代表是否重回队列
//            channel.basicNack(envelope.getDeliveryTag(), false, true);
//        } else {
//            //手动回复应答
//            channel.basicAck(envelope.getDeliveryTag(), false);
//        }

    }
}
