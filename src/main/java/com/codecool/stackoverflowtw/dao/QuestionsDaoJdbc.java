package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.dao.model.Question;
import com.codecool.stackoverflowtw.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionsDaoJdbc implements QuestionsDAO {
    private final Database database;

    public QuestionsDaoJdbc(Database database) {
        this.database = database;
    }

    public Optional<Question> findOneQuestion(String questionToCheck) {
        String template = "SELECT * FROM questions WHERE question = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setString(1, questionToCheck);
            ResultSet resultSet = statement.executeQuery();
            Optional<Question> question = Optional.empty();
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

    public List<Question> findAllQuestions() {
        String query = "SELECT * FROM questions;";
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<Question> questions = new ArrayList<>();
            while (resultSet.next()) {
                Question question = toEntity(resultSet);
                questions.add(question);
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Question toEntity(ResultSet resultSet) throws SQLException {
        return new Question(
                resultSet.getInt("id"),
                resultSet.getString("question"),
                resultSet.getInt("user_id")
        );
    }

    public void save(Question question) {
        if (findOneQuestion(question.question()).isPresent()) {
            return;
        }

        String template = "INSERT INTO identification(question, user_id)\n" +
                "VALUES (?,?);";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            prepare(question, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepare(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.question());
        statement.setInt(2, question.user_id());
    }
    @Override
    public void sayHi() {
        System.out.println("Hi DAO!");
    }
}
