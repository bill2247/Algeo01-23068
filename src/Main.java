import java.util.*;
//import java.io.*;

public class Main {
    static Scanner scan = new Scanner(System.in); // Inisialisasi Scanner dengan System.in 
    
    public static void main(String[] args) {
        StringBuilder output;
        output = new StringBuilder();

        while(true){
            int choice = 0;
            UI.Menu();  
            
            choice = Utility.getValidChoice(1,8);

            if (choice == 1){
                UI.SPL_Menu();
                int method = Utility.getValidChoice(1, 4);
                Matrix SPL = new Matrix();
                SPL.readMatrix("SPL");
                if (method == 1){
                    SPL.solveSPLGaussMethod(true);
                } else if (method == 2){
                    SPL.solveSPLGaussJordanMethod();
                } else if (method == 3){
                    SPL.solveSPLInversMatrix();
                } else if (method == 4){
                    SPL.solveSPLUsingCramer();
                }
                Utility.validasiFile(SPL.output.toString());
                System.out.println();
            }
            else if(choice == 2){
                String s;
                UI.Det_Menu();
                int method = Utility.getValidChoice(1, 2);
                Matrix mat = new Matrix();
                mat.readMatrix("Determinan");
                double determinant = 0;
                if (method == 1){
                    determinant = mat.DeterminantUsingCofactor();
                } else if (method == 2){
                    determinant = mat.DeterminantUsingRowReduction();
                }
                System.out.println("Determinan matriks: " + determinant);
                s = "Determianan matriks: "+determinant;
                Utility.validasiFile(s);
                System.out.println();
            }
            else if (choice == 3){
                UI.inverse_Menu();
                int method = Utility.getValidChoice(1, 2);

                Matrix mat = new Matrix();
                mat.readMatrix("Invers");
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
                            output.append(inverseMatrix[i][j]).append(" ");
                        }
                        System.out.println();
                        output.append("\n");
                    }
                    Utility.validasiFile(output.toString());
                } else if (inverseMatrix == null){
                    System.out.println("Matriks tidak memiliki invers.");
                    Utility.validasiFile("Matriks tidak memiliki invers.");
                }
                System.out.println();
            }
            else if(choice == 4){
                Interpolasi interpolasi = new Interpolasi();
                interpolasi.readInterpolasi();
                interpolasi.solveInterpolasi();
                interpolasi.printSol();
                interpolasi.printfc();
                Utility.validasiFile(interpolasi.output.toString());
                System.out.println();
            }
            else if(choice == 5){
                BicubicSplineInterpolation.Bicubic();
                Utility.validasiFile(BicubicSplineInterpolation.output.toString());
            }
            else if (choice == 6){
                UI.reg_Menu();
                int method = Utility.getValidChoice(1, 2);
                Regresi.readMatrix();
                if(method == 1){
                    Regresi.MultipleLinearRegression();
                }
                else{
                    Regresi.MultipleQuadraticRegression();
                }
                Utility.validasiFile(Regresi.output.toString());
                System.out.println();
            }
            else if(choice == 7){
                System.out.println("Belum implemen bang, moga jadi wkwk");
            }
            else{
                break;
            }
        }
    }
}


/*
 * jangan lupa implemen kalau dia bisa balik, jadi nanti ada 3 menu
 */