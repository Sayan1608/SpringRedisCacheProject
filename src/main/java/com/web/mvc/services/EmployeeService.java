package com.web.mvc.services;

import com.web.mvc.dtos.EmployeeDto;
import com.web.mvc.entities.Employee;
import com.web.mvc.entities.SalaryAccount;
import com.web.mvc.exceptions.ResourceNotFoundException;
import com.web.mvc.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final String CACHE_NAME = "employees";

    @Autowired
    private SalaryAccountService salaryAccountService;

    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    @CachePut(cacheNames = CACHE_NAME, key = "#result.id")
    @Transactional
    public EmployeeDto createNewEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        //create salary account and associate with the employee

        EmployeeDto employeeDto1 = modelMapper.map(employeeRepository.save(employee), EmployeeDto.class);
        salaryAccountService.createEmployeeSalaryAccount(employee);
        return employeeDto1;
    }

    @CachePut(cacheNames = CACHE_NAME, key = "#id")
    public EmployeeDto updateEmployeeDetails(Long id, EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        if(isExists(id))
            employee.setId(id);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    public boolean deleteEmployeeById(Long id) {
        isExists(id);
        employeeRepository.deleteById(id);
        return true;
    }

    private boolean isExists(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException("Employee Not Found with id : " + id);
        return true;
    }

    public EmployeeDto updateEmployeePartially(Long id, Map<String, Object> updates) {
        isExists(id);
        Employee employee = employeeRepository.findById(id).get();
        updates.forEach((key, value)->{
            Field field = ReflectionUtils.findRequiredField(Employee.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field,employee,value);
        });
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    public SalaryAccount incrementAccountBalance(Long accId) {
        return salaryAccountService.incrementAccountBalance(accId);
    }
}
