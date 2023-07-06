package oop.course.client.actions;

import oop.course.client.views.IView;

import java.util.function.Consumer;

public class ChangeSceneAction implements Action {
    private final IView.Type type;
    private final Action next;

    public ChangeSceneAction(IView.Type type, Action next) {
        this.type = type;
        this.next = next;
    }

    public ChangeSceneAction(IView.Type type) {
        this.type = type;
        this.next = new EmptyAction();
    }

    @Override
    public void perform(Consumer<IView.Type> sceneChangedHandler, Consumer requestHandler) {
        sceneChangedHandler.accept(type);
        next.perform(sceneChangedHandler, requestHandler);
    }
}
