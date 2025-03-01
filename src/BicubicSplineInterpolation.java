import java.util.*;
import java.lang.Math;
import java.io.*;

public class BicubicSplineInterpolation {

    // Matriks A untuk interpolasi
    static Scanner scanner = new Scanner(System.in);
    public static StringBuilder output;
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

    // Fungsi untuk membaca input manual dari pengguna dengan validasi input
    public static Object[] readManualInput() {
        Scanner scanner = new Scanner(System.in);
        double[][] matrix = new double[4][4];
        double a = 0, b = 0;
        boolean inputValid = false;

        while (!inputValid) {
            try {
                System.out.println("Masukkan nilai:");
                System.out.println("f(0, 0), f(1, 0), f(0, 1), f(1, 1),");
                System.out.println("fx(0, 0), fx(1, 0), fx(0, 1), fx(1, 1),");
                System.out.println("fy(0, 0), fy(1, 0), fy(0, 1), fy(1, 1),");
                System.out.println("fxy(0, 0), fxy(1, 0), fxy(0, 1), fxy(1, 1)");
                System.out.println("Serta masukkan nilai a dan b yang ingin dicari dalam f(a, b):");

                // Membaca matriks 4x4
                for (int i = 0; i < 4; i++) {
                    String[] line = scanner.nextLine().trim().split("\\s+");
                    if (line.length != 4) {
                        System.out.println("Input tidak valid. Harus ada tepat 4 nilai di setiap baris. Silakan coba lagi.");
                        throw new IllegalArgumentException(); // Memicu input ulang
                    }

                    for (int j = 0; j < 4; j++) {
                        matrix[i][j] = Double.parseDouble(line[j]); // Mengubah input menjadi double
                    }
                }

                // Membaca nilai a dan b
                System.out.println("Masukkan nilai a dan b:");
                a = Double.parseDouble(scanner.next());
                b = Double.parseDouble(scanner.next());
                inputValid = true; // Menandakan input valid

            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Pastikan memasukkan angka. Silakan coba lagi.");
                scanner.nextLine(); // Membersihkan input scanner jika terjadi kesalahan
            } catch (IllegalArgumentException e) {
                // Tidak perlu melakukan apapun, hanya mengulang input
            }
        }

        return new Object[]{matrix, a, b};
    }


    // Fungsi untuk membaca input dari file TXT dengan validasi input
    public static Object[] readFromFile(String filename) throws IOException {
        double[][] matrix = new double[4][4];
        double a = 0, b = 0;

        File file = new File("test/" + filename);
        Scanner scanner = new Scanner(file);

        // Membaca matriks 4x4 dari file dengan validasi
        for (int i = 0; i < 4; i++) {
            String line = scanner.nextLine().trim();
            String[] values = line.split("\\s+");

            if (values.length != 4) {
                System.out.println("Input pada baris " + (i + 1) + " tidak valid. Harus ada tepat 4 nilai. Silakan coba lagi.");
                throw new IllegalArgumentException(); // Memicu input ulang
            }

            for (int j = 0; j < 4; j++) {
                try {
                    matrix[i][j] = Double.parseDouble(values[j]); // Memastikan nilai adalah angka
                } catch (NumberFormatException e) {
                    System.out.println("Input pada baris " + (i + 1) + " tidak valid. Harus berupa angka.");
                    throw new NumberFormatException();
                }
            }
        }

        // Membaca nilai a dan b dengan validasi
        try {
            a = scanner.nextDouble();
            b = scanner.nextDouble();
        } catch (NumberFormatException e) {
            System.out.println("Nilai a atau b tidak valid. Pastikan memasukkan angka.");
            throw new NumberFormatException();
        }

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
        Matrix coeffs = new Matrix(16, 1);
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

    public static void Bicubic(){
        System.out.println("1. Input manual");
        System.out.println("2. Input dari file TXT");
        int choice = Utility.getValidChoice(1, 02);

        Object[] data = null;

        try {
            if (choice == 1) {
                // Membaca input manual dari pengguna
                data = readManualInput();
            } else if (choice == 2) {
                System.out.println("Masukkan nama file (dengan ekstensi .txt):");
                String filename = scanner.next();
                data = readFromFile(filename);
            }
            double[][] matrix = (double[][]) data[0];
            double a = (double) data[1];
            double b = (double) data[2];
            Matrix matrixA = new Matrix(16, 16);
            matrixA.matrix = A;
            double[][] A_invArray = matrixA.InverseUsingGaussJordan();
            Matrix A_inv = new Matrix(16, 16);
            A_inv.matrix = A_invArray;
            double[] values = new double[16];
            int index = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    values[index++] = matrix[i][j];
                }
            }
        if(output == null){
            output = new StringBuilder();
        }
        Matrix coefficients = getCoefficients(values, A_inv);
        double result = bicubicInterpolation(coefficients, a, b);
            if(a<0 || a>1 || b<0 || b>1){
                System.out.println("Nilai titik yang dicari bukan interpolasi melainkan EKSTRAPOLASI");
                System.out.println("Nilai ekstrapolasi di titik (" + a + ", " + b + "): " + result);
                output.append("Nilai ekstrapolasi di titik (").append(a).append(", ").append("): ").append(result);
            }
            else{
                System.out.println("Nilai interpolasi di titik (" + a + ", " + b + "): " + result);
                output.append("Nilai interpolasi di titik (").append(a).append(", ").append("): ").append(result);
            }

        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        } 
    }
}
