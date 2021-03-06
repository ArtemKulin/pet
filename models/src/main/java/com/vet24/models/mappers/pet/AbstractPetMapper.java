package com.vet24.models.mappers.pet;

import com.vet24.models.dto.pet.AbstractNewPetDto;
import com.vet24.models.enums.PetType;
import com.vet24.models.pet.Pet;

public interface AbstractPetMapper {
    PetType getPetType();
    Pet abstractPetDtoToPet(AbstractNewPetDto petDto);
}
