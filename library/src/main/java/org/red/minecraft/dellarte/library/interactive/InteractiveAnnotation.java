package org.red.minecraft.dellarte.library.interactive;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(InteractiveAnnotation.Array.class)
public @interface InteractiveAnnotation {
    Class<? extends InteractiveAct<?>> act();

    InteractivePriority priority() default InteractivePriority.NORMAL;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Array {
        InteractiveAnnotation[] value();
    }
}