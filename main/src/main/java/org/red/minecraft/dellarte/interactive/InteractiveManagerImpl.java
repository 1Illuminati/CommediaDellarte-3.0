package org.red.minecraft.dellarte.interactive;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.interactive.*;
import org.red.minecraft.dellarte.library.util.PairKeyMap;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.exception.InteractiveException;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InteractiveManagerImpl<T> implements InteractiveManager<T> {
    private final HashMap<NamespacedKey, InteractiveObjInfo<T>> interactiveObjInfoMaps = new HashMap<>();
    private final Class<T> managerType;

    public InteractiveManagerImpl(Class<T> managerType) {
        this.managerType = managerType;
    }

    @Override
    public Class<T> getManagerType() {
        return managerType;
    }

    @Override
    public void registerInteractiveObj(InteractiveObj<T> obj) {
        interactiveObjInfoMaps.put(obj.getKey(), this.createObjInfo(obj));
        CommediaDellartePlugin.sendLog("Interactive Registered - Type:" + managerType.getName() + " Key:" + obj.getKey());
    }

    @Override
    public List<InteractiveObj<T>> getInteractiveObj(T obj) {
        List<InteractiveObj<T>> list = new ArrayList<>();
        for (InteractiveObjInfo<T> value : interactiveObjInfoMaps.values())
            if (value.getObj().isHasInteractive(obj)) list.add(value.getObj());

        return list;
    }

    @Override
    public boolean hasInteractiveObj(T obj) {
        for (InteractiveObjInfo<T> value : interactiveObjInfoMaps.values())
            if (value.getObj().isHasInteractive(obj)) return true;

        return false;
    }

    @Override
    public @Nullable InteractiveObj<T> getInteractiveObj(@NotNull NamespacedKey key) {
        InteractiveObjInfo<T> info = interactiveObjInfoMaps.getOrDefault(key, null);
        return info != null ? info.getObj() : null;
    }

    @Override
    public void runInteractiveObj(@NotNull T obj, @NotNull InteractiveObj<T> interactiveObj, @NotNull Class<? extends InteractiveAct<T>> act, @NotNull Event event) {
        InteractiveObjInfo<T> info = this.interactiveObjInfoMaps.getOrDefault(interactiveObj.getKey(), null);

        if (info == null) throw new InteractiveException.NotRegisteredInteractiveObj(interactiveObj);

        if (info.methodMap.containsKeys(act, event.getClass())) {
            Method method = info.methodMap.get(act, event.getClass());

            try {
                method.invoke(interactiveObj, obj, event);
                CommediaDellartePlugin.sendDebugLog("Run InteractiveObj Key-" + interactiveObj.getKey() + ", Class-" + managerType.getName());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InteractiveException(interactiveObj, e.getMessage());
            }
        }
    }


    @Override
    public void runHasInteractiveObj(@NotNull T obj, @NotNull Class<? extends InteractiveAct<T>> act, @NotNull Event event) {
        for (InteractiveObjInfo<T> value : interactiveObjInfoMaps.values()) {
            if (!value.getObj().isHasInteractive(obj)) continue;

            Class<? extends Event> eventClass = event.getClass();

            if (value.methodMap.containsKeys(act, eventClass)) {
                Method method = value.methodMap.get(act, eventClass);

                try {
                    Parameter[] params = method.getParameters();
                    Object[] args = new Object[params.length];

                    for (int i = 0; i < params.length; i++) {
                        Class<?> type = params[i].getType();
                        if (type.isAssignableFrom(this.managerType)) {
                            args[i] = obj;
                        } else if (type.isAssignableFrom(eventClass)) {
                            args[i] = event;
                        } else {
                            throw new InteractiveException.IllegalParameterInteractiveObj(value.obj);
                        }
                    }

                    method.invoke(value.obj, args);
                    CommediaDellartePlugin.sendDebugLog("Run InteractiveObj Key-" + value.obj.getKey() + ", Class-" + managerType.getName());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InteractiveException(value.obj, e.getMessage());
                }
            }
        }
    }

    private InteractiveObjInfo<T> createObjInfo(InteractiveObj<T> obj) {
        InteractiveObjInfo<T> objInfo = new InteractiveObjInfo<>(obj);
        Method[] methods = obj.getClass().getMethods();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(InteractiveAnnotation.class))
                continue;

            List<Class<? extends InteractiveAct<T>>> acts = getAct(obj, method);
            Class<?>[] classes = method.getParameterTypes();

            if (classes.length <= 2)
                continue;

            for (Class<? extends InteractiveAct<T>> act : acts) {
                Class<? extends Event> event = act.getAnnotation(InteractiveAct.ActAnnotation.class).event();


                if (paramCheck(method.getParameters(), event))
                    throw new InteractiveException.IllegalParameterInteractiveObj(obj);


                objInfo.methodMap.put(act, event, method);
            }
        }

        return objInfo;
    }

    private boolean paramCheck(Parameter[] params, Class<? extends Event> event) {
        if (params.length <= 2)
            return false;

        for (Parameter param : params) {
            if (!param.getType().isAssignableFrom(this.managerType) && !param.getType().isAssignableFrom(event))
                return false;
        }

        return true;
    }

    private @NotNull List<Class<? extends InteractiveAct<T>>> getAct(InteractiveObj<T> obj, Method method) {
        InteractiveAnnotation[] annotations = method.getAnnotationsByType(InteractiveAnnotation.class);

        List<Class<? extends InteractiveAct<T>>> acts = new ArrayList<>();

        for (InteractiveAnnotation annotation : annotations) {
            Class<? extends InteractiveAct<T>> act = (Class<? extends InteractiveAct<T>>) annotation.act();

            if (!act.isAnnotationPresent(InteractiveAct.ActAnnotation.class))
                throw new InteractiveException.InteractiveActAnnotationNotFound(obj);

            try {
                acts.add(act);
            } catch (Exception e) {
                throw new InteractiveException.InteractiveGenericException(obj);
            }
        }

        return acts;
    }


    private static final class InteractiveObjInfo<T> {
        public final InteractiveObj<T> obj;
        public final PairKeyMap<Class<? extends InteractiveAct<T>>, Class<? extends Event>, Method> methodMap = new PairKeyMap<>();

        public InteractiveObjInfo(InteractiveObj<T> obj) {
            this.obj = obj;
        }

        public InteractiveObj<T> getObj() {
            return obj;
        }
    }
}
