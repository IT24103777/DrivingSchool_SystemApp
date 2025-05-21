package com.Driving_School_System.utils;

public class CustomQueue<T> {

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }

        rear = newNode;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot dequeue from an empty queue");
        }

        T data = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot peek an empty queue");
        }

        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = front;
        int index = 0;

        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }

        return array;
    }

    public static <E> CustomQueue<E> fromList(java.util.List<E> list) {
        if (list == null) {
            throw new IllegalArgumentException("Input list cannot be null");
        }

        CustomQueue<E> queue = new CustomQueue<>();

        for (E item : list) {
            queue.enqueue(item);
        }

        return queue;
    }

    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>(size);
        Node<T> current = front;

        while (current != null) {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<T> current = front;

        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }
}