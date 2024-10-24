public class UI {
    public static void Menu() {
        MenuArt();
        System.out.println("1. System of Linear Equations");
        System.out.println("2. Determinant");
        System.out.println("3. Inverse Matrix");
        System.out.println("4. Polynomial Interpolation");
        System.out.println("5. Bicubic Spline Interpolation");
        System.out.println("6. Multiple Linear and Quadratic Regression");
        System.out.println("7. Exit");
    }    

    public static void Pesan(){
        System.out.println("==========================================");
        System.err.println("Perhatikan format masukkan untuk matriks,");
        System.out.println("semisal terdapat 2 variable (x1,x2), dan");
        System.out.println("3 tiga persamaan, maka masukan yang sesuai");
        System.out.println("adalah sebagai berikut.");
        System.out.println("x[1][1] x[1][2] y1");
        System.out.println("x[2][1] x[2][2] y1");
        System.out.println("x[3][1] x[3][2] y1");
        System.out.println("==========================================");
        System.out.println();
    }

    public static void SPL_Menu() {
        System.out.println("1. Gauss Elimination Method");
        System.out.println("2. Gauss-Jordan Elimination Method");
        System.out.println("3. Inverse Matrix Method");
        System.out.println("4. Cramer's Rule");
    }

    public static void Det_Menu(){
        System.out.println("1. Cofactor Method");
        System.out.println("2. Gauss Elimination Method");
    }
    
    public static void inverse_Menu(){
        System.out.println("1. Adjoint Method");
        System.out.println("2. Gauss-Jordan Method");
    }

    public static void reg_Menu(){
        System.out.println("1. Multiple Linear Regression");
        System.out.println("2. Multiple Quadratic Regression");
    }

    public static void MenuArt(){
        System.out.println(
        "+========================================+\n"+
        "| ███╗   ███╗███████╗███╗   ██╗██╗   ██╗ |\n"+
        "| ████╗ ████║██╔════╝████╗  ██║██║   ██║ |\n"+
        "| ██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║ |\n"+
        "| ██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║ |\n"+
        "| ██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝ |\n"+
        "| ╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝  |\n"+
        "+========================================+\n"
        );
    }


}
