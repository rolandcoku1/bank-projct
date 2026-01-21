package com.wearhouse.bankproject.service;

import com.wearhouse.bankproject.operational.entity.Client;
import com.wearhouse.bankproject.operational.entity.Loan;
import com.wearhouse.bankproject.operational.repository.ClientRepository;
import com.wearhouse.bankproject.operational.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
public class DataSeederService {

    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;

    public DataSeederService(ClientRepository clientRepository, LoanRepository loanRepository) {
        this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
    }

    // Use the Operational Transaction Manager
    @Transactional(transactionManager = "operationalTransactionManager")
    public void seedDatabase() {
        System.out.println("--- SEEDING DATABASE ---");

        String[] firstNames = {"John", "Jane", "Mike", "Emily", "Chris", "Sarah", "David", "Laura"};
        String[] lastNames = {"Smith", "Doe", "Johnson", "Brown", "Taylor", "Anderson", "Thomas", "Moore"};
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            // 1. Create a Client
            Client client = new Client();
            String fName = firstNames[random.nextInt(firstNames.length)];
            String lName = lastNames[random.nextInt(lastNames.length)];
            client.setFullName(fName + " " + lName);
            client.setEmail(fName.toLowerCase() + "." + lName.toLowerCase() + random.nextInt(1000) + "@example.com");
            client.setPhone("555-01" + random.nextInt(99));
            client.setAddress(random.nextInt(999) + " Main St");

            client = clientRepository.save(client);

            // 2. Create 1-3 Loans for this Client
            int numLoans = random.nextInt(3) + 1;
            for (int j = 0; j < numLoans; j++) {
                Loan loan = new Loan();
                loan.setClient(client);

                // Random Amount between 1,000 and 50,000
                double amount = 1000 + (50000 - 1000) * random.nextDouble();
                loan.setLoanAmount(BigDecimal.valueOf(amount));

                loan.setInterestRate(BigDecimal.valueOf(3.5 + random.nextDouble() * 5)); // 3.5% to 8.5%
                loan.setStartDate(LocalDate.now().minusDays(random.nextInt(365)));

                // Random Status: 70% Active, 20% Paid, 10% Default
                int statusRoll = random.nextInt(100);
                if (statusRoll < 70) loan.setStatus("ACTIVE");
                else if (statusRoll < 90) loan.setStatus("PAID");
                else loan.setStatus("DEFAULT"); // These generate the RISK

                loanRepository.save(loan);
            }
        }
        System.out.println("--- SEEDING COMPLETE: Created 20 Clients and their Loans ---");
    }
}