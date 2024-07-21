package org.lio.userservicerr.infrastructure.outputport;

import org.lio.userservicerr.infrastructure.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository {
    public <T> UserModel updateUser(T reg, String uuid);
    public <T> Boolean deleteUser(T reg);
    public <T> UserModel getUserByUuid(T reg);
}
