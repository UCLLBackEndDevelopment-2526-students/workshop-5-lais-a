package be.ucll.controller;

import be.ucll.model.Loan;
import be.ucll.model.User;
import be.ucll.service.LoanService;
import be.ucll.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private UserService userService;
    private LoanService loanService;

    @Autowired
    public UserRestController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) String name) {
        return userService.getUsersByName(name);
    }

    @GetMapping("/adults")
    public List<User> getAdults() {
        return userService.getAllAdultUsers();
    }

    @GetMapping("age/{min}/{max}")
    public List<User> getUsersBetweenAge(@PathVariable int min, @PathVariable int max) {
        return userService.getUsersBetweenAge(min, max);
    }

    @GetMapping("/interest/{interest}")
    public List<User> getUsersByInterest(@PathVariable String interest) {
        return userService.getUsersByInterest(interest);
    }

    @GetMapping("/interest/{interest}/{age}")
    public List<User> getUsersByInterestAndOlderThan(@PathVariable String interest, @PathVariable int age) {
        return userService.getUsersByInterestAndOlderThan(interest, age);
    }

    @GetMapping("/{email}/loans")
    public List<Loan> getLoansByUser(@PathVariable("email") String email,
                                     @RequestParam(value = "onlyActive", required = false) Boolean onlyActive) {

        return loanService.getLoansByUser(email, onlyActive);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{email}")
    public User updateUser(@PathVariable("email") String email, @Valid @RequestBody User user) {
        return userService.updateUser(email, user);
    }

    @DeleteMapping("/{email}/loans")
    public String deleteLoansByUser(@PathVariable("email") String email) {
        loanService.deleteLoansByUser(email);
        return "Loans of user successfully deleted.";
    }

    @DeleteMapping("/{email}")
    public String deleteUser(@PathVariable("email") String email) {
        userService.deleteUser(email);
        return "User successfully deleted.";
    }

    @GetMapping("/oldest")
    public User getOldestUser() {
        return userService.getOldestUser();
    }

    @GetMapping("/age/{age}/name/{name}")
    public List<User> getAllUsersOlderThanAndNameContaining(@PathVariable int age, @PathVariable String name) {
        return userService.getAllUsersOlderThanAndNameContaining(age, name);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class})
    public Map<String, String> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMostSpecificCause().getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1);
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }
}
