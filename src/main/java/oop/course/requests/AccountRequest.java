package oop.course.requests;


import oop.course.interfaces.*;
import oop.course.tools.interfaces.*;

public class AccountRequest implements Id<String> {
    private final Form form;

    public AccountRequest(Form form) {
        this.form = form;
    }

    @Override
    public String id()  {
        return form.stringField("accountNumber");
    }
}
