package com.agenda.model;

import com.agenda.Handler.DbException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DAO {

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static Statement statement;
    private static ResultSet resultSet;

    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public static Statement getStatement() {
        return statement;
    }

    public static ResultSet getResultSet() {
        return resultSet;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closePreparedStatement() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeResultSet() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void insert(JavaBeans contact){
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (name, phone, email) VALUES (?, ?, ?)")) {

            ps.setString(1, contact.getNome());
            ps.setString(2, contact.getTelefone());
            ps.setString(3, contact.getEmail());

            System.out.println("Inserindo dados no banco de dados...");
            ps.executeUpdate();
            System.out.println("Dados inseridos com sucesso!");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            closeConnection();
            closePreparedStatement();
        }
    }
    public static void update(JavaBeans contact){
        try(Connection con = DAO.getConnection()){
            preparedStatement  = con.prepareStatement("UPDATE contact SET name = ?, phone = ?, email = ? WHERE id = ?");
            preparedStatement.setString(1, contact.getNome());
            preparedStatement.setString(2, contact.getTelefone());
            preparedStatement.setString(3, contact.getEmail());
            preparedStatement.setLong(4, contact.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            closeConnection();
            closePreparedStatement();
        }
    }

    public static ArrayList<JavaBeans> PrintAllContacts() {
        ArrayList<JavaBeans> list = new ArrayList<>();
        try {
             connection = getConnection();
             preparedStatement = connection.prepareStatement("SELECT * FROM contact ORDER BY id");
             resultSet= preparedStatement.executeQuery() ;

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");

                list.add(new JavaBeans(id, name, phone, email));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contatos: " + e.getMessage(), e);
        }
        finally {
            closeConnection();
            closePreparedStatement();
            closeResultSet();
        }

        return list;
    }

    public static void deletarContatos(Long id)  {
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("delete from contact where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar contato: " + e.getMessage(), e);
        }
        finally {
            closeConnection();
            closePreparedStatement();
        }
    }
    public static JavaBeans selecionarContatoPorId(Long id) {
        JavaBeans contato = new JavaBeans();
        try {
            connection = getConnection();
            preparedStatement  = connection.prepareStatement("SELECT * FROM contact WHERE id = ?");
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    contato.setId(rs.getLong("id"));
                    contato.setNome(rs.getString("name"));
                    contato.setTelefone(rs.getString("phone"));
                    contato.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao selecionar contato: " + e.getMessage(), e);
        }
        finally {
            closeConnection();
            closePreparedStatement();
        }

        return contato;
    }



    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties props = loadProperties();
                String url = props.getProperty("url");
                String user = props.getProperty("user");
                String password = props.getProperty("password");

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, password);
            }
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new DbException("Erro ao obter conexão: " + e.getMessage());
        }
    }


    private static Properties loadProperties() {
        try (InputStream input = DAO.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new DbException("Arquivo db.properties não encontrado no classpath");
            }
            var props = new Properties();
            props.load(input);
            return props;
        }

        catch (IOException e) {
            throw new DbException("Erro ao carregar propriedades do banco de dados: " + e.getMessage());
        }
    }

}