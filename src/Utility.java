import java.util.*;
import java.io.*;

public class Utility {  
    static Scanner scan = new Scanner(System.in);

    public static boolean cek_int(String a) {
        if (a.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < a.length(); i++) {
            if (!Character.isDigit(a.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }

    public static int getValidChoice(int min, int max) {
        int choice;
        String input;
        
        do {
            System.out.print("Masukkan pilihan (" + min + "-" + max + "): ");
            input = scan.nextLine();
    
            while (!cek_int(input)) {
                System.out.println("Pastikan masukkan sesuai!!");
                System.out.print("Masukkan pilihan (" + min + "-" + max + "): ");
                input = scan.nextLine();
            }
    
            choice = Integer.parseInt(input);
    
            if (choice < min || choice > max) {
                System.out.println("Pastikan masukkan sesuai!!");
            }
        } while (choice < min || choice > max);
    
        return choice;
    }

    // Prosedur untuk menyimpan output ke dalam file
    public static void saveOutputToFile(String output) {
        // Meminta nama file dari keyboard
        System.out.print("Masukkan nama file (dengan ekstensi, contoh: output.txt): ");
        String fileName = scan.nextLine();
        
        // Membuat path file di dalam folder 'test'
        String filePath = "test/" + fileName;

        // Membuat objek File
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!file.exists()) {
                // Jika file tidak ada, buat file baru dan tulis output
                file.getParentFile().mkdirs(); // Membuat folder jika belum ada
                file.createNewFile();
                System.out.println("File baru dibuat: " + filePath);
                writer.write(output);
            } else {
                // Jika file sudah ada, tambahkan output di akhir file
                System.out.print("File sudah ada, tambahkan output ke file? (Y/N): ");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("Y")) {
                    writer.newLine();
                    writer.write(output);
                    System.out.println("Output berhasil ditambahkan ke file: " + filePath);
                } else {
                    System.out.println("Operasi dibatalkan.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    //pembulatan
    public static void roundMatrixElements(Matrix M) {
        for (int i = 0; i < M.rowNum; i++) {
            for (int j = 0; j < M.colNum; j++) {
                M.matrix[i][j] = Math.round(M.matrix[i][j] * 10000000.0) / 10000000.0;
            }
        }
    }

    public static void roundArrayElements(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.round(array[i] * 10000000.0) / 10000000.0;
        }
    }

    public static double roundElmt(double M){
        return Math.round(M*10000000.0) / 10000000.0;
    }

}
