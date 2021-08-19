package com.lp.akka.notes;

import akka.actor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author li.pan
 * @title Actor 集成到Spring框架中
 */
@Component("actorDemo")
@Scope("prototype")
public class SpringIntegrationActor extends UntypedActor {
//    @Autowired
//    private EmpService empService;

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        System.out.println("receive: " + message);
//        empService.saveEmp((String)message);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        // 定义扫描的package
        ctx.scan("com.lp.akka.notes");
        ctx.refresh();
        ActorSystem system = ctx.getBean(ActorSystem.class);
        ActorRef ref = system.actorOf(SpringExtProvider.getInstance().get(system).createProps("actorDemo"), "actorDemo");
        ref.tell("hello", ActorRef.noSender());
    }
}


@Configuration
class AppConfig {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 创建ActorSystem并纳入bean中管理,初始化SpringExt中的 ApplicationContext
     *
     * @return
     */
    @Bean
    public ActorSystem createActorSystem() {
        ActorSystem system = ActorSystem.create("sys");
        SpringExtProvider.getInstance().get(system).initApplicationContext(applicationContext);
        return system;
    }
}

/**
 * Spring扩展器 加载到bean容器周末好
 */
class SpringExtProvider extends AbstractExtensionId<SpringExt> {
    private static SpringExtProvider provider = new SpringExtProvider();

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static SpringExtProvider getInstance() {
        return provider;
    }
}

/**
 * Akka集成Spring的扩展器
 */
class SpringExt implements Extension {
    private ApplicationContext applicationContext;

    public void initApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props createProps(String actorBeanName) {
        return Props.create(SpringDI.class, this.applicationContext, actorBeanName);
    }
}

/**
 * 依赖注入
 */
class SpringDI implements IndirectActorProducer {
    private ApplicationContext applicationContext;

    private String beanName;

    /**
     * 返回class对象
     *
     * @return
     */
    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(beanName);
    }

    /**
     * 返回Actor实例
     *
     * @return
     */
    @Override
    public Actor produce() {
        return (Actor) applicationContext.getBean(beanName);
    }
}
