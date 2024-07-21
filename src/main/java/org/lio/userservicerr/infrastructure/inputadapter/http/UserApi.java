package org.lio.userservicerr.infrastructure.inputadapter.http;

import org.lio.userservicerr.domain.User;
import org.lio.userservicerr.infrastructure.UserModel;
import org.lio.userservicerr.infrastructure.inputport.UserInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserApi {
    @Autowired
    UserInputPort userInputPort;

    @PutMapping(value = "/{uuid}")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody User user, @PathVariable String uuid) {
        UserModel updatedUser = userInputPort.updateUser(user, uuid);

        if (updatedUser == null) {
            BaseResponse response = BaseResponse.builder()
                    .data(Collections.EMPTY_LIST)
                    .message("User not found")
                    .build();

            return ResponseEntity.status(404).body(response);
        }

        BaseResponse response = BaseResponse.builder()
                .data(updatedUser)
                .message("User updated successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<BaseResponse> deleteUserByUuid(@PathVariable String uuid) {
        Boolean result = userInputPort.deleteUserByUuid(uuid);

        if (!result) {
            BaseResponse response = BaseResponse.builder()
                    .data(Collections.EMPTY_LIST)
                    .message("User not found")
                    .build();
            return ResponseEntity.status(404).body(response);
        }

        BaseResponse response = BaseResponse.builder()
                .data(Collections.EMPTY_LIST)
                .message("User deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<BaseResponse> getUserByUuid(@PathVariable String uuid) {
        UserModel user = userInputPort.getUserByUuid(uuid);

        if (user == null) {
            BaseResponse response = BaseResponse.builder()
                    .data(Collections.EMPTY_LIST)
                    .message("User not found")
                    .build();
            return ResponseEntity.status(404).body(response);
        }

        BaseResponse response = BaseResponse.builder()
                .data(user)
                .message("User retrieved successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
