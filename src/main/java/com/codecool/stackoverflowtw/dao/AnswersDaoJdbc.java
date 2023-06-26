package com.codecool.stackoverflowtw.dao;

import com.codecool.stackoverflowtw.controller.dto.AnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.NewAnswerDTO;
import com.codecool.stackoverflowtw.controller.dto.NewQuestionDTO;
import com.codecool.stackoverflowtw.controller.dto.QuestionDTO;
import com.codecool.stackoverflowtw.database.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswersDaoJdbc implements AnswersDAO {
    private final Database database;

    public AnswersDaoJdbc(Database database) {
        this.database = database;
    }

    @Override
    public List<AnswerDTO> findAllAnswersForQuestion(int id) {
        String template = "SELECT * FROM answers " +
                "WHERE question_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<AnswerDTO> answers = new ArrayList<>();
            while (resultSet.next()) {
                AnswerDTO answer = toEntity(resultSet);
                answers.add(answer);
            }
            resultSet.close();
            return answers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AnswerDTO> findOneAnswerById(int id) {
        String template = "SELECT * FROM answers WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Optional<AnswerDTO> answer = Optional.empty();
            if (resultSet.next()) {
                answer = Optional.of(toEntity(resultSet));
            }
            resultSet.close();
            return answer;
        } catch (SQLException e) {
            System.err.println(e);
            return Optional.empty();
        }
    }

    public Integer findIdOfAnswerByTitle(String answer) {
        String template = "SELECT * FROM answers WHERE answer = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            statement.setString(1, answer);
            ResultSet resultSet = statement.executeQuery();
            int id = 0;
            if (resultSet.next()) {
                AnswerDTO answerDTO = toEntity(resultSet);
                id = answerDTO.question_id();
            }
            resultSet.close();
            return id;
        } catch (SQLException e) {
            System.err.println(e);
            return 0;
        }
    }

    private AnswerDTO toEntity(ResultSet resultSet) throws SQLException {
        return new AnswerDTO(
                resultSet.getInt("answer_id"),
                resultSet.getString("answer"),
                resultSet.getInt("question_id"),
                resultSet.getDate("created")
        );
    }

    @Override
    public void save(NewAnswerDTO answer) {
        String template = "INSERT INTO answers(answer,question_id,created)\n" +
                "VALUES (?,?,?);";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(template)) {
            prepare(answer, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        if (!findOneAnswerById(id).isPresent()) {
            return false;
        }
        String deleteQuery = "DELETE FROM answers WHERE answer_id = ?";
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

    private void prepare(NewAnswerDTO answer, PreparedStatement statement) throws SQLException {
        statement.setString(1, answer.answer());
        statement.setString(2, "some description");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        statement.setDate(3, new java.sql.Date(timestamp.getTime()));
    }

}
