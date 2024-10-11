import java.util.*;
import java.io.*;

public class regresi {
    static Scanner scanner = new Scanner(System.in); 
    static int n, m; //baris dan kolom (sesuai spesifikasi, ditandai dengan variabel n, m)
    static Matrix matrix_X = new Matrix();
    static Matrix matrix_Y = new Matrix();
    static Matrix temp = new Matrix(); //untuk simpan seluruh inputan
    static Matrix Mbeta = new Matrix(); //Matrix beta berisi koefisien seluruh x, (Y = beta_0 + beta_1.x1 + beta_2.x2 + ... + beta_k.xk)

    public static void readMatrix(){
        System.out.println("Masukkan metode penginputan: ");
        System.out.println("1. Input manual");
        System.out.println("2. Input dari file .txt");
        System.out.print("Pilihan: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        while(choice != 1 && choice != 2){
            System.out.println("Kesalahan: Masukkan sesuai pilihan.");
            System.out.print("Pilihan: ");
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        if(choice == 1){
            readMatrixManual();
        }
        else{
            readMatrixFile();
            m -=1 ;
        }

        setElmt_XY();
    }

    public static void readMatrixManual(){

        System.out.print("Masukkan jumlah variabel x : ");
        m = scanner.nextInt(); 

        System.out.print("Masukkan jumlah data : ");
        n = scanner.nextInt(); 

        temp = new Matrix(n, m+1);

        System.out.println("Masukkan data (x1,x2,...,xn, y)");

        for (int i = 0; i < n; i++){
            for (int j = 0; j < m+1; j++){
                temp.matrix[i][j] = scanner.nextDouble();
            }
        }
    }

    public static void readMatrixFile(){
        System.out.print("Masukkan nama file: ");
        String filename = "test/" +scanner.nextLine();
        try {
            File file = new File(filename);

            // membaca jumlah baris berdasarkan file.txt
            Scanner fileScanner = new Scanner(file);
            int row = 0;
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                row++;
            }
            fileScanner.close();
            n = row;

            // membaca jumlah kolom berdasarkan file.txt
            fileScanner = new Scanner(file);
            String line = fileScanner.nextLine().trim();
            String[] values = line.split("\\s+");
            fileScanner.close();
            m = values.length; 

            temp = new Matrix(n, m);

            fileScanner = new Scanner(file);

            for (int i = 0; i < n; i++) {
                String line1 = fileScanner.nextLine().trim();
                String[] values1 = line1.split("\\s+");

                for (int j = 0; j < m; j++) {
                    temp.matrix[i][j] = Double.parseDouble(values1[j]);
                }
        }
        fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Kesalahan: File tidak ditemukan.");
        } catch (NumberFormatException e) {
            System.out.println("Kesalahan: File berisi nilai yang bukan angka.");
        }
    }

    public static void setElmt_XY(){
        matrix_X = new Matrix(n,m+1);

        for (int i = 0; i < n; i++){
            matrix_X.matrix[i][0] = 1;
        }

        for (int i = 0; i < n; i++){
            for (int j = 1; j <= m; j++){
                matrix_X.matrix[i][j] = temp.matrix[i][j-1];
            }
        }
        
        //set element matrix_y
        matrix_Y = new Matrix(n, 1);
        
        for (int i = 0; i < n; i++){
            matrix_Y.matrix[i][0] = temp.matrix[i][m];
        }
    }

    public static void MultipleLinearRegression(){
        Matrix mX_Tran = new Matrix(m+1, n);
        Matrix mX_Inv = new Matrix(n,n);
        Matrix result1 = new Matrix(n,n); //X*XT
        Matrix result2 = new Matrix(n,1);

        mX_Tran.matrix = matrix_X.transposeMatrix(); 
        result1.matrix = matrix_X.multiplyMatrix(mX_Tran);
        mX_Inv.matrix = result1.InverseUsingGaussJordan(); //(X.XT)^-1
        result2.matrix = mX_Tran.multiplyMatrix(matrix_Y);

        Mbeta = new Matrix(n,1);
        Mbeta.matrix = mX_Inv.multiplyMatrix(result2); 

        System.out.println(); 
        Mbeta.writeMatrix();//delsoon
    }

    public static void main(String[] ags){
        readMatrix();
        MultipleLinearRegression();
    }
}
