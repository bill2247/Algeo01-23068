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
        Matrix result1 = new Matrix(n,n); //X*XT
        Matrix result2 = new Matrix(n,1);

        mX_Tran.matrix = matrix_X.transposeMatrix(); 
        result1.matrix = mX_Tran.multiplyMatrix(matrix_X);
        result2.matrix = mX_Tran.multiplyMatrix(matrix_Y);

        result1.roundMatrixElements();
        result2.roundMatrixElements();

        //gabung result1 dan result2 menjadi matrix augmented

        Matrix augmented = new Matrix(result1.rowNum, result1.colNum+1);

        for (int i = 0; i < result1.rowNum; i++){
            for(int j = 0; j < result1.colNum; j++){
                augmented.matrix[i][j] = result1.matrix[i][j];
            }
            augmented.matrix[i][result1.colNum] = result2.matrix[i][0];
        }

        augmented.roundMatrixElements();
        augmented.solveSPLGaussMethod();
        

        Mbeta = new Matrix(n,1);

        System.out.println(); 
        Mbeta.writeMatrix();//delsoon
    }

    public static void MultipleQuadraticRegression(){
        //regresi kuadratik setidaknya 3 titik data (n >= 3);

        int newM;
        newM = (m*(m+1)/2) + m + 1;

        Matrix newMX = new Matrix(n, newM);

        //xi^2
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                newMX.matrix[i][j] = (matrix_X.matrix[i][j+1])*(matrix_X.matrix[i][j+1]);
            }
        }

        //xi*xj
        int kolom=0;

        for (int baris = 0; baris < n; baris++){
            kolom = m;
            for (int i = 0; i < m; i++){
                for (int j = i + 1; j < m; j++){
                    newMX.matrix[baris][kolom] = (matrix_X.matrix[baris][i+1] * matrix_X.matrix[baris][j+1]);
                    kolom += 1;
                }
            }
        }

        //xi
        for (int baris = 0; baris < n; baris++){
            int j = 1;
            for (int i = kolom; i < newM-1; i++){
                newMX.matrix[baris][i] = matrix_X.matrix[baris][j];
                j++;
            }
        }

        //1
        for (int baris = 0; baris < n; baris++){
            newMX.matrix[baris][newM-1] = 1;
        }

        //algoritma sama kyk linear (matrix : newMX)
        Matrix mX_Tran = new Matrix(newM, n); 
        Matrix result1 = new Matrix(newM,newM); //X*XT
        Matrix result2 = new Matrix(newM,1);
    
        System.out.println();
        newMX.writeMatrix(); System.out.println();
        matrix_Y.writeMatrix(); System.out.println();

        mX_Tran.matrix = newMX.transposeMatrix();  
        result1.matrix = mX_Tran.multiplyMatrix(newMX);
        result2.matrix = mX_Tran.multiplyMatrix(matrix_Y); 
        
        mX_Tran.writeMatrix(); System.out.println();
        result1.writeMatrix(); System.out.println();
        result2.writeMatrix(); System.out.println();
        
        Matrix augmented = new Matrix(result1.rowNum, result1.colNum+1);

        for (int i = 0; i < result1.rowNum; i++){
            for(int j = 0; j < result1.colNum; j++){
                augmented.matrix[i][j] = result1.matrix[i][j];
            }
            augmented.matrix[i][result1.colNum] = result2.matrix[i][0];
        }

        augmented.writeMatrix();
        augmented.solveSPLGaussMethod();

        Mbeta = new Matrix(newM,1);

        System.out.println(); 
        Mbeta.writeMatrix();//delsoon

        /*coba cek lagi yang using gauss jordan dan using adj, soalnya ada perbedaan hasil, 
        tapi dari dua pendekatan yang ada mendekati semua (kalau dibulatkan cenderung sama) (namanya juga regeresi, cuman taksiran)*/
    }

    public static void main(String[] ags){
        readMatrix();
        //MultipleQuadraticRegression();
        MultipleLinearRegression();
    }
}