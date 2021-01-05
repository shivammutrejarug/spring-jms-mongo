package com.codenotfound;

//import com.codenotfound.jms.Receiver;
//import com.codenotfound.jms.Sender;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SpringJmsApplicationTest {
//
//    @Autowired
//    private Sender sender;
//
//    @Autowired
//    private Receiver receiver;
//
//    @Test
//    public void testReceive() throws Exception {
//         sender.send("Hello Spring JMS ActiveMQ!", "helloworld.q");
//
//         receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
//         assertThat(receiver.getLatch().getCount(), 0).isEqualTo(0);
//    }
//}
