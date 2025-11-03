package com.web.mvc.controllers;

import com.web.mvc.dtos.EmployeeDto;
import com.web.mvc.exceptions.ResourceNotFoundException;
import com.web.mvc.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

   private final EmployeeService employeeService;
   

    @GetMapping(path = "/getSecretMessage")
    public String getMySecretMessage(){
        return "$%yuunjnknSabyjbjnkjnkn$$";
    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(name = "employeeId") Long id){
        Optional<EmployeeDto> optionalEmployeeDto = employeeService.getEmployeeById(id);
        return optionalEmployeeDto
                .map(ResponseEntity::ok)
                .orElseThrow(()->new ResourceNotFoundException("Employee Not Found with id : "+id));
    }


//    @GetMapping()
//    public String getAllEmployees(@RequestParam(name = "id")Long id, @RequestParam(name = "age",required = false) Long age){
//        return "Id ="+id+", age="+age;
//    }

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody @Valid EmployeeDto employee){
        return new ResponseEntity<>(employeeService.createNewEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployeeDetails(@PathVariable(name = "employeeId")Long id,
                                             @RequestBody @Valid EmployeeDto employeeDto){
        return  ResponseEntity.ok(employeeService.updateEmployeeDetails(id,employeeDto));
    }

    @DeleteMapping(path = "{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable(name = "employeeId")Long id){
       if(employeeService.deleteEmployeeById(id)){
           return ResponseEntity.ok(Boolean.TRUE);
       }
       return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployeePartially(@PathVariable(name = "employeeId")Long id,
                                               @RequestBody Map<String, Object> updates){
        EmployeeDto employeeDto = employeeService.updateEmployeePartially(id, updates);
        if(employeeDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDto);
    }

}
