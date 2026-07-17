package com.projects.coaching_offline_support.student.service.impl;

import com.projects.coaching_offline_support.parent.entity.Parent;
import com.projects.coaching_offline_support.parent.repostiory.ParentRepository;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.student.service.StudentService;
import com.projects.coaching_offline_support.student.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentResponse addStudent(AddStudent request) {
//        Parent parent = Parent.builder()
//                .name(request.name())
//                .em
//                .build();
//        parentRepository.save(parent);

        Student student = Student.builder()
                .name(request.name())
                .standard(request.class_std())
                .batch(request.batch())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .parentName(request.parentName())
                .parentEmail(request.parentEmail())
                .parentNumber(request.parentNumber())
                .build();
        studentRepository.save(student);

        return  new StudentResponse(student.getId(),student.getName(),student.getStandard(),student.getBatch(),student.getCreatedAt());

    }

    @Override
    public Page<StudentResponse> getStudents(StudentFilter filter , Pageable pageable) {


        return studentRepository.findAll(StudentSpecification.filter(filter),pageable)
                .map( student -> new StudentResponse(
                        student.getId(),
                        student.getName(),
                        student.getStandard(),
                        student.getBatch(),
                        student.getCreatedAt()
                ));
    }


}
