package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Long save(Employee employee) {
        return employeeRepository.save(employee).getId();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long Id) {
        return employeeRepository.getReferenceById(Id);
    }

    public void setAvailabilityByEmployee(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getReferenceById(employeeId);
        employee.setDaysAvailable(daysAvailable);
    }

    //Find out which employees with the right skills are available on a given date.
    public List<Employee> getEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        return employeeRepository.findAllByDaysAvailableContains(date.getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());

    }


}
