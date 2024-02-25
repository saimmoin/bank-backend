package com.telusko.bankingapplication.controllers;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.telusko.bankingapplication.bankingObjects.Account;
import com.telusko.bankingapplication.bankingObjects.Amount;
import com.telusko.bankingapplication.bankingObjects.Loan;
import com.telusko.bankingapplication.bankingObjects.User;
import com.telusko.bankingapplication.exceptions.ResourceNotFoundException;
import com.telusko.bankingapplication.repositories.AccountRepository;
import com.telusko.bankingapplication.repositories.LoanRepository;
import com.telusko.bankingapplication.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans;
    }

    @RequestMapping("/addusers")
    public String createUser(@RequestParam("name") String name, @RequestParam("status") String status, @RequestParam("cnic") String cnic) {
        if (userRepository.existsByCnic(cnic)) {
            return "CNIC already exists in the database!";
        } else {
            User user = new User();
            user.setName(name);
            user.setStatus(status);
            user.setCnic(cnic);
            userRepository.save(user);
            return "User added successfully!";
        }
    }

    @RequestMapping("/addaccounts")
    public String createAccount(@RequestParam("userId") long userId, @RequestParam("balance") double balance) {
        List<Account> accounts = accountRepository.findAll();
        List<Account> user_accounts = accounts.stream().filter(a -> a.getUserId() == userId).toList();
        if (user_accounts.size() > 3) {
            return "More than 3 accounts found!";
        } else {
            if (userRepository.existsById(userId)) {
                Account account = new Account();
                account.setBalance(balance);
                account.setUserId(userId);
                account.setAccountNumber(generate10DigitRandomNumber());
                accountRepository.save(account);
                return "Account created successfully!";
            } else {
                System.out.println(userId);
                return "No UserId found in database!";
            }

        }
    }

    public long generate10DigitRandomNumber() {
        Random random = new Random();
        long randomNumber = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return randomNumber;
    }

    @RequestMapping("/addloan")
    public String getLoan(@RequestParam("userId") long userId, @RequestParam("sanctionAmount") Double sanctionAmount) {
        double totalBalance = 0;
        List<Account> accounts = accountRepository.findAll();
        List<Account> userAccounts = accounts.stream().filter(a -> a.getUserId() == userId).toList();

        for (Account userAccount : userAccounts) {
            totalBalance += userAccount.getBalance();
        }
        if (2*sanctionAmount < totalBalance) {
            Loan loan = new Loan();
            loan.setSanctionAmount(sanctionAmount);
            loan.setUserId(userId);
            loanRepository.save(loan);
            return "Loan created successfully!";
        }
        return "Loan amount exceeds balance!";
    }

}
