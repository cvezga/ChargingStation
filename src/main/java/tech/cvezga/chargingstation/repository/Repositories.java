package tech.cvezga.chargingstation.repository;

import org.springframework.stereotype.Service;
import tech.cvezga.chargingstation.entity.User;
import tech.cvezga.chargingstation.entity.Villa;

@Service
public class Repositories {

    private final VillaRepository villaRepository;
    private final UserRepository userRepository;

    public Repositories(VillaRepository villaRepository, UserRepository userRepository) {
        this.villaRepository = villaRepository;
        this.userRepository = userRepository;
    }

    public <T> T getEntity(long id, Class<T> type) {

        if (type == Villa.class) return (T) villaRepository.findById(id).get();

        return null;

    }

    public void save(Object o){
        if (o instanceof Villa villa) {
            villaRepository.save(villa);
        }else if (o instanceof User  user){
            userRepository.save(user);
        }

    }
}
