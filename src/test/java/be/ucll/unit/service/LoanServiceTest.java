package be.ucll.unit.service;

import be.ucll.model.Loan;
import be.ucll.repository.LoanRepository;
import be.ucll.service.LoanService;
import be.ucll.unit.repository.UserRepositoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {

    private LoanService loanService;
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        loanRepository = new LoanRepository();
        loanService = new LoanService(loanRepository, new UserRepositoryStub());
    }

    @Test
    public void givenRepoWithLoansForUser_whenSearchingForLoans_thenLoanIsReturned() {
        List<Loan> result = loanService.getLoansByUser("jane.toe@ucll.be", false);

        assertEquals(2, result.size());
        assertTrue(result.contains(loanRepository.loans.get(1)));
        assertTrue(result.contains(loanRepository.loans.get(2)));
    }

    @Test
    public void givenUserAndOnlyActive_whenGetLoansByUser_thenOnlyActiveLoansByUserAreReturned() {
        List<Loan> result = loanService.getLoansByUser("jane.toe@ucll.be", true);

        assertEquals(1, result.size());
    }

    @Test
    public void givenUserAndNoActiveLoans_whenGetLoansByUser_thenLoansByUserAreReturned() {
        List<Loan> result = loanService.getLoansByUser("john.doe@ucll.be", true);

        assertEquals(0, result.size());
    }

    @Test
    public void givenNonExistingUser_whenGetLoansByUser_thenAnExceptionIsThrown() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> loanService.getLoansByUser("NonExistingUser", false));

        assertEquals("User not found.", ex.getMessage());
    }

    @Test
    public void givenUserWithOnlyInactiveLoans_whenDeleteLoansByUser_thenLoansAreDeleted() {
        String email = "john.doe@ucll.be";

        // precondition: user has loans
        assertTrue(loanService.getLoansByUser(email, false).size() > 0);

        // action
        loanService.deleteLoansByUser(email);

        // after deletion there should be no loans for that user
        List<Loan> remaining = loanService.getLoansByUser(email, false);
        assertEquals(0, remaining.size());
    }

    @Test
    public void givenUserWithActiveLoans_whenDeleteLoansByUser_thenThrowsActiveLoans() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> loanService.deleteLoansByUser("jane.toe@ucll.be"));

        assertEquals("User has active loans.", ex.getMessage());
    }

    @Test
    public void givenUserWithNoLoans_whenDeleteLoansByUser_thenThrowsUserHasNoLoans() {
        String email = "john.doe@ucll.be";

        loanRepository.loans.removeIf(loan -> loan.getUser().getEmail().equals(email));

        Exception ex = assertThrows(RuntimeException.class,
                () -> loanService.deleteLoansByUser(email));

        assertEquals("User has no loans.", ex.getMessage());
    }

    @Test
    public void givenNonExistingUser_whenDeleteLoansByUser_thenThrowsUserNotFound() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> loanService.deleteLoansByUser("NonExistingUser"));

        assertEquals("User not found.", ex.getMessage());
    }
}
