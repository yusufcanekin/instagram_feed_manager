package datastructures;

/**
 * A generic implementation of a max-heap, which maintains the maximum element
 * at the root.
 *
 * @param <T> the type of elements stored in the heap; must be comparable
 */
public class MaxHeap<T extends Comparable<T>> {
    private T[] heap; // Array representation of the heap
    private int size; // Number of elements in the heap
    private int capacity; // Maximum capacity of the heap

    /**
     * Constructs a new MaxHeap with the specified initial capacity.
     *
     * @param capacity the initial capacity of the heap
     */
    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = (T[]) new Comparable[capacity + 1]; // Index 0 is unused
    }

    /**
     * Swaps two elements in the heap.
     *
     * @param index1 the index of the first element
     * @param index2 the index of the second element
     */
    private void swap(int index1, int index2) {
        T temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    /**
     * Checks if the heap has reached its capacity and resizes it if necessary.
     * The capacity is doubled when resizing.
     */
    private void checkCapacity() {
        if (size == capacity) {
            capacity *= 2; // Double the capacity
            T[] newHeap = (T[]) new Comparable[capacity + 1];
            System.arraycopy(heap, 1, newHeap, 1, size); // Copy elements to new heap
            heap = newHeap;
        }
    }

    /**
     * Inserts a new element into the heap.
     *
     * @param value the element to insert
     */
    public void insert(T value) {
        checkCapacity(); // Ensure sufficient capacity
        heap[++size] = value; // Add the element to the end
        percolateUp(size); // Adjust the heap to maintain the max-heap property
    }

    /**
     * Moves the element at the specified index up to its correct position
     * to maintain the max-heap property.
     *
     * @param index the index of the element to adjust
     */
    private void percolateUp(int index) {
        // While the element is not the root and is greater than its parent
        while (index > 1 && heap[index / 2].compareTo(heap[index]) < 0) {
            swap(index, index / 2); // Swap with the parent
            index = index / 2; // Move up to the parent's index
        }
    }

    /**
     * Removes and returns the maximum element (the root) of the heap.
     *
     * @return the maximum element, or {@code null} if the heap is empty
     */
    public T deleteMax() {
        if (size == 0) {
            return null; // Return null if the heap is empty
        }
        T max = heap[1]; // The root element (maximum)
        heap[1] = heap[size--]; // Replace the root with the last element
        percolateDown(1); // Adjust the heap to maintain the max-heap property
        return max;
    }

    /**
     * Moves the element at the specified index down to its correct position
     * to maintain the max-heap property.
     *
     * @param index the index of the element to adjust
     */
    private void percolateDown(int index) {
        int largest = index;
        int left = 2 * index; // Left child index
        int right = 2 * index + 1; // Right child index

        // Compare with left child
        if (left <= size && heap[left].compareTo(heap[largest]) > 0) {
            largest = left;
        }

        // Compare with right child
        if (right <= size && heap[right].compareTo(heap[largest]) > 0) {
            largest = right;
        }

        // If the largest is not the current index, swap and continue percolating down
        if (largest != index) {
            swap(index, largest); // Swap with the largest child
            percolateDown(largest);
        }
    }

    /**
     * Checks if the heap is empty.
     *
     * @return {@code true} if the heap is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
