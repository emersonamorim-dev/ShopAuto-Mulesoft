package repository;

import java.util.List;

import model.Carro;

public interface CarroRepository {
    Carro save(Carro carro);
    Carro findById(Long id);
    List<Carro> findAll();
    void deleteById(Long id);
}
