package repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Carro;
import repository.CarroRepository;

public class CarroRepositoryJdbcImpl implements CarroRepository {
    
    private final String url = "jdbc:postgresql://your_database_url";
    private final String user = "your_username";
    private final String password = "your_password";

    @Override
    public Carro save(Carro carro) {
        String sql = "INSERT INTO carros (marca, modelo) VALUES (?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carro.getMarca());
            stmt.setString(2, carro.getModelo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                carro.setId(rs.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carro;
    }

    @Override
    public Carro findById(Long id) {
        String sql = "SELECT * FROM carros WHERE id = ?";
        Carro carro = null;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                carro = new Carro();
                carro.setId(rs.getLong("id"));
                carro.setMarca(rs.getString("marca"));
                carro.setModelo(rs.getString("modelo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carro;
    }

    @Override
    public List<Carro> findAll() {
        List<Carro> carros = new ArrayList<>();
        String sql = "SELECT * FROM carros";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Carro carro = new Carro();
                carro.setId(rs.getLong("id"));
                carro.setMarca(rs.getString("marca"));
                carro.setModelo(rs.getString("modelo"));
                carros.add(carro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carros;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM carros WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

