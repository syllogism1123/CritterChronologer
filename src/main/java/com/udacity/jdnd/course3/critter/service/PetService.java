package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Long save(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getCustomer();

        List<Pet> customerPets = customer.getPets();

        if (customerPets == null) {
            customerPets = new ArrayList<>();
        }

        customerPets.add(savedPet);
        customer.setPets(customerPets);
        customerRepository.save(customer);
        return savedPet.getId();
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Customer getOwnerByPet(Long petId) {
        return petRepository.getReferenceById(petId).getCustomer();
    }

    public Pet getPetById(Long Id) {
        return petRepository.getReferenceById(Id);
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findAllByCustomerId(ownerId);
    }


}
