import java.util.*;
import java.io.*;
import java.lang.Math;
from Matrix import *;

public class BicubicSplineInterpolation {

    double[][] A = {
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 1, 2, 3, 1, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
    {0, 1, 2, 3, 1, 1, 2, 3, 1, 1, 2, 3, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 2, 3, 1, 1, 2, 3},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 1, 1, 2, 3},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3, 1, 1, 2, 3}
    };











    // Fungsi untuk membaca file txt dan memisahkan matriks 4x4 serta nilai a, b
    public static Object[] readTxtFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        double[][] matrix = new double[4][4];
        String line;
        int row = 0;
        
        // Membaca matriks 4x4 dari file
        for (int i = 0; i < 4; i++) {
            line = reader.readLine();
            String[] values = line.split(" ");
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = Double.parseDouble(values[j]);
            }
        }
        
        // Membaca nilai a dan b dari baris terakhir
        line = reader.readLine();
        String[] values = line.split(" ");
        double a = Double.parseDouble(values[0]);
        double b = Double.parseDouble(values[1]);
        
        reader.close();
        return new Object[] { matrix, a, b };
    }








    // Fungsi manual untuk perkalian dua matriks
    public double[][] multiplyMatrix(Matrix other){
        // Mengalikan this.matrix dengan Matrix other
        if (this.colNum != other.rowNum) {
            System.out.println("Kesalahan: Jumlah kolom matriks pertama tidak sama dengan jumlah baris matriks kedua.");
            return null;
        }

        Matrix result = new Matrix(this.rowNum, other.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < other.colNum; j++) {
                for (int k = 0; k < this.colNum; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }

        return result.matrix;
    }







    // Fungsi untuk transpose sebuah matriks
    public double[][] transposeMatrix(){
        // menghasilkan matrix yang sudah di-transpose
        Matrix result = new Matrix(this.colNum, this.rowNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                result.matrix[j][i] = this.matrix[i][j];
            }
        }

        return result.matrix;
    }











    public double [][] InverseUsingGaussJordan(){
        // Menghasilkan invers dari this.matrix menggunakan metode Gauss-Jordan
        if (this.rowNum != this.colNum) {
            System.out.println("Kesalahan: Matriks harus berbentuk persegi.");
            return null;
        }

        Matrix augmentedMatrix = new Matrix(this.rowNum, this.colNum * 2);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                augmentedMatrix.matrix[i][j] = this.matrix[i][j];
            }
            augmentedMatrix.matrix[i][this.colNum + i] = 1;
        }

        int row = 0;
        int col = 0;
        while (row < this.rowNum && col < this.colNum) {
            // Mencari baris dengan elemen terbesar pada kolom ke-col
            int maxRow = row;
            for (int i = row + 1; i < this.rowNum; i++) {
                if (Math.abs(augmentedMatrix.matrix[i][col]) > Math.abs(augmentedMatrix.matrix[maxRow][col])) {
                    maxRow = i;
                }
            }

            // Menukar baris row dan maxRow
            for (int i = 0; i < augmentedMatrix.colNum; i++) {
                double temp = augmentedMatrix.matrix[row][i];
                augmentedMatrix.matrix[row][i] = augmentedMatrix.matrix[maxRow][i];
                augmentedMatrix.matrix[maxRow][i] = temp;
            }

            // Membagi baris row dengan elemen pada kolom ke-col
            double factor = augmentedMatrix.matrix[row][col];
            for (int i = col; i < augmentedMatrix.colNum; i++) {
                augmentedMatrix.matrix[row][i] /= factor;
            }

            // Mengurangi baris-baris selain row
            for (int i = 0; i < this.rowNum; i++) {
                if (i == row) {
                    continue;
                }

                factor = augmentedMatrix.matrix[i][col];
                for (int j = col; j < augmentedMatrix.colNum; j++) {
                    augmentedMatrix.matrix[i][j] -= factor * augmentedMatrix.matrix[row][j];
                }
            }

            row++;
            col++;
        }

        double[][] inverseMatrix = new double[this.rowNum][this.colNum];
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                inverseMatrix[i][j] = (double) (float) augmentedMatrix.matrix[i][this.colNum + j];
            }
        }
        return inverseMatrix;
    }












    // Fungsi untuk melakukan interpolasi bicubic spline
    public static double bicubicInterpolation(double[][] coeffs, double a, double b) {
        // Matriks X untuk nilai a
        double[][] X = { { 1, a, Math.pow(a, 2), Math.pow(a, 3) } };

        // Matriks Y untuk nilai b (sebagai kolom)
        double[][] Y = { { 1 }, { b }, { Math.pow(b, 2) }, { Math.pow(b, 3) } };

        // Hitung perkalian X * coeffs
        double[][] temp = X.multiplyMatrix(coeffs);

        // Hitung perkalian hasil dengan transpose Y (kolom vektor)
        double[][] result = temp.multiplyMatrix(Y);

        // Mengembalikan hasil akhir (skalar)
        return result[0][0];
    }

    // Fungsi untuk menghitung koefisien interpolasi bicubic
    public static double[][] getCoefficients(double[] values, double[][] A_inv) {
        // Melakukan perkalian A_inv dengan values untuk mendapatkan koefisien
        double[][] valuesMatrix = new double[16][1];
        for (int i = 0; i < 16; i++) {
            valuesMatrix[i][0] = values[i];
        }
        double[][] coeffs = A_inv.multiplyMatrix(valuesMatrix);

        // Bentuk kembali koefisien menjadi matriks 4x4
        double[][] reshaped = new double[4][4];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                reshaped[i][j] = coeffs[index++][0];
            }
        }
        return reshaped;
    }



    public static void main(String[] args) throws IOException {
        // Membaca file data.txt
        Object[] data = readTxtFile("data.txt");
        double[][] matrix = (double[][]) data[0];
        double a = (double) data[1];
        double b = (double) data[2];


        // Membuat objek matriks A
        Matrix matrixA = new Matrix(16, 16);
        matrixA.matrix = A;

        // Menghitung invers dari matriks A
        double[][] A_inv = matrixA.InverseUsingGaussJordan();


        // Menggunakan nilai dari file (16 nilai pertama) untuk menghitung koefisien
        double[] values = new double[16];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                values[index++] = matrix[i][j];
            }
        }
        double[][] coefficients = getCoefficients(values, A_inv);

        // Menghitung hasil interpolasi bicubic spline pada titik (a, b)
        double result = bicubicInterpolation(coefficients, a, b);
        System.out.println("Nilai interpolasi di titik (" + a + ", " + b + "): " + result);
    }
}
