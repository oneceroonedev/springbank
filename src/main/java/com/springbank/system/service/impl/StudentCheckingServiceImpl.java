package com.springbank.system.service.impl;

import com.springbank.system.model.accounts.StudentChecking;
import com.springbank.system.repository.StudentCheckingRepository;
import com.springbank.system.service.StudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCheckingServiceImpl implements StudentCheckingService {

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Override
    public StudentChecking createStudentChecking(StudentChecking studentChecking) {
        return studentCheckingRepository.save(studentChecking);
    }

    @Override
    public List<StudentChecking> getAllStudentCheckingAccounts() {
        return studentCheckingRepository.findAll();
    }
}