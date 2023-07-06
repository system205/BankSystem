package oop.course.client.actions;

import oop.course.client.views.IView;

import java.util.function.Consumer;

//this class is to avoid null in other actions
public class EmptyAction implements Action {

    @Override
    public void perform(Consumer<IView.Type> sceneChangedHandler, Consumer requestHandler) {
        //do nothing
    }
}
