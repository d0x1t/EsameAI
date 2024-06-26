package knn2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor {

    private List<Sample> trainingData;
    private KDTree kdtree;
    private int[] classCounts; // Assuming classes are labeled 0-9
    
    public NearestNeighbor(String filename) {
        this.trainingData = new ArrayList<>();
        this.kdtree = null;
        this.classCounts = new int[5]; // Adjust if you have a different number of classes
        
        this.readPointsFromCSV(filename);
    }

    private void readPointsFromCSV(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                
                // Add the sample by calling the constructor that takes the string input
                trainingData.add(new Sample(line));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.kdtree = new KDTree(trainingData); // Initialize the KDTree using the read points
    }

    public List<Sample> findKNearestNeighbors(Sample testPoint, int k) {
        return kdtree.kNearestNeighbors(testPoint, k);
    }

    public int classify(Sample testPoint, int k) {
        List<Sample> kNearestNeighbors = findKNearestNeighbors(testPoint, k);

        // Reset class counts
        for (int i = 0; i < classCounts.length; i++) {
            classCounts[i] = 0;
        }

        // Count the occurrences of each class in the k nearest neighbors
        for (Sample neighbor : kNearestNeighbors) {
            classCounts[neighbor.cls]++;
        }

        // Find the class with the maximum count
        int maxCount = -1;
        int predictedClass = -1;
        for (int i = 0; i < classCounts.length; i++) {
            if (classCounts[i] > maxCount) {
                maxCount = classCounts[i];
                predictedClass = i;
            }
        }

        return predictedClass;
    }

    public List<Sample> getTrainingData() {
        return trainingData;
    }
}
