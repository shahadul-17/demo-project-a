package com.demoproject.common;

public final class Tuple {

    private final Object[] elements;

    private Tuple(Object[] elements) {
        this.elements = elements;
    }

    public <Type> Type get(int elementPosition, Class<Type> classOfType) {
        int elementIndex = elementPosition - 1;
        Object element = elementIndex > -1 && elementIndex < elements.length
                ? elements[elementIndex] : null;

        // if the element is null or the types mismatch, we shall return null...
        if (element == null || !classOfType.isAssignableFrom(element.getClass())) { return null; }

        return (Type) element;
    }

    /**
     * Creates a tuple of items.
     * @implNote A tuple must contain at least two items.
     * @param firstElement The first element of the tuple.
     * @param secondElement The second element of the tuple.
     * @param restOfTheElements Rest of the elements of the tuple (optional).
     * @return A tuple containing all the elements provided.
     */
    public static Tuple of(Object firstElement,
                           Object secondElement,
                           Object ...restOfTheElements) {
        Object[] elements = new Object[restOfTheElements.length + 2];
        elements[0] = firstElement;
        elements[1] = secondElement;

        for (int i = 2, j = 0; i < elements.length || j < restOfTheElements.length; ++i, ++j) {
            elements[i] = restOfTheElements[j];
        }

        return new Tuple(elements);
    }
}
