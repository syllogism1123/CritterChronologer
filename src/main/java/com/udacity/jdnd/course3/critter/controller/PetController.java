package com.udacity.jdnd.course3.critter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertPetDTOToPet(petDTO);
        pet = petService.getPetById(petService.save(pet));
        return convertPetToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return convertPetListToPetDTOList(petService.getAllPets());
    }

    @GetMapping("/owner/{ownerId}")
    @JsonView(Views.Public.class)
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return convertPetListToPetDTOList(petService.getPetsByOwner(ownerId));
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        Long ownerId = petDTO.getOwnerId();
        Customer customer = customerService.getCustomerById(ownerId);
        pet.setCustomer(customer);
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

    private List<PetDTO> convertPetListToPetDTOList(List<Pet> petList) {
        return petList.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }


}
