package oop.course.entity;

import oop.course.tools.interfaces.*;

// TODO - rename
public class AccountRequest implements Id<String> {
    private final Form form;

    public AccountRequest(Form form) {
        this.form = form;
    }

    @Override
    public String id() throws Exception {
        return form.stringField("accountNumber");
    }
}
