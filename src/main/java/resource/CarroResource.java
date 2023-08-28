package resource;

import java.util.List;
import java.util.Optional;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.core.api.event.CoreEvent;

import model.Carro;
import repository.CarroRepository;

public class CarroResource {

    private CarroRepository carroRepository;

    public CarroResource(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    public Object processRequest(CoreEvent event) {
        Message message = event.getMessage();
        
        String httpMethod = (String) event.getVariables().get("http.method").getValue();
        String path = (String) event.getVariables().get("http.request.path").getValue();
        
        String[] parts = path.split("/");
        Long id = parts.length > 1 ? Long.parseLong(parts[1]) : null;

        switch (httpMethod) {
            case "GET":
                if (id == null) {
                    return getAll();
                } else {
                    return findById(id);
                }
            case "POST":
                Carro carro = (Carro) message.getPayload().getValue();
                return create(carro);
            case "PUT":
                if (id != null) {
                    Carro updatedCarro = (Carro) message.getPayload().getValue();
                    return update(id, updatedCarro);
                }
                break;
            case "DELETE":
                if (id != null) {
                    delete(id);
                    return "Carro deleted successfully";
                }
                break;
            default:
                return null;
        }
        return null;
    }

    public List<Carro> getAll() {
        return carroRepository.findAll();
    }

    public Carro findById(Long id) {
        Optional<Carro> carro = Optional.of(carroRepository.findById(id));
        return carro.orElse(null);
    }

    public Carro create(Carro carro) {
        return carroRepository.save(carro);
    }

    public Carro update(Long id, Carro updatedCarro) {
        Optional<Carro> existingCarro = Optional.of(carroRepository.findById(id));
        if (existingCarro.isPresent()) {
            updatedCarro.setId(id);
            return carroRepository.save(updatedCarro);
        }
        return null;
    }

    public void delete(Long id) {
        carroRepository.deleteById(id);
    }
}

