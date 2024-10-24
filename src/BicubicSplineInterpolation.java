import java.util.*;
import java.lang.Math;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BicubicSplineInterpolation {

    // Matriks A untuk interpolasi
    static double[][] A = {
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
        {0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0},
        {0, 0, 0, 0, 0, 1, 2, 3, 0, 2, 4, 6, 0, 3, 6, 9}
    };

    // Fungsi untuk membaca input manual dari pengguna
    public static Object[] readManualInput() {
        Scanner scanner = new Scanner(System.in);
        double[][] matrix = new double[4][4];

        System.out.println("Masukkan nilai:");
        System.out.println("f(0, 0), f(1, 0), f(0, 1), f(1, 1),");
        System.out.println("fx(0, 0), fx(1, 0), fx(0, 1), fx(1, 1),");
        System.out.println("fy(0, 0), fy(1, 0), fy(0, 1), fy(1, 1),");
        System.out.println("fxy(0, 0), fxy(1, 0), fxy(0, 1), fxy(1, 1)");
        System.out.println("Serta masukkan nilai a dan b yang ingin dicari dalam f(a, b):");

        // Membaca matriks 4x4
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

        // Membaca nilai a dan b
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();

        scanner.close();
        return new Object[]{matrix, a, b};
    }

    // Fungsi untuk membaca input dari file TXT
    public static Object[] readFromFile(String filename) throws IOException {
        double[][] matrix = new double[4][4];
        double a, b;

        File file = new File("test/" + filename);
        Scanner scanner = new Scanner(file);

        // Membaca matriks 4x4 dari file
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

        // Membaca nilai a dan b
        a = scanner.nextDouble();
        b = scanner.nextDouble();

        scanner.close();
        return new Object[]{matrix, a, b};
    }

    // Fungsi untuk melakukan interpolasi bicubic spline
    public static double bicubicInterpolation(Matrix coeffs, double a, double b) {
        // Matriks X untuk nilai a
        double[][] X = { { 1, a, Math.pow(a, 2), Math.pow(a, 3) } };

        // Matriks Y untuk nilai b (sebagai kolom)
        double[][] Y = { { 1 }, { b }, { Math.pow(b, 2) }, { Math.pow(b, 3) } };

        // Hitung perkalian X * coeffs
        Matrix XMatrix = new Matrix(1, 4);
        XMatrix.matrix = X;
        Matrix coeffsMatrix = new Matrix(4, 4); 
        coeffsMatrix.matrix = coeffs.matrix; // Pastikan coeffs sudah diisi dengan benar

        double[][] temp = XMatrix.multiplyMatrix(coeffsMatrix);
        Matrix tempMatrix = new Matrix(1, 4);
        tempMatrix.matrix = temp;

        Matrix YMatrix = new Matrix(4, 1);
        YMatrix.matrix = Y;

        double[][] result = tempMatrix.multiplyMatrix(YMatrix); // Sesuaikan dengan logika perkalian matriks yang benar
        Matrix resultMatrix = new Matrix(1, 1);
        resultMatrix.matrix = result;
        return resultMatrix.matrix[0][0]; // Mengembalikan hasil akhir (skalar)
    }

    // Fungsi untuk menghitung koefisien interpolasi bicubic
    public static Matrix getCoefficients(double[] values, Matrix A_inv) {
        // melakukan perkalian A_inv dengan values untuk mendapatkan koefisien
        Matrix valuesMatrix = new Matrix(16, 1);
        for (int i = 0; i < 16; i++) {
            valuesMatrix.matrix[i][0] = values[i];
        }
        double[][] coeffsArray = A_inv.multiplyMatrix(valuesMatrix);
        Matrix coeffs = new Matrix(4, 4);
        coeffs.matrix = coeffsArray;



        // membentuk kembali koefisien menjadi matriks 4x4
        Matrix reshaped = new Matrix(4, 4);
        int index = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                reshaped.matrix[i][j] = coeffs.matrix[index++][0];
            }
        }
        return reshaped;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        // Meminta input hingga pilihan yang valid diberikan
        while (choice != 1 && choice != 2) {
            System.out.println("Pilih opsi input:");
            System.out.println("1. Input manual");
            System.out.println("2. Input dari file TXT");
            choice = scanner.nextInt();

            if (choice != 1 && choice != 2) {
                System.out.println("Opsi tidak valid. Silakan masukkan 1 atau 2.");
            }
        }

        Object[] data = null;

        try {
            if (choice == 1) {
                // Membaca input manual dari pengguna
                data = readManualInput();
            } else if (choice == 2) {
                // Membaca input dari file TXT
                System.out.println("Masukkan nama file (dengan ekstensi .txt):");
                String filename = scanner.next();
                data = readFromFile(filename);
            }

            // Mengambil nilai matriks dan a, b
            double[][] matrix = (double[][]) data[0];
            double a = (double) data[1];
            double b = (double) data[2];

            // Membuat objek matriks A
            Matrix matrixA = new Matrix(16, 16);
            matrixA.matrix = A;
            // Menghitung invers dari matriks A
            double[][] A_invArray = matrixA.InverseUsingGaussJordan();
            Matrix A_inv = new Matrix(16, 16);
            A_inv.matrix = A_invArray;
            // Menggunakan nilai dari input manual untuk menghitung koefisien
            double[] values = new double[16];
            int index = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    values[index++] = matrix[i][j];
                }
            }
        
        Matrix coefficients = getCoefficients(values, A_inv);
        // Menghitung hasil interpolasi bicubic spline pada titik (a, b)
        double result = bicubicInterpolation(coefficients, a, b);

            System.out.println("Nilai interpolasi di titik (" + a + ", " + b + "): " + result);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
