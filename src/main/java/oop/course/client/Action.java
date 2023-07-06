package oop.course.client;

import java.util.function.Consumer;

public interface Action {
    void perform(Consumer<IView.Type> sceneChangedHandler);
}
