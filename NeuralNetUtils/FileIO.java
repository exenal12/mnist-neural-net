package NeuralNetUtils;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.FileReader;


public class FileIO {
    String data;
    BufferedReader bReader;
    FileReader fReader = new FileReader("./input/data.csv");
    File inputFile = new File("./input/data.csv");

    public void init() {
        if (inputFile.exists()) {
            bReader = new BufferedReader(new FileReader("./input/data.csv"));
            System.out.println("Input data found!");
        } else System.out.println("Input data not found!");
    }
}
