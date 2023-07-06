package oop.course.client.actions;

import oop.course.client.views.IView;

import java.util.function.Consumer;

public interface Action {
    void perform(Consumer<IView.Type> sceneChangedHandler, Consumer requestHandler);
}
