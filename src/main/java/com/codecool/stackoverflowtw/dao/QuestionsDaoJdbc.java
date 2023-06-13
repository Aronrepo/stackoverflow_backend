package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.controller.dto.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.QuestionDTO;
import com.codecool.stackoverflowtw.dao.model.Question;
import com.codecool.stackoverflowtw.database.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionsDaoJdbc implements QuestionsDAO {
    private final Database database;

    public QuestionsDaoJdbc(Database database) {
        this.database = database;
    }

    @Override
    public Optional<QuestionDTO> findOneQuestionById(int id) {
        String template = "SELECT * FROM questions WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<QuestionDTO> question = Optional.empty();
            if (resultSet.next()) {
                question = Optional.of(toEntity(resultSet));
            }
            resultSet.close();
            return question;
        } catch (SQLException e) {
            System.err.println(e);
            return Optional.empty();
        }
    }

    @Override
    public List<QuestionDTO> findAllQuestions() {
        String query = "SELECT * FROM questions;";
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<QuestionDTO> questions = new ArrayList<>();
            while (resultSet.next()) {
                QuestionDTO question = toEntity(resultSet);
                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private QuestionDTO toEntity(ResultSet resultSet) throws SQLException {
        return new QuestionDTO(
                resultSet.getInt("question_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getLocalDateTime("created") // TODO change in the record to Date?
        );
    }

    @Override
    public void save(NewQuestionDTO question) {
        /*if (findOneQuestionById(question.id()).isPresent()) {
            return;
        }*/ //TODO write a new method to check the title?

        String template = "INSERT INTO questions(id,title,description,created)\n" +
                "VALUES (?,?,?,?);";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            prepare(question, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepare(NewQuestionDTO question, PreparedStatement statement) throws SQLException {
        statement.setInt(1, 1); //TODO add changing id
        statement.setString(1, question.title());
        statement.setString(1, "some description");
        statement.setDate(LocalDateTime.now()); //TODO make compatible Date and LocalDateTime
    }

    @Override
    public void sayHi() {
        System.out.println("Hi DAO!");
    }
}
