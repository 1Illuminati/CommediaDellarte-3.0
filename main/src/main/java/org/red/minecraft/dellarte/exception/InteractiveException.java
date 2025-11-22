package org.red.minecraft.dellarte.exception;

import org.red.minecraft.dellarte.library.interactive.InteractiveObj;

public class InteractiveException extends RuntimeException {
    public InteractiveException(InteractiveObj<?> obj, String message) {
        super("Obj: " + obj.getKey() + "/n" + message);
    }

    public static class InteractiveGenericException extends InteractiveException {
        public InteractiveGenericException(InteractiveObj<?> obj) {
            super(obj, "The generic type of InteractiveAct should be the same as InteractiveObj.");
        }
    }

    public static class InteractiveActAnnotationNotFound extends InteractiveException {
        public InteractiveActAnnotationNotFound(InteractiveObj<?> obj) {
            super(obj, "The InteractiveAct class must have an annotation from the InteractiveAct.ActAnnotation class.");
        }
    }

    public static class NotRegisteredInteractiveObj extends InteractiveException {
        public NotRegisteredInteractiveObj(InteractiveObj<?> obj) {
            super(obj, "Please register the InteractiveObj first.");
        }
    }
}
