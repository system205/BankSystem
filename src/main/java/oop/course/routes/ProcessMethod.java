package oop.course.routes;

import oop.course.interfaces.Process;

public interface ProcessMethod extends Process {
    boolean accept(String method);
}
