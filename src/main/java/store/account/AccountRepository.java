package store.account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountModel, String> {
    
    // Responsible for the data persistance logic of the Account entity    
    // using an ORM (Object Relational Mapping) to interact with the database

}
