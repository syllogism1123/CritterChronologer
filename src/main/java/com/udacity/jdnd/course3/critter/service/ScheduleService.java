package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Long save(Schedule schedule) {
        return scheduleRepository.save(schedule).getId();
    }

    public List<Schedule> getScheduleForPet(Long petId) {
        return scheduleRepository.findAllByPetsId(petId);
    }

    public Schedule getScheduleById(Long Id) {
        return scheduleRepository.getReferenceById(Id);
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findAllByEmployeesId(employeeId);
    }


    public List<Schedule> getScheduleForCustomer(Long customerId) {
        return scheduleRepository.findAllByPetsCustomerId(customerId);
    }


}
