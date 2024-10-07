// OS ku masih bermasalah ges buat cloning github, jadi aku nitip disini dulu ya hehe
// Btw ini pake metode perkalian trace

public double determinantUsingTriangle() {
    // I. S. : matriks persegi
    // F. S. : mengembalikan nilai determinan matriks
    
    // Membuat salinan matriks untuk dioperasikan
    double[][] triangularMatrix = new double[this.rowNum][this.colNum];
    for (int i = 0; i < this.rowNum; i++) {
        System.arraycopy(this.matrix[i], 0, triangularMatrix[i], 0, this.colNum);
    }

    // Melakukan reduksi untuk membentuk matriks segitiga atas
    for (int i = 0; i < this.rowNum; i++) {
        for (int j = i + 1; j < this.rowNum; j++) {
            // Jika elemen di diagonal utama adalah 0, tukar dengan baris lain
            if (triangularMatrix[i][i] == 0) {
                // Mencari baris di bawah yang bisa ditukar
                for (int k = j; k < this.rowNum; k++) {
                    if (triangularMatrix[k][i] != 0) {
                        double[] temp = triangularMatrix[i];
                        triangularMatrix[i] = triangularMatrix[k];
                        triangularMatrix[k] = temp;
                        break;
                    }
                }
            }

            // Menghilangkan elemen di bawah diagonal utama
            if (triangularMatrix[i][i] != 0) {
                double factor = triangularMatrix[j][i] / triangularMatrix[i][i];
                for (int k = i; k < this.colNum; k++) {
                    triangularMatrix[j][k] -= factor * triangularMatrix[i][k];
                }
            }
        }
    }

    // Menghitung determinan dari matriks segitiga atas
    double det = 1;
    for (int i = 0; i < this.rowNum; i++) {
        det *= triangularMatrix[i][i]; // Mengalikan elemen diagonal utama
    }

    return det; // Mengembalikan nilai determinan
}
