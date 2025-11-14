package com.web.mvc.services;

import com.web.mvc.entities.Employee;
import com.web.mvc.entities.SalaryAccount;
import com.web.mvc.exceptions.ResourceNotFoundException;
import com.web.mvc.repositories.SalaryAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class SalaryAccountService {
    private final SalaryAccountRepository salaryAccountRepository;

    public void createEmployeeSalaryAccount(Employee employee){
//        if(employee.getName().startsWith("Piyush")){
//            throw new NotValidEmployeeException("Salary Account not initiated for this invalid employee!!");
//        }
        SalaryAccount salaryAccount = SalaryAccount.builder()
                .balance(BigDecimal.valueOf(0))
                .employee(employee)
                .build();

        salaryAccountRepository.save(salaryAccount);
    }

    public SalaryAccount incrementAccountBalance(Long accId) {
        SalaryAccount salaryAccount = salaryAccountRepository.findById(accId)
                .orElseThrow(() -> new ResourceNotFoundException("Salary Account not found with id : " + accId));
        salaryAccount.setBalance(salaryAccount.getBalance().add(BigDecimal.ONE));
        salaryAccountRepository.save(salaryAccount);
        return salaryAccount;
    }
}
