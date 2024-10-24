import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ResizeImage {
    private int width;
    private int height;

    public ResizeImage(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }    




    static double[][] XD = {
        {0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, -8, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 16, -40, 32, -8, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, -8, 24, -24, 8, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, -8, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0},
        {0, -4, 0, 0, -4, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0},
        {0, 32, -20, 0, 8, -4, -4, 0, 0, -24, 16, -4, 0, 0, 0, 0},
        {0, -20, 12, 0, -4, 0, 4, 0, 0, 16, -12, 4, 0, 0, 0, 0},
        {0, 16, 0, 0, 0, -40, 0, 0, 0, 32, 0, 0, 0, -8, 0, 0},
        {0, 8, 0, 0, 32, -4, -24, 0, -20, -4, 0, 0, 0, 0, -4, 0},
        {0, -64, 40, 0, -64, 96, -68, 24, 40, -68, -16, -16, 0, 24, -16, 0},
        {0, 40, -24, 0, 32, -52, 52, -24, -20, 40, 16, 16, 0, -16, 12, 0},
        {0, -8, 0, 0, 0, 24, 0, 0, 0, -24, 0, 0, 0, 8, 0, 4},
        {0, -4, 0, 0, -20, 0, 16, 0, 12, 4, -12, 0, 0, 0, 4, -4},
        {0, 32, -20, 0, 40, -52, 40, -16, -24, 52, -52, 12, 0, -24, 16, -4},
        {0, -20, 12, 0, -20, 28, -32, 16, 12, -32, 40, -12, 0, 16, -12, 4}
    };

    // Fungsi untuk membaca gambar dan menyimpan nilai RGB ke dalam matriks
    public static int[][][] readImage(String fileName) {
        try {
            // Load gambar dari file
            File inputFile = new File("./test/"+fileName);
            BufferedImage image = ImageIO.read(inputFile);
            
            // Dapatkan lebar dan tinggi gambar
            int width = image.getWidth();
            int height = image.getHeight();
            
            // Buat matriks untuk menyimpan nilai RGB
            int[][][] rgbMatrix = new int[height][width][3]; // [height][width][3] untuk Red, Green, Blue
            
            // Simpan nilai RGB ke dalam matriks
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Ambil nilai warna pixel
                    int rgb = image.getRGB(x, y);
                    
                    // Ekstrak nilai Red, Green, dan Blue
                    rgbMatrix[y][x][0] = (rgb >> 16) & 0xFF;   // Red
                    rgbMatrix[y][x][1] = (rgb >> 8) & 0xFF;    // Green
                    rgbMatrix[y][x][2] = rgb & 0xFF;            // Blue
                }
            }
            return rgbMatrix; // Kembalikan matriks RGB
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Kembalikan null jika terjadi kesalahan
        }
    }


    public static int bicubic(Matrix matrixRGBtemp, double a, double b){
        Matrix A_inv = new Matrix(16, 16);
        A_inv.matrix = XD;
        // Menggunakan nilai dari matrixRGBtemp
        double[][] matrix = new double[4][4];
        matrix[1][1] = matrixRGBtemp.matrix[(int) Math.floor(a)][(int) Math.floor(b)];
        matrix[1][2] = matrixRGBtemp.matrix[(int) Math.floor(a)][(int) Math.ceil(b)];
        matrix[2][1] = matrixRGBtemp.matrix[(int) Math.ceil(a)][(int) Math.floor(b)];
        matrix[2][2] = matrixRGBtemp.matrix[(int) Math.ceil(a)][(int) Math.ceil(b)];

        matrix[0][0] = matrixRGBtemp.matrix[(int) Math.floor(a) - 1][(int) Math.floor(b) - 1];
        matrix[0][1] = matrixRGBtemp.matrix[(int) Math.floor(a) - 1][(int) Math.floor(b)];
        matrix[0][2] = matrixRGBtemp.matrix[(int) Math.floor(a) - 1][(int) Math.ceil(b)];
        matrix[0][3] = matrixRGBtemp.matrix[(int) Math.floor(a) - 1][(int) Math.ceil(b) + 1];

        matrix[1][0] = matrixRGBtemp.matrix[(int) Math.floor(a)][(int) Math.floor(b) - 1];
        matrix[1][3] = matrixRGBtemp.matrix[(int) Math.floor(a)][(int) Math.ceil(b) + 1];
        matrix[2][0] = matrixRGBtemp.matrix[(int) Math.ceil(a)][(int) Math.floor(b) - 1];
        matrix[2][3] = matrixRGBtemp.matrix[(int) Math.floor(a)][(int) Math.floor(b) +1];

        matrix[3][0] = matrixRGBtemp.matrix[(int) Math.ceil(a) + 1][(int) Math.floor(b) - 1];
        matrix[3][1] = matrixRGBtemp.matrix[(int) Math.ceil(a) + 1][(int) Math.floor(b)];
        matrix[3][2] = matrixRGBtemp.matrix[(int) Math.ceil(a) + 1][(int) Math.ceil(b)];
        matrix[3][3] = matrixRGBtemp.matrix[(int) Math.ceil(a) + 1][(int) Math.ceil(b) + 1];
        
        double[] values = new double[16];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                values[index++] = matrix[i][j];
            }
        }
        Matrix coefficients = BicubicSplineInterpolation.getCoefficients(values, A_inv);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                coefficients.matrix[i][j] /= 16;
            }
        }
        // Menghitung hasil interpolasi bicubic spline pada titik (a, b)
        int result = (int) BicubicSplineInterpolation.bicubicInterpolation(coefficients, a, b);

        return result;
    }
    



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file gambar: ");
        String imageName = scanner.nextLine();

        // Panggil fungsi untuk membaca gambar dan mendapatkan matriks RGB
        int[][][] rgbMatrix = readImage(imageName);

        if (rgbMatrix != null) {
            System.out.println("Matriks RGB berhasil dibaca dan disimpan.");
        }

        ResizeImage sizeImage = new ResizeImage("./test/" + imageName);
        int width = sizeImage.getWidth();
        int height = sizeImage.getHeight();

        System.out.print("Masukkan skala heigth : ");
        double heightSkala = scanner.nextDouble();
        System.out.print("Masukkan skala width : ");
        double widthSkala = scanner.nextDouble(); 

        

        Matrix matrixRtemp = new Matrix(height+2, width+2);
        Matrix matrixGtemp = new Matrix(height+2, width+2);
        Matrix matrixBtemp = new Matrix(height+2, width+2);
        double[][] matrixRtem = new double[height+2][width+2];
        double[][] matrixGtem = new double[height+2][width+2];
        double[][] matrixBtem = new double[height+2][width+2];
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) { 
                matrixRtem[i][j] = rgbMatrix[i-1][j-1][0];
                matrixGtem[i][j] = rgbMatrix[i-1][j-1][1];
                matrixBtem[i][j] = rgbMatrix[i-1][j-1][2];
            }
        }
        for (int i = 1; i <= height; i++) {
                matrixRtem[i][width+1] = rgbMatrix[i-1][width-1][0];
                matrixGtem[i][width+1] = rgbMatrix[i-1][width-1][1];
                matrixBtem[i][width+1] = rgbMatrix[i-1][width-1][2];
        }
        for (int i = 1; i <= height; i++) {
                matrixRtem[i][0] = rgbMatrix[i-1][0][0];
                matrixGtem[i][0] = rgbMatrix[i-1][0][1];
                matrixBtem[i][0] = rgbMatrix[i-1][0][2];
        }

        for (int j = 1; j <= width; j++) {
                matrixRtem[height+1][j] = rgbMatrix[height-1][j-1][0];
                matrixGtem[height+1][j] = rgbMatrix[height-1][j-1][1];
                matrixBtem[height+1][j] = rgbMatrix[height-1][j-1][2];
        }
        for (int j = 1; j <= width; j++) {
                matrixRtem[0][j] = rgbMatrix[0][j-1][0];
                matrixGtem[0][j] = rgbMatrix[0][j-1][1];
                matrixBtem[0][j] = rgbMatrix[0][j-1][2];
        }
        matrixRtem[0][0] = rgbMatrix[0][0][0];
        matrixGtem[0][0] = rgbMatrix[0][0][1];
        matrixBtem[0][0] = rgbMatrix[0][0][2];

        matrixRtem[height+1][0] = rgbMatrix[height-1][0][0];
        matrixGtem[height+1][0] = rgbMatrix[height-1][0][1];
        matrixBtem[height+1][0] = rgbMatrix[height-1][0][2];

        matrixRtem[0][width+1] = rgbMatrix[0][width-1][0];
        matrixGtem[0][width+1] = rgbMatrix[0][width-1][1];
        matrixBtem[0][width+1] = rgbMatrix[0][width-1][2];

        matrixRtem[height+1][width+1] = rgbMatrix[height-1][width-1][0];
        matrixGtem[height+1][width+1] = rgbMatrix[height-1][width-1][1];
        matrixBtem[height+1][width+1] = rgbMatrix[height-1][width-1][2];
        

        matrixRtemp.matrix = matrixRtem;
        matrixGtemp.matrix = matrixGtem;
        matrixBtemp.matrix = matrixBtem;
  
        int heightBaru = (int) (height * heightSkala);
        int widthBaru = (int) (width * widthSkala);
        System.out.println("Ukuran image menjadi: "+heightBaru+"*"+widthBaru);
        Matrix redMatrix = new Matrix(heightBaru, widthBaru);
        Matrix greenMatrix = new Matrix(heightBaru, widthBaru);
        Matrix blueMatrix = new Matrix(heightBaru, widthBaru);
        
        for (int i = 0; i < heightBaru; i++) {
            for (int j = 0; j < widthBaru; j++) {
                double titikI = (i*((height-1)/(double)(heightBaru-1)))+1;
                double titikJ = (j*((width-1)/(double)(widthBaru-1)))+1;
                redMatrix.matrix[i][j] = bicubic(matrixRtemp, titikI, titikJ);
                greenMatrix.matrix[i][j] = bicubic(matrixGtemp, titikI, titikJ);
                blueMatrix.matrix[i][j] = bicubic(matrixBtemp, titikI, titikJ);
            }
        }

        // Gabungkan ke dalam matriks 3 dimensi rgbNew
        int[][][] rgbNew = new int[heightBaru][widthBaru][3];
        for (int y = 0; y < heightBaru; y++) {
            for (int x = 0; x < widthBaru; x++) {
                rgbNew[y][x][0] = (int) redMatrix.matrix[y][x];   // Red
                rgbNew[y][x][1] = (int) greenMatrix.matrix[y][x]; // Green
                rgbNew[y][x][2] = (int) blueMatrix.matrix[y][x];  // Blue
            }
        }

        // Buat gambar baru dari rgbNew
        BufferedImage outputImage = new BufferedImage(widthBaru, heightBaru, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < heightBaru; y++) {
            for (int x = 0; x < widthBaru; x++) {
                // Gabungkan nilai RGB ke dalam integer
                int rgb = (rgbNew[y][x][0] << 16) | (rgbNew[y][x][1] << 8) | rgbNew[y][x][2];
                outputImage.setRGB(x, y, rgb); // Set pixel
            }
        }


        // Simpan gambar ke file output
        try {
            File outputFile = new File("imageOutput.png");
            ImageIO.write(outputImage, "png", outputFile);
            System.out.println("Gambar berhasil disimpan sebagai 'imageOutput.jpg'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}