package oop.course.routes;

import oop.course.interfaces.Process;

public interface Route extends Process {
    boolean accept(String path);

}
