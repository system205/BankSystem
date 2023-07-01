package oop.course.implementations;

import oop.course.interfaces.*;
import oop.course.interfaces.Process;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.storage.*;
import oop.course.tools.implementations.*;

public class AccountReturn implements Process {
    private final Database<String, CheckingAccount> accountDb;

    public AccountReturn(Database<String, CheckingAccount> accountDb) {
        this.accountDb = accountDb;
    }

    @Override
    public Response act(Request request) {
        return new CheckingResponse(
                new CheckingAccount(
                        this.accountDb,
                        new AccountRequest(
                                new JsonForm(request.body())
                        ).id()
                ).json()
        );
    }
}
