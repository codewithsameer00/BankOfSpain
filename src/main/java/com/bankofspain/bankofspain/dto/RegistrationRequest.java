
package com.bankofspain.bankofspain.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class RegistrationRequest {

    @NotBlank(message = "Please enter your full name")
    @Size(max = 100)
    private String fullName;

    @NotNull(message = "Please enter your date of birth")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private String fatherName;

    private String motherName;

    private String demoAddress;

    @NotBlank(message = "Please select your gender")
    @Pattern(
            regexp = "MALE|FEMALE|OTHER|PREFER_NOT_TO_SAY",
            message = "Please select a valid gender"
    )
    private String gender;

    @NotBlank(message = "Please select your marital status")
    @Pattern(
            regexp = "SINGLE|MARRIED",
            message = "Please select a valid marital status"
    )
    private String maritalStatus;

    @NotBlank(message = "Please choose a username")
    @Size(min = 4, max = 50,
            message = "Username must contain 4 to 50 characters")
    private String username;

    @NotBlank(message = "Please create a password")
    @Size(min = 8, max = 72,
            message = "Password must contain at least 8 characters")
    private String password;

    public RegistrationRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDemoAddress() {
        return demoAddress;
    }

    public void setDemoAddress(String demoAddress) {
        this.demoAddress = demoAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

