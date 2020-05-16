package com.javawro27.jdbc;
import com.javawro27.jdbc.model.Student;

//Student DAO reprezentuje obiekt DATA ACCESS OBJECT - obiekt dostępu do danych
//narzędzie do zarządzania obiektami w bazie danych

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
        private MysqlConnection mysqlConnection;

    public StudentDao(){
        this.mysqlConnection = new MysqlConnection();
    }

    public void addToDatabase(Student student){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = mysqlConnection.getConnection();

            statement = connection.prepareStatement(StudentTableQueries.INSERT_STUDENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,student.getFirstName());
            statement.setString(2,student.getLastName());
            statement.setDouble(3,student.getHeight());
            statement.setInt(4,student.getAge());
            statement.setBoolean(5,student.isAlive());

            int affectedRecords = statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next()){ // jeśli jest rekord
                Long generatedKey = generatedKeys.getLong(1);
                student.setId(generatedKey);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
            if (connection != null){
                if (statement != null) {
                    statement.close();
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Błąd zamknięcia połączenia");
        }
            }
        }

        public List<Student> getAllStudents () {
        List<Student> list = new ArrayList<>();

            try(Connection connection = mysqlConnection.getConnection()){

                try(PreparedStatement statement = connection.prepareStatement(StudentTableQueries.SELECT_ALL_STUDENTS_QUERY)){
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()){
                        Student newStudent = Student.builder()
                                .id(resultSet.getLong(1))
                                .firstName(resultSet.getString(2))
                                .lastName(resultSet.getString(3))
                                .height(resultSet.getDouble(4))
                                .age(resultSet.getInt(5))
                                .alive(resultSet.getBoolean(6))
                                .build();

                        list.add(newStudent);

                    }
                }


            } catch (SQLException e){
                e.printStackTrace();
            }
            return  list;
        }

    public void updateStudent(Student student) {
        if (student.getId() == null) {
            System.err.println("Can't edit student without id.");
            return;
        }
        try (Connection connection = mysqlConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(StudentTableQueries.UPDATE_STUDENT_QUERY)) {
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.setDouble(3, student.getHeight());
                statement.setInt(4, student.getAge());
                statement.setBoolean(5, student.isAlive());

                // uzupełnienie klauzuli where
                statement.setLong(6, student.getId());

                int affectedRecords = statement.executeUpdate();
                System.out.println("Edytowanych rekordów: " + affectedRecords);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteStudent(Student student){
        if (student.getId() == null) {
            System.err.println("Can't edit student without id.");
            return;
        }
        deleteStudent(student.getId());
    }

    public void deleteStudent(Long studentId) {
        try (Connection connection = mysqlConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(StudentTableQueries.DELETE_STUDENT_QUERY)) {
                // uzupełnienie klauzuli where
                statement.setLong(1, studentId);

                int affectedRecords = statement.executeUpdate();
                System.out.println("Edytowanych rekordów: " + affectedRecords);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}