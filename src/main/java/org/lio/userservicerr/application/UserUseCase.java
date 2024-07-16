package org.lio.userservicerr.application;

import org.lio.userservicerr.infrastructure.UserModel;
import org.lio.userservicerr.infrastructure.inputport.UserInputPort;
import org.lio.userservicerr.infrastructure.outputport.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUseCase implements UserInputPort {
    @Autowired
    EntityRepository entityRepository;

    @Override
    public <T> UserModel updateUser(T user, String uuid) {
        return entityRepository.updateUser(user, uuid);
    }

    @Override
    public <T> Boolean deleteUserByUuid(T uuid) {
        return entityRepository.deleteUser(uuid);
    }
}
