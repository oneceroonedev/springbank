package com.springbank.system.service;

import com.springbank.system.model.accounts.StudentChecking;

import java.util.List;

public interface StudentCheckingService {
    StudentChecking createStudentChecking(StudentChecking studentChecking);
    List<StudentChecking> getAllStudentCheckingAccounts();
}