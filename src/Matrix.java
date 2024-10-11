import java.util.*;
import java.io.*;
import java.lang.Math;

public class Matrix{
    double[][] matrix;
    int rowNum;
    int colNum;
    double[] SPLsolution;

    public Matrix(int rowNum, int colNum){
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.matrix = new double[rowNum][colNum];
    }

    public Matrix(){
        this.rowNum = 0;
        this.colNum = 0;
    }

    public void readMatrix(){
        // I. S. : Matrix belum terisi
        // F. S. : Matrix sudah terisi

        // Memilih metode input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Masukkan metode penginputan: ");
        System.out.println("1. Input manual");
        System.out.println("2. Input dari file .txt");
        System.out.print("Pilihan: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Membersihkan buffer

        // Mengisi matriks manual
        if (choice == 1){
            this.rowNum = 0;
            this.colNum = 0;
            System.out.print("Masukkan jumlah baris: ");
            this.rowNum = scanner.nextInt();
            System.out.print("Masukkan jumlah kolom: ");
            this.colNum = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer
            this.matrix = new double[this.rowNum][this.colNum];

            for (int i = 0; i < this.rowNum; i++) {
                while (true) {
                    System.out.println("Masukkan elemen untuk baris " + (i + 1) + " (pisahkan dengan spasi): ");
                    String input = scanner.nextLine().trim();
                    String[] values = input.split("\\s+"); // Memisahkan elemen dengan spasi

                    // Validasi jumlah elemen dalam baris
                    if (values.length != this.colNum) {
                        System.out.println("Kesalahan: Jumlah elemen dalam baris tidak sesuai. Harus ada " + this.colNum + " elemen.");
                        continue; // Ulangi input baris jika jumlah elemen tidak sesuai
                    }

                    try {
                        // Mengisi matriks dengan nilai yang diinputkan
                        for (int j = 0; j < this.colNum; j++) {
                            this.matrix[i][j] = Double.parseDouble(values[j]);
                        }
                        break; // Keluar dari while loop jika input baris valid
                    } catch (NumberFormatException e) {
                        // Menangani kesalahan jika ada nilai yang bukan angka
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
        scanner.close();
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
            int maxRow = row;
            for (int i = row + 1; i < this.rowNum; i++) {
                if (Math.abs(upperTriangularMatrix.matrix[i][col]) > Math.abs(upperTriangularMatrix.matrix[maxRow][col])) {
                    maxRow = i;
                }
            }
            
            // Menukar baris row dan maxRow jika diperlukan
            if (maxRow != row) {
                countTukar++;
                for (int i = 0; i < this.colNum; i++) {
                    double temp = upperTriangularMatrix.matrix[row][i];
                    upperTriangularMatrix.matrix[row][i] = upperTriangularMatrix.matrix[maxRow][i];
                    upperTriangularMatrix.matrix[maxRow][i] = temp;
                }
            }
    
            // Mengurangi baris-baris di bawah baris row
            for (int i = row + 1; i < this.rowNum; i++) {
                double factor = upperTriangularMatrix.matrix[i][col] / upperTriangularMatrix.matrix[row][col];
                for (int j = col; j < this.colNum; j++) {
                    upperTriangularMatrix.matrix[i][j] -= factor * upperTriangularMatrix.matrix[row][j];
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

    // Mencari solusi SPL menggunakan metode eliminasi Gauss
    public void solveSPLGaussMethod(){
        // I. S. : Matriks augmented sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar (penyelesaian menggunakan metode eliminasi Gauss)
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

            row++;
            col++;
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
                System.out.println("SPL memiliki banyak solusi.");
                break;
            }
        }

        if (hasSolution){
            if (infiniteSolution){
                // Menampilkan solusi parametrik
            } else {
                // Mencari solusi SPL
                this.SPLsolution = new double[this.colNum - 1];
                for (int i = this.rowNum - 1; i >= 0; i--) {
                    double sum = 0;
                    for (int j = i + 1; j < this.colNum - 1; j++) {
                        sum += this.matrix[i][j] * this.SPLsolution[j];
                    }
                    this.SPLsolution[i] = (this.matrix[i][this.colNum - 1] - sum) / this.matrix[i][i];
                }
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
                System.out.println("SPL memiliki banyak solusi.");
                break;
            }
        }

        if (hasSolution){
            if (infiniteSolution){
                // Menampilkan solusi parametrik
            } else {
                // Mencari solusi SPL
                this.SPLsolution = new double[this.colNum - 1];
                for (int i = 0; i < this.rowNum; i++) {
                    this.SPLsolution[i] = this.matrix[i][this.colNum - 1];
                }
            }
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
    
        Matrix X = new Matrix(this.rowNum, 1);
        X.matrix =  inverseA.multiplyMatrix(B);
        this.SPLsolution = new double[this.rowNum];
        for (int i = 0; i < this.rowNum; i++){
            this.SPLsolution[i] = X.matrix[i][0];
        }
        
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
    }

    public void printSol(){
        // I. S. : Solusi SPL sudah terisi
        // F. S. : Solusi SPL ditampilkan pada layar
        for (int i = 0; i < this.rowNum; i++) {
            System.out.println("x" + (i + 1) + " = " + this.SPLsolution[i]);
        }
    }

    public static void main(String[] args) {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit){
            System.out.println("Pilih aksi yang ingin dilakukan: ");
            System.out.println("1. Menyelesaikan SPL");
            System.out.println("2. Menghitung determinan matriks");
            System.out.println("3. Menghitung invers matriks");
            System.out.print("Pilihan: ");
            
            int choice = scanner.nextInt();
            if (choice == 1){
                System.out.println("Pilih metode penyelesaian SPL: ");
                System.out.println("1. Metode eliminasi Gauss");
                System.out.println("2. Metode eliminasi Gauss-Jordan");
                System.out.println("3. Metode invers matriks");
                System.out.println("4. Metode Cramer");
                System.out.print("Pilihan: ");
                int method = scanner.nextInt();
                Matrix SPL = new Matrix();
                SPL.readMatrix();
                if (method == 1){
                    SPL.solveSPLGaussMethod();
                } else if (method == 2){
                    SPL.solveSPLGaussJordanMethod();
                } else if (method == 3){
                    SPL.solveSPLInversMatrix();
                } else if (method == 4){
                    SPL.solveSPLUsingCramer();
                }
                // menampilkan hasil solusi SPL
                SPL.printSol();
            } else if (choice == 2){
                System.out.println("Masukkan metode perhitungan determinan: ");
                System.out.println("1. Metode kofaktor");
                System.out.println("2. Metode eliminasi Gauss");
                System.out.print("Pilihan: ");
                int method = scanner.nextInt();
                Matrix mat = new Matrix();
                mat.readMatrix();
                double determinant = 0;
                if (method == 1){
                    determinant = mat.DeterminantUsingCofactor();
                } else if (method == 2){
                    determinant = mat.DeterminantUsingRowReduction();
                }
                System.out.println("Determinan matriks: " + determinant);

            } else if (choice == 3){
                System.out.println("Masukkan metode perhitungan invers: ");
                System.out.println("1. Metode adjoint");
                System.out.println("2. Metode Gauss-Jordan");
                System.out.print("Pilihan: ");
                int method = scanner.nextInt();
                Matrix mat = new Matrix();
                mat.readMatrix();
                double[][] inverseMatrix = null;
                if (method == 1){
                    inverseMatrix = mat.InverseUsingAdjoin();
                } else if (method == 2){
                    inverseMatrix = mat.InverseUsingGaussJordan();
                }
                if (inverseMatrix != null){
                    System.out.println("Matriks invers:");
                    for (int i = 0; i < mat.rowNum; i++){
                        for (int j = 0; j < mat.colNum; j++){
                            System.out.print(inverseMatrix[i][j] + " ");
                        }
                        System.out.println();
                    }
                } else if (inverseMatrix == null){
                    System.out.println("Matriks tidak memiliki invers.");
                }
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        
            System.out.print("Apakah ingin keluar? (y/n) ");
            String exitChoice = scanner.next();
            if (exitChoice.equals("y")){
                exit = true;
            }
        }
    scanner.close();
    }
}

