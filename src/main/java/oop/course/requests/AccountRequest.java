package oop.course.requests;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.tools.interfaces.*;

public class AccountRequest implements Id<String> {
    private final Form form;

    public AccountRequest(Form form) {
        this.form = form;
    }

    @Override
    public String id() throws MalformedDataException {
        return form.stringField("accountNumber");
    }
}
