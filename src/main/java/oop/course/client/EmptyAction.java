package oop.course.client;

import java.util.function.Consumer;

//this class is to avoid null in other actions
public class EmptyAction implements Action {

    @Override
    public void perform(Consumer<IView.Type> sceneChangedHandler) {
        //do nothing
    }
}
