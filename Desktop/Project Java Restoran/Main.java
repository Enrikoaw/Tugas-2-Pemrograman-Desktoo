import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

   
    private static ArrayList<Menu> daftarMenu = new ArrayList<>();
    private static ArrayList<Menu> pesanan = new ArrayList<>();
    
    
    private static Scanner scanner = new Scanner(System.in);

   
     
    public static void main(String[] args) {
       
        inisialisasiMenu();
        
       
        menuUtama();
        
      
        scanner.close();
    }

    
    private static void inisialisasiMenu() {
    
        daftarMenu.add(new Menu("Nasi Goreng", 25000, "makanan"));
        daftarMenu.add(new Menu("Mie Ayam", 20000, "makanan"));
        daftarMenu.add(new Menu("Ayam Bakar", 30000, "makanan"));
        daftarMenu.add(new Menu("Sate Ayam", 28000, "makanan"));
        
        daftarMenu.add(new Menu("Es Teh", 8000, "minuman"));
        daftarMenu.add(new Menu("Es Jeruk", 10000, "minuman"));
        daftarMenu.add(new Menu("Jus Alpukat", 15000, "minuman"));
        daftarMenu.add(new Menu("Kopi Susu", 18000, "minuman"));
    }

    
    private static void menuUtama() {
        boolean keluar = false;
        
        do {
            System.out.println("\n===== SELAMAT DATANG DI RESTORAN =====");
            System.out.println("Pilih Menu Anda:");
            System.out.println("1. Menu Pelanggan (Pesan Makanan)");
            System.out.println("2. Menu Pengelolaan (Admin)");
            System.out.println("3. Keluar");
            System.out.print("Pilihan (1-3): ");

            int pilihan = bacaInteger();

            
            switch (pilihan) {
                case 1:
                    menuPelanggan();
                    break;
                case 2:
                    menuPengelolaan();
                    break;
                case 3:
                    keluar = true;
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (!keluar);
    }

    
    private static void menuPelanggan() {
        System.out.println("\n--- Menu Pemesanan Pelanggan ---");
        
        pesanan.clear();
        prosesPemesanan();
    }

    
    private static void tampilkanMenuPelanggan() {
        System.out.println("\n===== MENU MAKANAN =====");
       
        for (Menu item : daftarMenu) {
            
            if (item.getKategori().equalsIgnoreCase("makanan")) {
                System.out.printf("%-20s - Rp %,d\n", item.getNama(), item.getHarga());
            }
        }
        
        System.out.println("\n===== MENU MINUMAN =====");
        for (Menu item : daftarMenu) {
            if (item.getKategori().equalsIgnoreCase("minuman")) {
                System.out.printf("%-20s - Rp %,d\n", item.getNama(), item.getHarga());
            }
        }
        System.out.println("---------------------------------");
    }

    
    private static void prosesPemesanan() {
        
        while (true) {
            tampilkanMenuPelanggan();
            System.out.println("\nKetik nama menu yang ingin dipesan (atau 'selesai' untuk Cek Out):");
            String namaPesanan = scanner.nextLine();

            
            if (namaPesanan.equalsIgnoreCase("selesai")) {
                break; 
            }

           
            Menu itemDitemukan = cariMenu(namaPesanan);

            
            if (itemDitemukan != null) {
                pesanan.add(itemDitemukan);
                System.out.println("-> '" + itemDitemukan.getNama() + "' berhasil ditambahkan ke pesanan.");
            } else {
                
                System.out.println("Menu tidak ditemukan! Silakan masukkan nama menu yang valid.");
            }
        }

        
        if (!pesanan.isEmpty()) {
            hitungDanCetakStruk();
        } else {
            System.out.println("Anda tidak memesan apapun.");
        }
    }

    
    private static void hitungDanCetakStruk() {
        int subtotal = 0;
        
        
        HashMap<String, Integer> jumlahPerItem = new HashMap<>();
        HashMap<String, Menu> mapItem = new HashMap<>();

        
        for (Menu item : pesanan) {
            subtotal += item.getHarga();
            
            jumlahPerItem.put(item.getNama(), jumlahPerItem.getOrDefault(item.getNama(), 0) + 1);
            mapItem.put(item.getNama(), item);
        }

        
        final double PAJAK_RATE = 0.10; 
        final double BIAYA_PELAYANAN = 20000;

        
        double pajak = subtotal * PAJAK_RATE;
        double diskonPersen = 0;
        double diskonBogo = 0;
        String catatanDiskon = "";

        
        if (subtotal > 100000) {
            diskonPersen = subtotal * 0.10;
            catatanDiskon += String.format("   - Diskon 10%% (Total > 100rb): -Rp %,.0f\n", diskonPersen);
        }

       
        if (subtotal > 50000) {
            Menu minumanTermurah = null;
            
            for (Menu item : pesanan) {
                if (item.getKategori().equalsIgnoreCase("minuman")) {
                    
                    if (minumanTermurah == null || item.getHarga() < minumanTermurah.getHarga()) {
                        minumanTermurah = item;
                    }
                }
            }
            
            if (minumanTermurah != null) {
                diskonBogo = minumanTermurah.getHarga();
                catatanDiskon += String.format("   - Gratis 1 Minuman (%s): -Rp %,.0f\n", minumanTermurah.getNama(), diskonBogo);
            }
        }
      

        double totalDiskon = diskonPersen + diskonBogo;
        double totalAkhir = subtotal - totalDiskon + pajak + BIAYA_PELAYANAN;

        // Cetak Struk
        System.out.println("\n=================== STRUK PEMBAYARAN ===================");
        System.out.println("Item yang Dipesan:");
        
        
        for (String namaMenu : jumlahPerItem.keySet()) {
            Menu item = mapItem.get(namaMenu);
            int jumlah = jumlahPerItem.get(namaMenu);
            System.out.printf("   %-20s x%d (Rp %,d) = Rp %,d\n", 
                item.getNama(), jumlah, item.getHarga(), (item.getHarga() * jumlah));
        }
        
        System.out.println("------------------------------------------------------");
        System.out.printf("Subtotal: \t\t\t\tRp %,d\n", subtotal);
        
        
        if (totalDiskon > 0) {
            System.out.println("Diskon & Penawaran:");
            System.out.print(catatanDiskon);
            System.out.printf("Total Diskon: \t\t\t\t-Rp %,.0f\n", totalDiskon);
        }
        
        System.out.printf("Pajak Restoran (10%%): \t\t\tRp %,.0f\n", pajak);
        System.out.printf("Biaya Pelayanan: \t\t\tRp %,.0f\n", BIAYA_PELAYANAN);
        System.out.println("------------------------------------------------------");
        System.out.printf("TOTAL PEMBAYARAN: \t\t\tRp %,.0f\n", totalAkhir);
        System.out.println("======================================================");
        System.out.println("Tekan Enter untuk kembali ke Menu Utama...");
        scanner.nextLine(); 
    }

    
    private static void menuPengelolaan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n--- Menu Pengelolaan Restoran ---");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilihan (1-4): ");

            int pilihan = bacaInteger();

            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    kembali = true; 
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

   
    private static void tampilkanMenuManajemen() {
        System.out.println("\n--- Daftar Menu Saat Ini ---");
        
        for (int i = 0; i < daftarMenu.size(); i++) {
            
            System.out.println((i + 1) + ". " + daftarMenu.get(i).toString());
        }
        System.out.println("----------------------------");
    }

    
    private static void tambahMenu() {
        System.out.println("\n--- Tambah Menu Baru ---");
        String kategori;
        
        do {
            System.out.print("Masukkan kategori (makanan/minuman): ");
            kategori = scanner.nextLine();
            if (!kategori.equalsIgnoreCase("makanan") && !kategori.equalsIgnoreCase("minuman")) {
                System.out.println("Kategori tidak valid. Harap masukkan 'makanan' atau 'minuman'.");
            }
        } while (!kategori.equalsIgnoreCase("makanan") && !kategori.equalsIgnoreCase("minuman"));

        System.out.print("Masukkan nama menu baru: ");
        String nama = scanner.nextLine();
        
        System.out.print("Masukkan harga menu baru: ");
        int harga = bacaInteger();

        
        daftarMenu.add(new Menu(nama, harga, kategori));
        System.out.println("Menu '" + nama + "' berhasil ditambahkan!");
    }

    
    private static void ubahHargaMenu() {
        tampilkanMenuManajemen();
        if (daftarMenu.isEmpty()) {
            System.out.println("Daftar menu kosong.");
            return;
        }

        System.out.print("Masukkan nomor menu yang akan diubah harganya: ");
        int nomor = bacaInteger();

        
        if (nomor >= 1 && nomor <= daftarMenu.size()) {
            int index = nomor - 1; 
            Menu menu = daftarMenu.get(index);
            
            System.out.println("Mengubah: " + menu.getNama());
            System.out.print("Masukkan harga baru: ");
            int hargaBaru = bacaInteger();

            System.out.print("Anda yakin ingin mengubah harga? (Ya/Tidak): ");
            String konfirmasi = scanner.nextLine();

           
            if (konfirmasi.equalsIgnoreCase("Ya")) {
                menu.setHarga(hargaBaru);
                System.out.println("Harga berhasil diubah.");
            } else {
                System.out.println("Perubahan dibatalkan.");
            }
        } else {
           
            System.out.println("Nomor menu tidak valid.");
        }
    }

   
    private static void hapusMenu() {
        tampilkanMenuManajemen();
        if (daftarMenu.isEmpty()) {
            System.out.println("Daftar menu kosong.");
            return;
        }

        System.out.print("Masukkan nomor menu yang akan dihapus: ");
        int nomor = bacaInteger();

        
        if (nomor >= 1 && nomor <= daftarMenu.size()) {
            int index = nomor - 1;
            Menu menu = daftarMenu.get(index);
            
            System.out.print("Anda yakin ingin menghapus '" + menu.getNama() + "'? (Ya/Tidak): ");
            String konfirmasi = scanner.nextLine();

            if (konfirmasi.equalsIgnoreCase("Ya")) {
                daftarMenu.remove(index);
                System.out.println("Menu berhasil dihapus.");
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }
        } else {
            System.out.println("Nomor menu tidak valid.");
        }
    }

    
     
    private static Menu cariMenu(String nama) {
        for (Menu item : daftarMenu) {
            if (item.getNama().equalsIgnoreCase(nama)) {
                return item;
            }
        }
        return null; 
    }

    
    private static int bacaInteger() {
        while (true) {
            try {
                
                int input = Integer.parseInt(scanner.nextLine());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka.");
                System.out.print("Coba lagi: ");
            }
        }
    }
}