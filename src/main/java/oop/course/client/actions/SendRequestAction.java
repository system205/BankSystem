package oop.course.client.actions;

import oop.course.client.requests.Request;
import oop.course.client.responses.Response;
import oop.course.client.views.IView;

import java.util.function.Consumer;

public class SendRequestAction<T extends Response> implements Action {
    private final Action next;
    private final Request<T> request;

    public SendRequestAction(Action next, Request<T> request) {
        this.next = next;
        this.request = request;
    }

    @Override
    public void perform(Consumer<IView.Type> sceneChangedHandler, Consumer requestHandler) {
        requestHandler.accept(request);
        next.perform(sceneChangedHandler, requestHandler);
    }
}
