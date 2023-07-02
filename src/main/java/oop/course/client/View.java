package oop.course.client;

import java.io.IOException;

public interface View {
    enum ViewType
    {
        LoginView,
        RegisterView,
        AccountView,
        TransferView,
        NoView
    }
    ViewType show() throws IOException;
}
