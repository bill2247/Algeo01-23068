import java.util.*;
import java.io.*;

public class Regresi {
    static Scanner scanner = new Scanner(System.in); 
    static int n, m; // baris dan kolom (sesuai spesifikasi, ditandai dengan variabel n, m)
    static Matrix matrix_X = new Matrix();
    static Matrix matrix_Y = new Matrix();
    static Matrix Data = new Matrix(); // untuk simpan seluruh inputan
    static double[] Target;

    public static void readMatrix() {
        System.out.println("Masukkan metode penginputan: ");
        System.out.println("1. Input manual");
        System.out.println("2. Input dari file .txt");
        int choice = Utility.getValidChoice(1, 2);
        
        if (choice == 1) {
            readMatrixManual();
        } else {
            readMatrixFile();
            m -= 1;
        }

        setElmt_XY();
    }

    public static void readMatrixManual() {
        String input1, input2;
        System.out.print("Masukkan jumlah variabel x : ");
        input1 = scanner.nextLine();
        System.out.print("Masukkan jumlah data : ");
        input2 = scanner.nextLine();

        while(!Utility.cek_int(input1) || !Utility.cek_int(input2)){
            System.out.println("Terdapat masukkan yang tidak valid.");
            System.out.println("Periksa Kembali.");
            System.out.print("Masukkan jumlah variabel x : ");
            input1 = scanner.nextLine();
            System.out.print("Masukkan jumlah data : ");
            input2 = scanner.nextLine();
        }

        n = Integer.parseInt(input2);
        m = Integer.parseInt(input1);

        Data = new Matrix(n, m + 1); // +1 untuk kolom target (y)

        System.out.println("Masukkan data (x1, x2, ..., xm, y)");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m + 1; j++) {
                Data.matrix[i][j] = scanner.nextDouble();
            }
        }

        // Mengambil target dari baris terakhir
        Target = new double[m]; // Target sesuai jumlah variabel
        System.out.println("Masukkan nilai data yang ingin dihampiri (x1, x2, ..., xm): ");
        for (int i = 0; i < m; i++) {
            Target[i] = scanner.nextDouble();
        }
    }

    public static void readMatrixFile() {
        System.out.print("Masukkan nama file: ");
        String filename = "test/" + scanner.nextLine();
        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            int rowCount = 0;
            int colCount = 0;

            // Menghitung jumlah baris dan kolom di file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (rowCount == 0) {
                    colCount = line.split("\\s+").length; // Menentukan jumlah kolom dari baris pertama
                }
                rowCount++;
            }
            fileScanner.close();
            
            n = rowCount - 1; // Baris terakhir untuk Target
            m = colCount; // Jumlah kolom ditentukan dari baris pertama
            
            Data = new Matrix(n, m); // Matrix tanpa kolom target
            Target = new double[m]; // Menyimpan target yang sama dengan jumlah kolom

            // Membaca kembali file dan mengisi matrix Data
            fileScanner = new Scanner(new File(filename));
            for (int i = 0; i < n; i++) {
                String[] values = fileScanner.nextLine().trim().split("\\s+");
                for (int j = 0; j < m; j++) {
                    Data.matrix[i][j] = Double.parseDouble(values[j]);
                }
            }
            
            // Membaca baris terakhir sebagai Target
            String[] lastLine = fileScanner.nextLine().trim().split("\\s+");
            // Memastikan ukuran array tidak terlewati
            for (int j = 0; j < Math.min(lastLine.length, m); j++) {
                Target[j] = Double.parseDouble(lastLine[j]);
            }
    
            fileScanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Kesalahan: File tidak ditemukan.");
            return;
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
                matrix_X.matrix[i][j] = Data.matrix[i][j-1];
            }
        }
        
        //set element matrix_y
        matrix_Y = new Matrix(n, 1);
        
        for (int i = 0; i < n; i++){
            matrix_Y.matrix[i][0] = Data.matrix[i][m];
        }
    }

    public static void MultipleLinearRegression(){
        Matrix mX_Tran = new Matrix(matrix_X.colNum, matrix_X.rowNum);
        Matrix result1 = new Matrix(matrix_X.colNum, matrix_X.colNum); // X * XT
        Matrix result2 = new Matrix(matrix_X.colNum, 1);
    
        mX_Tran.matrix = matrix_X.transposeMatrix(); 
        result1.matrix = mX_Tran.multiplyMatrix(matrix_X);
        result2.matrix = mX_Tran.multiplyMatrix(matrix_Y);
    
        // Gabung result1 dan result2 menjadi matrix augmented
        Matrix augmented = new Matrix(result1.rowNum, result1.colNum + 1);
    
        for (int i = 0; i < result1.rowNum; i++) {
            for (int j = 0; j < result1.colNum; j++) {
                augmented.matrix[i][j] = result1.matrix[i][j];
            }
            augmented.matrix[i][result1.colNum] = result2.matrix[i][0];
        }
    
        Utility.roundMatrixElements(augmented);
        augmented.solveSPLGaussMethod(false);
        
        double[] Mbeta;
        double hasil = 0;
        Mbeta = augmented.SPLsolution;

        //atasi null
        if(augmented.SPLsolution == null){
            System.out.println("Tidak ada solusi pasti, tidak bisa menentukan nilai Y.");
            System.out.println("Berikut persamaan parametrik.");
            System.out.print("Y = ");
            for (int i = 0; i < augmented.infiniteSPLsol.length; i++){
                if(i == 0){
                    System.out.print(augmented.infiniteSPLsol[i]);
                }
                else{
                    System.out.print(" + " + "(" + augmented.infiniteSPLsol[i] + ")" + "X" + i );
                }
            }
            System.out.println();
            return;
        }
        //Output Target
        System.out.println("Persamaan Regresi Linear Berganda yang Memenuhi adalah berikut.");
        System.out.print("Y = ");
        for (int i = 0; i < augmented.SPLsolution.length; i++){
            if(i == 0){
                System.out.print(Mbeta[i]);
                hasil+=Mbeta[i];
            }
            else{
                if(Mbeta[i] < 0){
                    System.out.print(" - " + -1*Mbeta[i] + "X" + i);
                }
                else{
                    System.out.print(" + " + Mbeta[i] + "X" + i);
                }
                hasil += Mbeta[i]*Target[i-1];
            }
            
        }
        System.out.println();
        System.out.println("Hampiran dari data adalah Y = " + hasil);
    }
    
    public static void MultipleQuadraticRegression() {    
        // Menghitung ukuran baru dari newMX (ukuran augmented matrix untuk regresi kuadratik)
        int newM = (m * (m + 1) / 2) + m + 1;  // Kombinasi kuadratik + variabel linier + konstanta
        
        Matrix newMX = new Matrix(n, newM);  // Matriks dengan ukuran n x newM
        
        // Mengisi xi^2 (kuadrat dari setiap variabel)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newMX.matrix[i][j] = matrix_X.matrix[i][j + 1] * matrix_X.matrix[i][j + 1];  // Mengambil dari matrix_X
            }
        }
        
        // Mengisi xi*xj (hasil kali silang antar variabel)
        int kolom = m;
        for (int baris = 0; baris < n; baris++) {
            kolom = m;  // Mulai dari setelah kolom kuadrat
            for (int i = 0; i < m; i++) {
                for (int j = i + 1; j < m; j++) {
                    newMX.matrix[baris][kolom] = matrix_X.matrix[baris][i + 1] * matrix_X.matrix[baris][j + 1];
                    kolom += 1;
                }
            }
        }
        
        // Mengisi xi (variabel original)
        for (int baris = 0; baris < n; baris++) {
            int j = 1;  // Mulai dari kolom kedua (kolom pertama adalah konstanta)
            for (int i = kolom; i < newM - 1; i++) {
                newMX.matrix[baris][i] = matrix_X.matrix[baris][j];
                j++;
            }
        }
        
        // Mengisi konstanta 1 untuk persamaan regresi (untuk beta_0)
        for (int baris = 0; baris < n; baris++) {
            newMX.matrix[baris][newM - 1] = 1;
        }
    
        double[] NewTarget = new double[newM];
        int idx = 0;
    
        // Mengisi kuadrat dari Target
        for(int i = 0; i < m; i++) {
            NewTarget[idx] = Target[i] * Target[i];
            idx++;
        }
    
        // Mengisi hasil kali silang
        for (int i = 0; i < m; i++) {
            for(int j = i+1; j < m; j++) {
                NewTarget[idx] = Target[i] * Target[j];
                idx++;
            }
        }
    
        // Mengisi variabel linier
        for(int i = 0; i < m; i++) {
            NewTarget[idx] = Target[i];
            idx++;
        }
        // Melakukan regresi sama seperti regresi linear (menggunakan newMX)
        Matrix mX_Tran = new Matrix(newM, n);  // Transpose dari newMX
        Matrix result1 = new Matrix(newM, newM);  // Hasil perkalian X * XT
        Matrix result2 = new Matrix(newM, 1);  // Hasil perkalian X * Y
    
        // Menghitung transpose dari newMX dan melakukan perkalian dengan newMX dan Y
        mX_Tran.matrix = newMX.transposeMatrix();
        result1.matrix = mX_Tran.multiplyMatrix(newMX);
        result2.matrix = mX_Tran.multiplyMatrix(matrix_Y);
    
        // Membuat augmented matrix
        Matrix augmented = new Matrix(result1.rowNum, result1.colNum + 1);
    
        for (int i = 0; i < result1.rowNum; i++) {
            for (int j = 0; j < result1.colNum; j++) {
                augmented.matrix[i][j] = result1.matrix[i][j];
            }
            augmented.matrix[i][result1.colNum] = result2.matrix[i][0];
        }
    
        // Menyelesaikan sistem persamaan linear (SPL) menggunakan metode Gauss
        Utility.roundMatrixElements(augmented);
        augmented.solveSPLGaussMethod(false);
    
        double[] Mbeta = augmented.SPLsolution;
        double hasil = 0;
    
        //atasi null
        if(Mbeta == null){
            System.out.println("Berikut persamaan parametrik.");
            System.out.print("Y = ");

            idx = 0;
            for(int i = 0; i < m; i++) {
                if(i == 0){
                    System.out.print("("+augmented.infiniteSPLsol[idx]+")X" + (i+1) +"^2");
                }
                else{
                    System.out.print(" + " + "(" + augmented.infiniteSPLsol[idx] + ")" + "X" + (i+1) + "^2");
                }
                idx++;
            }

            for (int i = 0; i < m; i++) {
                for(int j = i+1; j < m; j++) {
                    System.out.print(" + " + "(" + augmented.infiniteSPLsol[idx] + ")" + "X" + (i+1) + "X" +(j+1));
                    idx++;
                }
            }

            for(int i = 0; i < m; i++) {
                System.out.print(" + " + "(" + augmented.infiniteSPLsol[idx] + ")" + "X" + (i+1));
                idx++;
            }

            System.out.println(" + " + "(" + augmented.infiniteSPLsol[idx] + ")");
            System.out.println("Tidak ada solusi pasti, tidak bisa menentukan nilai Y.");
            System.out.println();
            return;
        }
    
        //Output Target
        System.out.println("Persamaan Regresi Kuadratik Berganda yang Memenuhi adalah berikut.");
        System.out.print("Y = ");

        idx = 0;

        // Mengisi kuadrat dari Target
        for(int i = 0; i < m; i++) {
            if(i == 0){
                System.out.print(Mbeta[idx] + "X" + (i+1) + "^2");
            }
            else{
                if(Mbeta[idx] < 0){
                    System.out.print(" - " + -1*Mbeta[idx] + "X" + (i+1) + "^2");
                }
                else{
                    System.out.print(" + " + Mbeta[idx] + "X" + (i+1) + "^2");
                }
            }
            hasil+= NewTarget[idx] * Mbeta[idx];
            idx++;
        }

        for (int i = 0; i < m; i++) {
            for(int j = i+1; j < m; j++) {
                if(Mbeta[idx] < 0){
                    System.out.print(" - " + -1*Mbeta[idx] + "X" + (i+1) + "X" + (j+1));
                }
                else{
                    System.out.print(" + " + Mbeta[idx] + "X" + (i+1) + "X" + (j+1));
                }
                hasil += NewTarget[idx]*Mbeta[idx];
                idx++;
            }
        }
        for(int i = 0; i < m; i++) {
            if(Mbeta[idx] < 0){
                System.out.print(" - " + -1*Mbeta[idx] + "X" + (i+1));
            }
            else{
                System.out.print(" + " + Mbeta[idx] + "X" + (i+1));
            }
            hasil += NewTarget[idx] * Mbeta[idx];
            idx++;
        }

        if(Mbeta[idx] < 0){
            System.out.print(" - " + -1*Mbeta[idx]);
        }
        else{
            System.out.print(" + " + Mbeta[idx]);
        }

        hasil += Mbeta[idx];

        System.out.println();
        System.out.println("Hampiran dari data adalah Y = " + hasil);
    }    
}