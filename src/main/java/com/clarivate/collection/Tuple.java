package com.clarivate.collection;

import com.clarivate.exception.InvalidNumberOfArgumentsException;
import com.clarivate.exception.InvalidObjectTypeException;

import java.util.ArrayList;
import java.util.List;

public class Tuple {

    private final List<Class<?>> classList = new ArrayList<>();

    private final List<Object> objectList = new ArrayList<>();

    private Tuple(Class<?>... classes) {
        classList.addAll(List.of(classes));
    }

    public Tuple createTuple(Object... objects) throws Exception {
        if (objects.length != classList.size()) {
            throw new InvalidNumberOfArgumentsException(objects.length, classList.size());
        }
        for (int i = 0; i < objects.length; i++) {
            if (!typeEquals(classList.get(i), objects[i].getClass())) {
                throw new InvalidObjectTypeException(classList.get(i).getName(), objects[i].getClass().getName());
            }
        }
        objectList.addAll(List.of(objects));
        return this;
    }

    public Object getElement(int position){
        if(position >= objectList.size())
            throw new ArrayIndexOutOfBoundsException();
        return objectList.get(position);
    }

    public void setElement(int position, Object element) throws Exception {
        if(position >= objectList.size())
            throw new ArrayIndexOutOfBoundsException();
        if(!typeEquals(classList.get(position), element.getClass()))
            throw new InvalidObjectTypeException(classList.get(position).getName(), element.getClass().getName());
        objectList.set(position, element);
    }

    private boolean typeEquals(Class<?> aClass, Class<?> bClass) {
        return aClass.getName().equals(bClass.getName());
    }

    public static Tuple tupleOf(Class<?>... classes) {
        return new Tuple(classes);
    }

    public int getSize(){
        return objectList.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < objectList.size(); i++) {
            sb.append(objectList.get(i).toString());
            if (i < objectList.size() -1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }
}
