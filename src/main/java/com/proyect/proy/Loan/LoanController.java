package com.proyect.proy.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Endpoint para prestar un libro
    @PostMapping("/borrow/{userId}/{bookId}")
    public Loan borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return loanService.borrowBook(userId, bookId);
    }

    // Endpoint para devolver un libro
    @PostMapping("/return/{userId}/{bookId}")
    public Loan returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return loanService.returnBook(userId, bookId);
    }

}
