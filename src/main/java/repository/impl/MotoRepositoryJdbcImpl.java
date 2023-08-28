package repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Moto;
import repository.MotoRepository;

public class MotoRepositoryJdbcImpl implements MotoRepository {
    
    private final String url = "jdbc:postgresql://your_database_url";
    private final String user = "your_username";
    private final String password = "your_password";

    @Override
    public Moto save(Moto moto) {
        String sql = "INSERT INTO motos (marca, modelo) VALUES (?, ?) RETURNING id";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moto.getMarca());
            stmt.setString(2, moto.getModelo());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                moto.setId(rs.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moto;
    }

    @Override
    public Moto findById(Long id) {
        String sql = "SELECT * FROM motos WHERE id = ?";
        Moto moto = null;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                moto = new Moto();
                moto.setId(rs.getLong("id"));
                moto.setMarca(rs.getString("marca"));
                moto.setModelo(rs.getString("modelo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moto;
    }

    @Override
    public List<Moto> findAll() {
        List<Moto> motos = new ArrayList<>();
        String sql = "SELECT * FROM motos";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Moto moto = new Moto();
                moto.setId(rs.getLong("id"));
                moto.setMarca(rs.getString("marca"));
                moto.setModelo(rs.getString("modelo"));
                motos.add(moto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return motos;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM motos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


