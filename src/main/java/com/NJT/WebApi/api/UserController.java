package com.NJT.WebApi.api;

import com.NJT.WebApi.model.auth.ChangePasswordBody;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllByOdobren(@RequestParam String odobren) {
        List<User> users = userService.getAllByOdobren(odobren);

        return ResponseEntity.ok(users);
    }

    @PostMapping("/accept")
    public ResponseEntity acceptUserRegistration(@RequestBody User user) {
        return userService.acceptUserRegistration(user) ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/deny/{id}")
    public ResponseEntity denyUserRegistration(@PathVariable Long id) {
        return userService.denyUserRegistration(id) ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody ChangePasswordBody changePassword) {
        return userService.changePassword(changePassword) ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }

}
