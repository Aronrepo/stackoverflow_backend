package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.controller.dto.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.QuestionDTO;
import com.codecool.stackoverflowtw.dao.model.Question;
import com.codecool.stackoverflowtw.database.Database;

import java.sql.*;
import java.time.LocalDate;
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
    public QuestionDTO getQuestionById(int id) {
        String template = "SELECT * FROM questions WHERE question_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            QuestionDTO question = null;
            if (resultSet.next()) {
                question = toEntity(resultSet);
            }
            //System.out.println(question);
            return question;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer findIdOfQuestionByTitle(String title) {
        String template = "SELECT * FROM questions WHERE title = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            int id = 0;
            if (resultSet.next()) {
                QuestionDTO question = toEntity(resultSet);
                id = question.question_id();
            }
            resultSet.close();
            return id;
        } catch (SQLException e) {
            System.err.println(e);
            return 0;
        }
    }

    @Override
    public List<QuestionDTO> findAllQuestions() {
        String query = "select q.question_id, q.title, q.description, q.created,\n" +
                "case when\n" +
                "a.count_answers is not null then a.count_answers\n" +
                "else 0\n" +
                "end count_answers\n" +
                "from questions q\n" +
                "left join\n" +
                "(\n" +
                "select a.question_id, count(a.answer_id) as count_answers from answers a\n" +
                "group by question_id) as a\n" +
                "using (question_id)";
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
                resultSet.getDate("created"),
                resultSet.getInt("count_answers")
        );
    }

    @Override
    public void save(NewQuestionDTO question) {
        String template = "INSERT INTO questions(title,description,created)\n" +
                "VALUES (?,?,?);";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            prepare(question, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepare(NewQuestionDTO question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.title());
        statement.setString(2, question.description());
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        statement.setDate(3, new java.sql.Date(timestamp.getTime()));
    }

    @Override
    public boolean delete(int id) {
        if (!findOneQuestionById(id).isPresent()){
            return false;
        }

        String deleteQuery = "DELETE FROM questions WHERE question_id = ?";
        PreparedStatement statement = null;

        try {
            Connection connection = database.getConnection();
            statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sayHi() {
        System.out.println("Hi DAO!");
    }
}
