package com.codecool.stackoverflowtw;

import com.codecool.stackoverflowtw.dao.QuestionsDAO;
import com.codecool.stackoverflowtw.dao.QuestionsDaoJdbc;
import com.codecool.stackoverflowtw.database.Database;
import com.codecool.stackoverflowtw.initialize.TableInitializer;
import com.codecool.stackoverflowtw.initialize.TableStatements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class StackoverflowTwApplication {

    public static void main(String[] args) {

        SpringApplication.run(StackoverflowTwApplication.class, args);

        Database database = new Database(
                System.getenv("database"),
                System.getenv("dbuser"),
                System.getenv("password"));
        Map<String, String> tables = Map.of(
                "questions", TableStatements.QUESTIONS,
                "answers", TableStatements.ANSWERS
        );
        TableInitializer tableInitializer = new TableInitializer(database, tables);
        tableInitializer.initialize();
    }

    @Bean
    public QuestionsDAO questionsDAO() {
        Database database = new Database(
                "jdbc:postgresql://localhost:5432/bruteforce_plus_plus",
                "postgres",
                "Jelszó");
        return new QuestionsDaoJdbc(database);
    }
}
