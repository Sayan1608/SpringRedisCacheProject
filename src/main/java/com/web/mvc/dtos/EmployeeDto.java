package com.web.mvc.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.mvc.annotations.EmployeeAgeValidation;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {
    private Long id;

    @NotBlank(message = "Required field 'name' in Employee")
    @Size(min = 2, max = 50, message = "Please enter a valid 'name' in the range of 2 to 50 characters.")
    private String name;

    @Email
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@infosys\\.com$", message = "Please enter a valid email address")
    private String email;

    @PastOrPresent(message = "Joining date cannot be in future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    @EmployeeAgeValidation
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @JsonProperty("isActive")
    private Boolean isActive;
}
