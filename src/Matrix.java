import java.util.*;
import java.io.*;
import java.lang.Math;

public class Matrix{
    double[][] matrix;
    int rowNum;
    int colNum;
    double[] SPLsolution;
    String[] infiniteSPLsol;
    boolean infiniteSol = false;
    double[][] infiniteKoef;
    double[] SPLParam; //solusi SPL Parametric, yang nilai seluruh parametric adalah 0;

    public Matrix(int rowNum, int colNum){
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.matrix = new double[rowNum][colNum];
    }

    public Matrix(){
        this.rowNum = 0;
        this.colNum = 0;
    }

    public void readMatrix(String type){
        // I. S. : Matrix belum terisi
        // F. S. : Matrix sudah terisi

        // Memilih metode input
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("Masukkan metode penginputan: ");
        System.out.println("1. Input manual");
        System.out.println("2. Input dari file .txt");
        int choice = Utility.getValidChoice(1, 2);

        // Mengisi matriks manual
        if (choice == 1){
            UI.Pesan();
            this.rowNum = 0;
            this.colNum = 0;

            String input1 = ""; // Inisialisasi input1
            String input2 = ""; // Inisialisasi input2

            do {
                System.out.print("Masukkan jumlah variabel x : ");
                input1 = scanner.nextLine();

                System.out.print("Masukkan jumlah data : ");
                input2 = scanner.nextLine();

                // Cek apakah kedua input adalah bilangan bulat
                if (!Utility.cek_int(input1) || !Utility.cek_int(input2)) {
                    System.out.println("Terdapat masukkan yang tidak valid.");
                    System.out.println("Periksa Kembali.");
                }
            } while (!Utility.cek_int(input1) || !Utility.cek_int(input2));

            this.rowNum = Integer.parseInt(input1);
            this.rowNum = Integer.parseInt(input2);
            this.matrix = new double[this.rowNum][this.colNum];
            
            for (int i = 0; i < this.rowNum; i++) {
                while (true) {
                    System.out.println("Masukkan elemen untuk baris " + (i + 1) + " (pisahkan dengan spasi): ");
                    String input = scanner.nextLine().trim();
                    String[] values = input.split("\\s+");

                    // Validasi jumlah elemen dalam baris
                    if (values.length != this.colNum) {
                        System.out.println("Kesalahan: Jumlah elemen dalam baris tidak sesuai. Harus ada " + this.colNum + " elemen.");
                        continue;
                    }

                    try {
                        // Mengisi matriks dengan nilai yang diinputkan
                        for (int j = 0; j < this.colNum; j++) {
                            this.matrix[i][j] = Double.parseDouble(values[j]);
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Kesalahan: Input harus berupa angka. Ulangi input.");
                    }
                }
            }
        // Mengisi matrix dengan file .txt
        } else if (choice == 2){
            System.out.print("Masukkan nama file: ");
            String filename = "test/" + scanner.nextLine();
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
                this.rowNum = row;

                // membaca jumlah kolom berdasarkan file.txt
                fileScanner = new Scanner(file);
                String line = fileScanner.nextLine().trim();
                String[] values = line.split("\\s+");
                fileScanner.close();
                this.colNum = values.length;

                this.matrix = new double[this.rowNum][this.colNum];

                // membaca isi matriks berdasarkan file.txt
                fileScanner = new Scanner(file);

                for (int i = 0; i < this.rowNum; i++) {
                    String line1 = fileScanner.nextLine().trim();
                    String[] values1 = line1.split("\\s+");

                    for (int j = 0; j < this.colNum; j++) {
                        this.matrix[i][j] = Double.parseDouble(values1[j]);
                    }
                }

                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Kesalahan: File tidak ditemukan.");
            } catch (NumberFormatException e) {
                System.out.println("Kesalahan: File berisi nilai yang bukan angka.");
            }
        } else {
            System.out.println("Kesalahan: Pilihan tidak valid.");
        }
    }  

    public void writeMatrix(){
        // I. S. : Matrix sudah terisi
        // F. S. : Matrix dituliskan pada layar
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                System.out.print(this.matrix[i][j] + " ");
            }
            System.out.println();
        }
    }  

    public double[][] addMatrix(Matrix other){
        // Menambahkan this.matrix dengan Matrix other
        if (this.rowNum != other.rowNum || this.colNum != other.colNum) {
            System.out.println("Kesalahan: Ukuran matriks tidak sama.");
            return null;
        }

        Matrix result = new Matrix(this.rowNum, this.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                result.matrix[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }

        return result.matrix;
    }

    public double[][] substractMatrix(Matrix other){
        // Mengurangi this.matrix dengan Matrix other
        if (this.rowNum != other.rowNum || this.colNum != other.colNum) {
            System.out.println("Kesalahan: Ukuran matriks tidak sama.");
            return null;
        }

        Matrix result = new Matrix(this.rowNum, this.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                result.matrix[i][j] = this.matrix[i][j] - other.matrix[i][j];
            }
        }

        return result.matrix;
    }

    public double[][] multiplyMatrix(Matrix other) {
        // Mengalikan this.matrix dengan Matrix other
        if (this.colNum != other.rowNum) {
            System.out.println("Kesalahan: Jumlah kolom matriks pertama tidak sama dengan jumlah baris matriks kedua.");
            return null;
        }
    
        Matrix result = new Matrix(this.rowNum, other.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < other.colNum; j++) {
                result.matrix[i][j] = 0; // Inisialisasi dengan nol
                for (int k = 0; k < this.colNum; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
                // Pembulatan hasil ke 3 angka di belakang koma
                result.matrix[i][j] = Math.round(result.matrix[i][j] * 1000.0) / 1000.0;
            }
        }
        
        return result.matrix;
    }
    

    public double[][] multiplyConstant(double constant){
        // Mengalikan this.matrix dengan konstanta
        Matrix result = new Matrix(this.rowNum, this.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                result.matrix[i][j] = this.matrix[i][j] * constant;
            }
        }

        return result.matrix;
    }

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

    public Matrix getMinor(int row, int col){
        // Menghasilkan matrix minor dari this.matrix
        Matrix minor = new Matrix(this.rowNum - 1, this.colNum - 1);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                if (i == row || j == col) {
                    continue;
                }

                int minorRow = i < row ? i : i - 1;
                int minorCol = j < col ? j : j - 1;
                minor.matrix[minorRow][minorCol] = this.matrix[i][j];
            }
        }

        return minor;
    }

    public double DeterminantUsingCofactor(){
        // Menghitung determinan dengan metode kofaktor
        if (this.rowNum != this.colNum) {
            System.out.println("Kesalahan: Matriks harus berbentuk persegi.");
            return 0;
        }

        if (this.rowNum == 1) {
            return this.matrix[0][0];
        }

        if (this.rowNum == 2) {
            return this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
        }

        double determinant = 0;
        for (int i = 0; i < this.rowNum; i++) {
            determinant += Math.pow(-1, i) * this.matrix[0][i] * this.getMinor(0, i).DeterminantUsingCofactor();
        }

        return determinant;
    }

    public double DeterminantUsingRowReduction(){
        // Menghitung determinan menggunakan OBE (Metode segitiga)
        if (this.rowNum != this.colNum) {
            System.out.println("Kesalahan: Matriks harus berbentuk persegi.");
            return 0;
        }
    
        Matrix upperTriangularMatrix = new Matrix(this.rowNum, this.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                upperTriangularMatrix.matrix[i][j] = this.matrix[i][j];
            }
        }
    
        int row = 0;
        int col = 0;
        int countTukar = 0;
        while (row < this.rowNum && col < this.colNum) {
            // Mencari baris dengan elemen terbesar pada kolom ke-col
            int rowTakNol = row;
            if (upperTriangularMatrix.matrix[row][col] == 0){
                for (int i = row +1; i < this.rowNum; i++){
                    if (upperTriangularMatrix.matrix[i][col] != 0){
                        rowTakNol = i;
                        break;
                    }
                }
            }
            
            // Menukar baris row dan rowTakNol jika diperlukan
            if (rowTakNol != row) {
                countTukar++;
                for (int i = 0; i < this.colNum; i++) {
                    double temp = upperTriangularMatrix.matrix[row][i];
                    upperTriangularMatrix.matrix[row][i] = upperTriangularMatrix.matrix[rowTakNol][i];
                    upperTriangularMatrix.matrix[rowTakNol][i] = temp;
                }
            }

            if (upperTriangularMatrix.matrix[row][col] == 0){
                col++;
                continue;
            } else {
                // Mengurangi baris-baris di bawah baris row
                for (int i = row + 1; i < this.rowNum; i++) {
                    double factor = upperTriangularMatrix.matrix[i][col] / upperTriangularMatrix.matrix[row][col];
                    for (int j = col; j < this.colNum; j++) {
                        upperTriangularMatrix.matrix[i][j] -= factor * upperTriangularMatrix.matrix[row][j];
                    }
                }
            }
            row++;
            col++;
        }
    
        double determinant = 1;
        for (int i = 0; i < this.rowNum; i++) {
            determinant *= upperTriangularMatrix.matrix[i][i];
        }
        
        // Aplikasikan perubahan tanda sesuai jumlah pertukaran baris
        determinant *= Math.pow(-1, countTukar);
    
        return determinant;
    }
    
    public double[][] InverseUsingAdjoin(){
        // Menghasilkan invers dari this.matrix mengunakan metode adjoint
        if (this.rowNum != this.colNum) {
            System.out.println("Kesalahan: Matriks harus berbentuk persegi.");
            return null;
        }

        double determinant = this.DeterminantUsingRowReduction();
        if (determinant == 0) {
            System.out.println("Kesalahan: Determinan matriks bernilai 0.");
            return null;
        }

        Matrix cofactorMatrix = new Matrix(this.rowNum, this.colNum);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum; j++) {
                cofactorMatrix.matrix[i][j] = Math.pow(-1, i + j) * this.getMinor(i, j).DeterminantUsingRowReduction();
            }
        }

        Matrix adjugateMatrix = new Matrix(this.rowNum, this.colNum);
        adjugateMatrix.matrix = cofactorMatrix.transposeMatrix();
        return adjugateMatrix.multiplyConstant(1 / determinant);
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

    public void solveManySolution(){
        this.infiniteKoef = new double[this.colNum - 1][this.colNum];
        this.SPLParam = new double[this.colNum];
        for (int i = 0; i < this.colNum - 1; i++){
            for (int j = 0; j < this.colNum; j++){
                if (i == j){
                    this.infiniteKoef[i][j] = 1;
                } else {
                    this.infiniteKoef[i][j] = 0;
                }
            }
        }
        
        this.infiniteSPLsol = new String[this.colNum - 1];
        boolean[] aVar = new boolean[this.colNum - 1];
        for (int i = 0; i < this.colNum - 1; i++){
            aVar[i] = true;
        }
        char[] param = new char[this.colNum - 1];

        for (int i = this.rowNum - 1; i >= 0; i--){
            for (int j = i; j < this.colNum - 1; j++){
                if (this.matrix[i][j] == 1){
                    aVar[j] = false;
                    this.infiniteKoef[j][j] = 0;
                    this.infiniteKoef[j][this.colNum - 1] = this.matrix[i][this.colNum - 1];
                    for (int k = j+1; k < this.colNum - 1; k++){
                        if (this.matrix[i][k] != 0){
                            if (aVar[k]){
                                this.infiniteKoef[j][k] -= this.matrix[i][k];
                            } else {
                                for (int l = k + 1; l <= this.colNum - 1; l++){
                                    if (this.infiniteKoef[k][l] != 0){
                                        this.infiniteKoef[j][l] -= this.matrix[i][k] * this.infiniteKoef[k][l];
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        int var = 0;
        for (int i = 0; i < this.colNum - 1; i++){
            if (aVar[i]){
                param[i] = (char) (97 + var);
                var ++;
            }
        }

        for (int i = 0; i < this.colNum - 1; i++){
            if (aVar[i]){
                this.infiniteSPLsol[i] = Character.toString(param[i]);//"x" + Integer.toString(i+1) + " = " + Character.toString(param[i]);
            } else {
                boolean isZero = true;
                for (int j = i + 1; j <= this.colNum - 1; j++){
                    if (this.infiniteKoef[i][j] != 0){
                        isZero = false;
                        break;
                    }
                }
                if (isZero){
                    this.infiniteSPLsol[i] = "0"; //"x" + Integer.toString(i+1) + " = 0";(sebelum diubah)
                } else {
                    String temp = "";
                    if (this.infiniteKoef[i][this.colNum - 1] != 0){
                        temp += Double.toString(this.infiniteKoef[i][this.colNum - 1]);
                    }
                    for (int j = i + 1; j < this.colNum - 1; j++){
                        if (this.infiniteKoef[i][j] != 0){
                            if (this.infiniteKoef[i][j] > 0){
                                temp += (temp.length() == 0 ? "" : " + ");
                                temp += (this.infiniteKoef[i][j] != 1.0 ? Double.toString(this.infiniteKoef[i][j]) : "");
                                temp += Character.toString(param[j]);
                            } else {
                                temp += "-";
                                temp += (this.infiniteKoef[i][j] != -1.0 ? Double.toString(-this.infiniteKoef[i][j]) : "");
                                temp += Character.toString(param[j]);
                            }
                        }
                    }
                    this.infiniteSPLsol[i] = temp; //"x" + Integer.toString(i+1) + " = " + temp; (sebelum diubah)
                }
            }
        }
    }   

    // Mencari solusi SPL menggunakan metode eliminasi Gauss
    public void solveSPLGaussMethod(boolean findSPL) {
        // I. S. : Matriks augmented sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar (penyelesaian menggunakan metode eliminasi Gauss)
        int row = 0;
        int col = 0;
        double epsilon = 1e-9;  // Batas toleransi untuk presisi angka sangat kecil
        
        while (row < this.rowNum && col < this.colNum - 1) {
            // Mencari baris dengan elemen terbesar pada kolom ke-col
            int maxRow = row;
            for (int i = row + 1; i < this.rowNum; i++) {
                if (Math.abs(this.matrix[i][col]) > Math.abs(this.matrix[maxRow][col])) {
                    maxRow = i;
                }
            }
    
            // Jika elemen terbesar pada kolom tersebut hampir 0, pindah ke kolom berikutnya
            if (Math.abs(this.matrix[maxRow][col]) < epsilon) {
                col++;
            } else {
                // Menukar baris row dan maxRow
                for (int i = 0; i < this.colNum; i++) {
                    double temp = this.matrix[row][i];
                    this.matrix[row][i] = this.matrix[maxRow][i];
                    this.matrix[maxRow][i] = temp;
                }
    
                // Mengurangi baris-baris di bawah baris row
                for (int i = row + 1; i < this.rowNum; i++) {
                    double factor = this.matrix[i][col] / this.matrix[row][col];
                    for (int j = col; j < this.colNum; j++) {
                        this.matrix[i][j] -= factor * this.matrix[row][j];
                    }
                }
    
                // Membentuk satu utama pada baris row
                double pivot = this.matrix[row][col];
                for (int j = col; j < this.colNum; j++) {
                    this.matrix[row][j] /= pivot;
                    if (Math.abs(this.matrix[row][j]) < epsilon) {
                        this.matrix[row][j] = 0.0;  // Menghilangkan angka mendekati nol
                    }
                }
    
                row++;
                col++;
            }
        }

        // Mengecek apakah SPL memiliki solusi
        boolean hasSolution = true;
        boolean infiniteSolution = false;
    
        for (int i = 0; i < this.rowNum; i++) {
            boolean allZero = true;
            for (int j = 0; j < this.colNum - 1; j++) {
                if (Math.abs(this.matrix[i][j]) > epsilon) {
                    allZero = false;
                    break;
                }
            }
    
            if (allZero && Math.abs(this.matrix[i][this.colNum - 1]) > epsilon) {
                hasSolution = false;
                System.out.println("SPL tidak memiliki solusi.");
                break;
            } else if (allZero && Math.abs(this.matrix[i][this.colNum - 1]) < epsilon) {
                infiniteSolution = true;
            }
        }
    
        if (hasSolution) {
            if (infiniteSolution) {
                this.infiniteSol = true;
                this.solveManySolution();
            } else {
                // Mencari solusi SPL (back substitution)
                this.SPLsolution = new double[this.colNum - 1];
                for (int i = this.rowNum - 1; i >= 0; i--) {
                    double sum = 0;
                    for (int j = i + 1; j < this.colNum - 1; j++) {
                        sum += this.matrix[i][j] * this.SPLsolution[j];
                    }
                    this.SPLsolution[i] = (this.matrix[i][this.colNum - 1] - sum) / this.matrix[i][i];
                }
                Utility.roundArrayElements(SPLsolution);
            }
            if(findSPL){
                printSol();
            }
        }
    }    

    // Mencari solusi SPL menggunakan metode gauss-jordan
    public void solveSPLGaussJordanMethod(){
        // I. S. : Matriks augmented sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar (penyelesaian menggunakan metode eliminasi Gauss-Jordan)
        int row = 0;
        int col = 0;
        while (row < this.rowNum && col < this.colNum - 1) {
            // Mencari baris dengan elemen terbesar pada kolom ke-col
            int maxRow = row;
            for (int i = row + 1; i < this.rowNum; i++) {
                if (Math.abs(this.matrix[i][col]) > Math.abs(this.matrix[maxRow][col])) {
                    maxRow = i;
                }
            }

            if (this.matrix[maxRow][col] == 0){
                col ++;
            } else {
                // Menukar baris row dan maxRow
                for (int i = 0; i < this.colNum; i++) {
                    double temp = this.matrix[row][i];
                    this.matrix[row][i] = this.matrix[maxRow][i];
                    this.matrix[maxRow][i] = temp;
                }
    
                // Membagi baris row dengan elemen pada kolom ke-col
                double factor = this.matrix[row][col];
                for (int i = col; i < this.colNum; i++) {
                    this.matrix[row][i] /= factor;
                }
    
                // Mengurangi baris-baris selain row
                for (int i = 0; i < this.rowNum; i++) {
                    if (i == row) {
                        continue;
                    }
    
                    factor = this.matrix[i][col];
                    for (int j = col; j < this.colNum; j++) {
                        this.matrix[i][j] -= factor * this.matrix[row][j];
                    }
                }
    
                row++;
                col++;
            }
        }

        // Mengecek apakah SPL memiliki solusi
        boolean hasSolution = true;
        boolean infiniteSolution = false;
        for (int i = 0; i < this.rowNum; i++) {
            boolean allZero = true;
            for (int j = 0; j < this.colNum - 1; j++) {
                if (this.matrix[i][j] != 0) {
                    allZero = false;
                    break;
                }
            }

            if (allZero && this.matrix[i][this.colNum - 1] != 0) {
                hasSolution = false;
                System.out.println("SPL tidak memiliki solusi.");
                break;
            } else if (allZero && this.matrix[i][this.colNum - 1] == 0) {
                infiniteSolution = true;
                break;
            }
        }

        if (hasSolution){
            if (infiniteSolution){
                this.infiniteSol = true;
                this.solveManySolution();
            } else {
                // Mencari solusi SPL
                this.SPLsolution = new double[this.colNum - 1];
                for (int i = 0; i < this.rowNum; i++) {
                    this.SPLsolution[i] = this.matrix[i][this.colNum - 1];
                }
            }
            Utility.roundArrayElements(SPLsolution);
            printSol();
        }
    }

    public void solveSPLInversMatrix(){
        // I. S. : Matriks augmented sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar (penyelesaian menggunakan metode invers matriks)
        Matrix A = new Matrix(this.rowNum, this.colNum - 1);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum - 1; j++) {
                A.matrix[i][j] = this.matrix[i][j];
            }
        }

        Matrix B = new Matrix(this.rowNum, 1);
        for (int i = 0; i < this.rowNum; i++) {
            B.matrix[i][0] = this.matrix[i][this.colNum - 1];
        }

        Matrix inverseA = new Matrix(this.rowNum, this.colNum - 1);
        inverseA.matrix = A.InverseUsingGaussJordan();
        
        if(inverseA.matrix == null){ // agar tidak error kebawahnya.
            System.out.println("Tidak dapat menemukan solusi SPL.");
            return;
        }

        Matrix X = new Matrix(this.rowNum, 1);
        X.matrix =  inverseA.multiplyMatrix(B);
        this.SPLsolution = new double[this.rowNum];
        for (int i = 0; i < this.rowNum; i++){
            this.SPLsolution[i] = X.matrix[i][0];
        }
        Utility.roundArrayElements(SPLsolution);
        printSol();
    }

    public void solveSPLUsingCramer(){
        // I. S. : Matriks augmented sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar (penyelesaian menggunakan metode eliminasi Gauss)
        Matrix determinant = new Matrix(this.rowNum, this.colNum - 1);
        for (int i = 0; i < this.rowNum; i++) {
            for (int j = 0; j < this.colNum - 1; j++) {
                determinant.matrix[i][j] = this.matrix[i][j];
            }
        }

        double determinantA = determinant.DeterminantUsingCofactor();
        // System.out.println("Determinan matriks A: " + determinantA);
        if (determinantA == 0) {
            System.out.println("Kesalahan: Determinan matriks A bernilai 0.");
        } else {
            this.SPLsolution = new double[this.rowNum];
            for (int i = 0; i < this.rowNum; i++) {
                Matrix Ai = new Matrix(this.rowNum, this.colNum - 1);
                for (int j = 0; j < this.rowNum; j++) {
                    for (int k = 0; k < this.colNum - 1; k++) {
                        Ai.matrix[j][k] = this.matrix[j][k];
                    }
                }

                for (int j = 0; j < this.rowNum; j++) {
                    Ai.matrix[j][i] = this.matrix[j][this.colNum - 1];
                }

                double determinantAi = Ai.DeterminantUsingCofactor();
                // System.out.println("Determinan matriks A" + (i + 1) + ": " + determinantAi);
                this.SPLsolution[i] = determinantAi / determinantA;
            }
        }
        if(determinantA == 0){
            System.out.println("Tidak dapat menemukan solusi SPL.");
            return;
        }
        Utility.roundArrayElements(SPLsolution);
        printSol();
    }

    public void printSol(){
        // I. S. : Solusi SPL sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar
        if (this.infiniteSol){
            for (int i = 0; i < this.colNum - 1; i ++){
                System.out.println(this.infiniteSPLsol[i]);
            }
        } else {
            for (int i = 0; i < this.rowNum; i++) {
                System.out.println("x" + (i + 1) + " = " + this.SPLsolution[i]);
            }
        }
    }
}