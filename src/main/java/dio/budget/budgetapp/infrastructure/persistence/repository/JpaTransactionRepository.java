package dio.budget.budgetapp.infrastructure.persistence.repository;

import dio.budget.auth.infrastructure.security.AuthenticatedUserProvider;
import dio.budget.budgetapp.domain.Category;
import dio.budget.util.mapper.MapperTransaction;
import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionRepository;
import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;
import dio.budget.budgetapp.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaTransactionRepository implements TransactionRepository {
    private final TransactionEntityRepository transactionEntityRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final MapperTransaction mapperTransaction;

    private final MapperTransaction mapper;

    public JpaTransactionRepository(TransactionEntityRepository transactionEntityRepository,
                                    MapperTransaction mapper,
                                    AuthenticatedUserProvider authenticatedUserProvider, MapperTransaction mapperTransaction) {
        this.transactionEntityRepository = transactionEntityRepository;
        this.mapper = mapper;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.mapperTransaction = mapperTransaction;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.modelToEntity(transaction);
        transactionEntityRepository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Override
    public List<Transaction> findByUserId() {
        UUID userId = authenticatedUserProvider.getCurrentUserId();
        return transactionEntityRepository.findByUserId(userId).stream().map(mapper::entityToModel).toList();
    }

    @Override
    public List<Transaction> findAll() {
        //var iterable = transactionEntityRepository.findAll().stream(iterable.spliterator(), false).toList();
        return transactionEntityRepository.findAll().stream().map(mapperTransaction::entityToModel).toList();
    }

    @Override
    public List<Transaction> findByCategory(Category category) {
        return transactionEntityRepository
                .findAllByCategory(category)
                .stream()
                .map(mapperTransaction::entityToModel)
                .toList();
    }

    @Override
    public List<ListByCategory> ListGroupByCategoryByUserId() {
        UUID userId = authenticatedUserProvider.getCurrentUserId();
        return transactionEntityRepository.ListGroupByCategoryByUserId(userId);
    }


}
