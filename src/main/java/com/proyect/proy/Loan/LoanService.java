package com.proyect.proy.Loan;

import com.proyect.proy.Book.Book;
import com.proyect.proy.Book.BookRepository;
import com.proyect.proy.Book.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Optional;
@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    // Solicitar préstamo de un libro
    public Loan borrowBook(Long userId, Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (book.getStatus() == Status.AVAILABLE) {
                // Cambiar el estado del libro a prestado
                book.setStatus(Status.NOT_AVAILABLE);
                bookRepository.save(book);

                // Registrar el préstamo
                Loan loan = new Loan(userId, bookId, LocalDateTime.now(), null, Action.LOAN);
                loanRepository.save(loan);

                return loan;
            } else {
                throw new RuntimeException("El libro '"+book.getTitle()+"no esta disponible");
            }
        } else {
            throw new RuntimeException("El libro '"+bookId+"no existe");
        }
    }


    // Devolver un libro
    public Loan returnBook(Long userId, Long bookId) {
        // Buscar el préstamo activo (sin devolución) para este libro
        Optional<Loan> optionalLoan = loanRepository.findByBookIdAndReturned(bookId, Action.LOAN);

        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();

            // Verificar que el préstamo corresponde al usuario que está devolviendo el libro
            if (!loan.getUserId().equals(userId)) {
                throw new RuntimeException("Este libro no fue prestado por el usuario con ID: " + userId);
            }

            // Actualizar el estado del libro a 'AVAILABLE'
            Optional<Book> optionalBook = bookRepository.findById(bookId);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                book.setStatus(Status.AVAILABLE);
                bookRepository.save(book);
            } else {
                throw new RuntimeException("El libro con ID " + bookId + " no existe.");
            }

            // Actualizar la información del préstamo para reflejar la devolución
            loan.setReturnDate(LocalDateTime.now());
            loan.setReturned(Action.RETURNED);
            loanRepository.save(loan);

            return loan;
        } else {
            throw new RuntimeException("No se encontró un préstamo activo para el libro con ID: " + bookId);
        }
    }

}
