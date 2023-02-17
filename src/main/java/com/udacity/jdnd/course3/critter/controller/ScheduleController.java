package com.udacity.jdnd.course3.critter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final EmployeeService employeeService;
    private final PetService petService;

    public ScheduleController(ScheduleService scheduleService, EmployeeService employeeService, PetService petService) {
        this.scheduleService = scheduleService;

        this.employeeService = employeeService;
        this.petService = petService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToSchedule(scheduleDTO);
        schedule = scheduleService.getScheduleById(scheduleService.save(schedule));
        return convertScheduleToScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertScheduleListToScheduleDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertScheduleListToScheduleDTOList(scheduleService.getScheduleForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {

        return convertScheduleListToScheduleDTOList(scheduleService.getScheduleForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    @JsonView(Views.Public.class)
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return convertScheduleListToScheduleDTOList(scheduleService.getScheduleForCustomer(customerId));
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        List<Pet> pets = schedule.getPets();
        List<Employee> employees = schedule.getEmployees();
        List<Long> petIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();

        if (pets != null && employees != null) {
            pets.forEach(pet -> petIds.add(pet.getId()));
            employees.forEach(employee -> employeeIds.add(employee.getId()));
        }
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);

        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Long> petIds = scheduleDTO.getPetIds();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Pet> pets = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();

        if (petIds != null && employeeIds != null) {
            petIds.forEach(Id -> pets.add(petService.getPetById(Id)));
            employeeIds.forEach(Id -> employees.add(employeeService.getEmployeeById(Id)));
        }
        schedule.setPets(pets);
        schedule.setEmployees(employees);

        return schedule;
    }

    private List<ScheduleDTO> convertScheduleListToScheduleDTOList(List<Schedule> schedules) {
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

}
