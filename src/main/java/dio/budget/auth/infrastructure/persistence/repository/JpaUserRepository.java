package dio.budget.auth.infrastructure.persistence.repository;

import dio.budget.util.mapper.MapperUser;
import dio.budget.auth.domain.User;
import dio.budget.auth.domain.UserRepository;
import dio.budget.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository, UserDetailsService {
    private final UserEntityRepository userEntityRepository;
    private final MapperUser mapper;

    public JpaUserRepository(UserEntityRepository userEntityRepository, MapperUser mapper) {
        this.userEntityRepository = userEntityRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.modelToEntity(user);
        userEntityRepository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }




}
