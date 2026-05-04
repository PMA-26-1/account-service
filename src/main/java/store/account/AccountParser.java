package store.account;

import java.util.List;

public class AccountParser {

    // Parses the input and output of the API endpoints, converting the 
    // AccountIn and AccountOut DTOs into the Account entity, and vice-versa
    
    public static AccountOut to(Account a) {

        // Converts Account object into AccountOut DTO

        return a == null ? null :
            AccountOut.builder()
                .id(a.id())
                .name(a.name())
                .email(a.id())
                .build();

    }

    public static List<AccountOut> to(List<Account> l) {

        // Applies the last method to a list of Accounts,
        // converting them into a list of AccountOuts

        return l.stream().map(AccountParser::to).toList();
    }

    public static Account to(AccountIn in) {

        // Converts AccountIn DTO into Account object

        return in == null ? null :
            Account.builder()
                .name(in.name()) 
                .email(in.email()) 
                .password(in.password())
                .build();
    }

}
