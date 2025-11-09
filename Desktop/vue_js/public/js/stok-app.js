
new Vue({
    el: '#stok-app',
    
    data: function() {
        return {
            bahanAjar: [],
            searchQuery: ''
        };
    },
    created() {
        this.bahanAjar = dataBahanAjar.map(item => ({
            ...item,
            jumlahPesan: null 
        }));
    },
    computed: {
        filteredBahanAjar() {
            if (!this.searchQuery) {
                return this.bahanAjar;
            }
            const query = this.searchQuery.toLowerCase();
            return this.bahanAjar.filter(item =>
                item.nama.toLowerCase().includes(query) ||
                item.kode.toLowerCase().includes(query)
            );
        },

        keranjang() {
            
            return this.bahanAjar.filter(item => 
                item.jumlahPesan > 0 && item.jumlahPesan <= item.stok
            );
        }
    },
    methods: {
        
        
        buatPesanan() {
            
            const newDO = Math.floor(10000 + Math.random() * 90000).toString();

            
            const ringkasanItem = this.keranjang.map(item => 
                `${item.nama} (${item.jumlahPesan} buah)`
            ).join(', '); 

         
            
            const trackingInfo = {
                status: 'Sedang Diproses',
                lokasi: 'Gudang Utama Jl. Raya Soekarno-Hatta No.45, Malang, Jawa Timur',
                itemDipesan: ringkasanItem 
            };

            
            let databaseDO = JSON.parse(localStorage.getItem('databaseDO')) || {};

            
            databaseDO[newDO] = trackingInfo;

            
            localStorage.setItem('databaseDO', JSON.stringify(databaseDO));

            
            localStorage.setItem('lastDONumber', newDO);

            
            alert(
                `Pemesanan berhasil!\n` +
                `Nomor DO Anda adalah: ${newDO}\n\n` +
                `Total ${this.keranjang.length} jenis item telah diproses.`
            );
            
            
            this.keranjang.forEach(item => {
                item.jumlahPesan = null; 
            });
        }
    }
});