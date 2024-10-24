import java.util.*;
import java.io.*;
import java.lang.Math;

public class Interpolasi{
    private int n;
    private Matrix point;
    private Matrix mat;
    private double x; 
    private double[] solOfInterpolation;
    static Scanner input = new Scanner(System.in);
    StringBuilder output; 

    public Interpolasi(){
        this.n = 0;
        this.output = new StringBuilder();
    }

    public void readInterpolasi(){
        // Memilih metode input
        System.out.println("Pilih metode input:");
        System.out.println("1. Input dari keyboard");
        System.out.println("2. Input dari file");
        int pilihan = Utility.getValidChoice(1, 2);
        if (pilihan == 1){
            // Input dari keyboard
            System.out.print("Masukkan jumlah titik: ");
            int n = input.nextInt();
            this.n = n;
            this.point = new Matrix(this.n, 2);
            System.out.println("Masukkan titik-titiknya:");
            for (int i = 0; i < n; i++){
                System.out.print("x" + (i+1) + " = ");
                this.point.matrix[i][0] = input.nextDouble();
                System.out.print("y" + (i+1) + " = ");
                this.point.matrix[i][1] = input.nextDouble();
            }
            System.out.print("Masukkan nilai x yang ingin dicari taksiran nilai y nya: ");
            this.x = input.nextDouble();

            // mengisi matrix mat yang merepresentasikan persamaan interpolasi
            this.mat = new Matrix(n, n+1);
            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++){
                    this.mat.matrix[i][j] = Math.pow(this.point.matrix[i][0], j);
                }
                this.mat.matrix[i][n] = this.point.matrix[i][1];
            }
        } else if (pilihan == 2){
            // Input dari file .txt
            System.out.print("Masukkan nama file: ");
            String filename = input.next();
            
            try {
                File file = new File(filename);

                Scanner fileInput = new Scanner(file);
                int n = 0;
                while (fileInput.hasNextLine()){
                    fileInput.nextLine();
                    n++;
                }
                n --;
                this.n = n;
                fileInput.close();

                fileInput = new Scanner(file);
                this.point = new Matrix(this.n, 2);
                for (int i = 0; i < this.n; i++){
                    String line = fileInput.nextLine().trim();
                    String[] values = line.split("\\s+");
                    this.point.matrix[i][0] = Double.parseDouble(values[0]);
                    this.point.matrix[i][1] = Double.parseDouble(values[1]);
                }
                String line = fileInput.nextLine().trim();
                String[] values = line.split("\\s+");
                this.x = Double.parseDouble(values[0]);
                
                this.mat = new Matrix(this.n, this.n + 1);
                for (int i = 0; i < this.n; i++){
                    for (int j = 0; j < this.n; j++){
                        this.mat.matrix[i][j] = (double) (float) Math.pow(this.point.matrix[i][0], j);
                    }
                    this.mat.matrix[i][n] = this.point.matrix[i][1];
                }
                fileInput.close();
            } catch (FileNotFoundException e){
                System.out.println("File tidak ditemukan");
            }
        }
    }

    public void solveInterpolasi(){
        // Mencari solusi dari persamaan interpolasi
        this.mat.solveSPLGaussJordanMethod();
        this.solOfInterpolation = new double[this.n];
        for (int i = 0; i < this.n; i++){
            this.solOfInterpolation[i] = this.mat.matrix[i][this.n];
        }
        //print this.solOfInterpolation
        // for (int i = 0; i < this.n; i++){
        //     System.out.print(this.solOfInterpolation[i] + ", ");
        // }
    }

     public void printSol() {
        // Mencetak solusi dari persamaan interpolasi
        System.out.println("Persamaan interpolasi:");
        for (int i = 0; i < this.n; i++) {
            if (i == 0) {
                this.output.append("y = ").append((float) this.solOfInterpolation[i]);
                System.out.print("y = " + (float) this.solOfInterpolation[i]);
            } else {
                if (this.solOfInterpolation[i] >= 0) {
                    this.output.append(" + ").append((float) this.solOfInterpolation[i]).append("x^").append(i);
                    System.out.print(" + " + (float) this.solOfInterpolation[i] + "x^" + i);
                } else {
                    this.output.append(" - ").append(- (float) this.solOfInterpolation[i]).append("x^").append(i);
                    System.out.print(" - " + (- (float) this.solOfInterpolation[i]) + "x^" + i);
                }
            }
        }
        System.out.println();
    }
    
    public void printfc() {
        double fx = 0;
        for (int i = 0; i < this.n; i++) {
            fx += this.solOfInterpolation[i] * Math.pow(this.x, i);
        }

        this.output.append("\n");
        String resultOutput = "Taksiran nilai y pada x = " + this.x + " adalah " + fx;
        this.output.append(resultOutput);
        System.out.println(resultOutput);
    }
    
}