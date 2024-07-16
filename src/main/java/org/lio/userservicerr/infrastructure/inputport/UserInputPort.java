package org.lio.userservicerr.infrastructure.inputport;

import org.lio.userservicerr.infrastructure.UserModel;

public interface UserInputPort {
    public <T> UserModel updateUser(T user, String uuid);
    public <T> Boolean deleteUserByUuid(T uuid);
}
