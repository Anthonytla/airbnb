package com.quest.etna;

import com.quest.etna.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.quest.etna.repositories")
public class QuestWebJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestWebJavaApplication.class, args);
    }

}
