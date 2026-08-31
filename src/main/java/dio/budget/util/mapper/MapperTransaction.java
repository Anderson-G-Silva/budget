package dio.budget.util.mapper;

import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionId;
import dio.budget.budgetapp.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper (componentModel = "spring")
public interface MapperTransaction {

    // Entities -> models

    Transaction entityToModel(TransactionEntity entity);

    // Models -> entities



    TransactionEntity modelToEntity(Transaction entity);

    // model -> entity (TransactionId -> UUID)
    default UUID mapT(TransactionId id) {
        return id == null ? null : id.id();
    }

    // entity -> model (UUID -> TransactionId)
    default TransactionId mapT(UUID id) {
        return id == null ? null : new TransactionId(id);
    }

}

