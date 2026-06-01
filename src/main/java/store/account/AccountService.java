package store.account;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {

    // Responsible for the business logic of the Account microsservice, it uses
    // the AccountRepository to handle the data persistance of the Account entity,
    // and the AccountParser to handle parsing the inputs and outputs of the API endpoints

    @Autowired
    private AccountRepository accountRepository;

    public Account create(Account account) {

        if (account.password() == null || account.password().trim().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is empty");
        }

        account.passwordSha256(calcHash(account.password()));

        return accountRepository.save(
            new AccountModel(account)
        ).to();
    }

    @CacheEvict(value = "accounts", key = "#id")
    public void delete(String id) {
        accountRepository.deleteById(id);
    }

    @Cacheable(value = "accounts", key = "#id")
    public Account findById(String id) {
        return accountRepository.findById(id)
            .map(AccountModel::to)
            .orElse(null);
    }

    public Account findByEmailAndPassword(Account account) {
        String email = account.email();
        String passwordSha256 = calcHash(account.password());

        return accountRepository.findByEmailAndPasswordSha256(email, passwordSha256)
            .map(AccountModel::to)
            .orElse(null);
    }

    public List<Account> findByAll() {

        return StreamSupport.stream(
            accountRepository.findAll().spliterator(),
            false
        ).map(AccountModel::to)
        .toList();
    }

    private String calcHash(String text) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            return Base64.getEncoder().encodeToString(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
