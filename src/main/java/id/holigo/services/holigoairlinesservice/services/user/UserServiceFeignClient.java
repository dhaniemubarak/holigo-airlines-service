package id.holigo.services.holigoairlinesservice.services.user;

import id.holigo.services.common.model.UserDtoForUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "holigo-user-service")
public interface UserServiceFeignClient {

    String USER_PATH = "/api/v1/users/{id}";

    @RequestMapping(method = RequestMethod.GET, value = USER_PATH)
    ResponseEntity<UserDtoForUser> getUser(@PathVariable Long id);
}
