package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final PetService petService;

    public UserController(CustomerService customerService, EmployeeService employeeService,
                          PetService petService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = convertCustomerDTOToCustomer(customerDTO);
        customer = customerService.getCustomerById(customerService.save(customer));
        return convertCustomerToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return convertCustomerListToCustomerDTOList(customerService.getAllCustomers());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer = petService.getOwnerByPet(petId);
        return convertCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEmployee(employeeDTO);
        employee = employeeService.getEmployeeById(employeeService.save(employee));
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailabilityByEmployee(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employeeList = employeeService.getEmployeesForService(employeeDTO.getDate(),
                employeeDTO.getSkills());
        return convertEmployeeListToEmployeeDTOList(employeeList);
    }


    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Pet> pets = customer.getPets();
        List<Long> petIds = new ArrayList<>();
        if (pets != null) {
            pets.stream().forEach(p -> petIds.add(p.getId()));
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        if (petIds != null) {
            petIds.stream().forEach(petId -> pets.add(petService.getPetById(petId)));
            customer.setPets(pets);
        }
        return customer;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private List<EmployeeDTO> convertEmployeeListToEmployeeDTOList(List<Employee> employeeList) {
        return employeeList.stream().map(this::convertEmployeeToEmployeeDTO).collect(Collectors.toList());
    }

    private List<CustomerDTO> convertCustomerListToCustomerDTOList(List<Customer> customerList) {
        return customerList.stream().map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

}
