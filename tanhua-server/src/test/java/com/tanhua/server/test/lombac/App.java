package com.tanhua.server.test.lombac;

import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Period;

@SpringBootTest
@RunWith(SpringRunner.class)
public class App {

    @Test
    public void test1(){
        Person person = new Person();
        person.setId(1);
        person.setName("jack");
        System.out.println("pp==:"+person);
    }

    @Test
    public void test2(){
        Person person = Person.builder().id(2).name("lishi").build();
        System.out.println("p=:"+person);
    }
}
