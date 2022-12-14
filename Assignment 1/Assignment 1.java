import java.lang.Math;

class FibSeq {
    public int[] arr;

    private static int fibIter(int n) {
        /* Iterative Method
        Complexity: O(n) */

        // verify input is valid
        if (n <= 0) {
            throw new RuntimeException("Cannot find Fibonnaci numbers below 1.");
        }

        int prev = 0;
        int cur = 1;
        int temp;

        for (int i=1; i<n; i++) {
            temp = cur;
            cur = prev + cur;
            prev = temp;
        }
        
        return cur;
    }

    private static int fibRecur(int n) {
        /* Recursive Method
        Complexity: O(2^n) */ 

        // verify input is valid
        if (n <= 0) {
            throw new RuntimeException("Cannot find Fibonnaci numbers below 1.");
        // base cases
        } else if (n == 1 || n == 2) {
            return 1;
        } else {
            return fibRecur(n-1) + fibRecur(n-2);
        }
    }

    class FiboArray {
        // size is position of last non-zero number in array.
        private int size = 0;
        // maxSize is length of array.
        private int maxSize = 0;

        // makes the initial array using fibIter.
        public void makeArray() {
            arr = new int[10];
            size = 5;
            maxSize = 10;

            for (int i=1;i<5;i++) {
                arr[i] = fibIter(i);
            }
        }

        // if size == maxSize, there is a number at the end of the array.
        public boolean isFull() {
            if (size == maxSize) {
                return true;
            }

            return false;
        }

        // transfers arr into a new array that is 5 larger.
        public void resize() {
            int[] tempArr = new int[maxSize+5];

            for (int i=0;i<maxSize;i++) {
                tempArr[i] = arr[i];

            }

            maxSize += 5;
            arr = tempArr;
        }

        // adds a number to the end of the array.
        public void add(int n) {
            // must expand array if full.
            if (isFull()) {
                resize();
            }

            arr[size++] = n;
        }

        // adds a number to the specified index.
        public void add(int n, int index) {
            if (index < 0 || index >= maxSize) {
                throw new RuntimeException("Invalid index.");
            }

            arr[index] = n;

            if(index > size) {
                size = index + 1;
            }
        }

        // returns index of specified int, -1 if not there.
        public int ifContains(int n) {
            for (int i=0;i<size;i++) {
                if (arr[i] == n) {
                    return i;
                }
            }

            return -1;
        }

        // removes first instance of n.
        public boolean remove(int n) {
            int contains = ifContains(n);

            if (contains != -1) {
                arr[contains] = 0;
                
                // make sure size is set correctly after removing (could remove number not on end).
                int i = maxSize - 1;
                while (arr[i] == 0) {
                    size = i;
                    i--;
                }

                return true;
            }
            
            return false;
        }

        // returns arr[i] for 0<=i<maxSize
        public int grab() {
            double rand = Math.random() * (maxSize);
            
            return arr[(int)rand];
        }

        // prints out array in format 0, 1, 1, 2, 3,... , 0
        public void print() {
            for (int i=0;i<maxSize;i++) {
                if (i == maxSize - 1)  {
                    System.out.println(arr[i]);
                } else {
                    System.out.print(arr[i] + ", ");
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(fibIter(10));
        FiboArray fibArr = new FibSeq().new FiboArray();

        fibArr.makeArray();
        fibArr.print();
    }
}
