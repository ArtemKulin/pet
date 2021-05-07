package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue(ProcedureType.Values.EXTERNAL_PARASITE)
public class ExternalParasiteProcedure extends Procedure {

    public ExternalParasiteProcedure() {
        super();
    }

    public ExternalParasiteProcedure(LocalDate date, String medicineBatchNumber,
                                     Boolean isPeriodical, Integer periodDays, Medicine medicine) {
        super(date, ProcedureType.EXTERNAL_PARASITE, medicineBatchNumber, isPeriodical, periodDays, medicine);
    }
}
