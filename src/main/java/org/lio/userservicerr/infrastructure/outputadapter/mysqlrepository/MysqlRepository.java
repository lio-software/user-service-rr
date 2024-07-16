package org.lio.userservicerr.infrastructure.outputadapter.mysqlrepository;

import org.lio.userservicerr.domain.User;
import org.lio.userservicerr.infrastructure.UserModel;
import org.lio.userservicerr.infrastructure.UserRepository;
import org.lio.userservicerr.infrastructure.outputport.EntityRepository;
import org.lio.userservicerr.infrastructure.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MysqlRepository implements EntityRepository {
    @Autowired
    private UserRepository userRepository;

    private final EncryptionUtils encryptionUtils = new EncryptionUtils();

    @Override
    public <T> UserModel updateUser(T reg, String uuid) {
        UserModel user = userRepository.findByUuid(uuid);

        if (user == null) {
            return null;
        }

        String password = user.getPassword();
        String posiblePassword = ((User) reg).getPassword();

        try {
            Boolean matches = encryptionUtils.matches(posiblePassword, password);

            if (!matches) {
                return null;
            }

            user.setFirstName(((User) reg).getFirstName());
            user.setLastName(((User) reg).getLastName());
            user.setEmail(((User) reg).getEmail());
            user.setPassword(encryptionUtils.encrypt(posiblePassword));
            user.setPhoneNumber(((User) reg).getPhoneNumber());
            user.setImageUrl(((User) reg).getImageUrl());
        } catch (Exception e) {
            return null;
        }

        return userRepository.save(user);
    }

    @Override
    public <T> Boolean deleteUser(T reg) {
        UserModel user = userRepository.findByUuid((String) reg);

        if (user == null) {
            return Boolean.FALSE;
        }
        userRepository.deleteById(user.getId());
        return Boolean.TRUE;
    }
}
