package dio.budget.util.mapper;

import dio.budget.auth.domain.User;
import dio.budget.auth.domain.UserId;
import dio.budget.auth.infrastructure.persistence.entity.UserEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface MapperUser {

    // Entities -> models

    User entityToModel(UserEntity entity);


    // Models do entities

    UserEntity modelToEntity(User model);

    // model -> entity (UserId -> UUID)
    default UUID mapT(UserId id) {
        return id == null ? null : id.id();
    }

    // entity -> model (UUID -> UserId)
    default UserId mapT(UUID id) {
        return id == null ? null : new UserId(id);
    }

}

