package resource;

import java.util.List;
import java.util.Optional;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.core.api.event.CoreEvent;

import model.Moto;
import repository.MotoRepository;

public class MotoResource {

    private MotoRepository motoRepository;

    public MotoResource(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
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
                Moto moto = (Moto) message.getPayload().getValue();
                return create(moto);
            case "PUT":
                if (id != null) {
                    Moto updatedMoto = (Moto) message.getPayload().getValue();
                    return update(id, updatedMoto);
                }
                break;
            case "DELETE":
                if (id != null) {
                    delete(id);
                    return "Moto deleted successfully";
                }
                break;
            default:
                return null;
        }
        return null;
    }

    public List<Moto> getAll() {
        return motoRepository.findAll();
    }

    public Moto findById(Long id) {
        return motoRepository.findById(id);
    }


    public Moto create(Moto moto) {
        return motoRepository.save(moto);
    }

    public Moto update(Long id, Moto updatedMoto) {
        Moto existingMoto = findById(id);
        if (existingMoto != null) {
            updatedMoto.setId(id);
            return motoRepository.save(updatedMoto);
        }
        return null;
    }

    public void delete(Long id) {
        motoRepository.deleteById(id);
    }
}
