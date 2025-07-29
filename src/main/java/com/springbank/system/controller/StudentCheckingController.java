package com.springbank.system.controller;

import com.springbank.system.model.accounts.StudentChecking;
import com.springbank.system.service.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-checking")
public class StudentCheckingController {

    @Autowired
    private StudentCheckingService studentCheckingService;

    @PostMapping
    public StudentChecking create(@RequestBody StudentChecking studentChecking) {
        return studentCheckingService.createStudentChecking(studentChecking);
    }

    @GetMapping
    public List<StudentChecking> getAll() {
        return studentCheckingService.getAllStudentCheckingAccounts();
    }
}