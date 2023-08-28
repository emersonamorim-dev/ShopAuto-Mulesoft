package repository;

import java.util.List;
import java.util.Optional;

import model.Moto;

public interface MotoRepository {
    Moto save(Moto moto);
    Moto findById(Long id);
    List<Moto> findAll();
    void deleteById(Long id);
}

