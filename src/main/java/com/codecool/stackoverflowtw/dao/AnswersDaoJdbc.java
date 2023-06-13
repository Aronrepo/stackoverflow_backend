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

public class AnswersDaoJdbc implements AnswersDAO {
    private final Database database;

    public AnswersDaoJdbc(Database database) {
        this.database = database;
    }

    @Override
    public List<AnswerDTO> findAllAnswers() {
        String query = "SELECT * FROM answers;";
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<AnswerDTO> answers = new ArrayList<>();
            while (resultSet.next()) {
                AnswerDTO answer = toEntity(resultSet);
                answers.add(answer);
            }
            return answers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    private void prepare(NewAnswerDTO answer, PreparedStatement statement) throws SQLException {
        statement.setString(1, answer.answer());
        statement.setString(2, "some description");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        statement.setDate(3, new java.sql.Date(timestamp.getTime()));
    }

}
