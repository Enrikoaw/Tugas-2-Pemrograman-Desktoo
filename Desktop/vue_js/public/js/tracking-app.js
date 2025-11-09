/* global Vue */
new Vue({
    el: '#tracking-app',
    data: function() {
        const savedDO = localStorage.getItem('lastDONumber');
        return {
            nomorDO: savedDO || '', 
            hasilTracking: null,
            pesanError: '',
            isLoading: false
        };
    },
    created() {
        if (this.nomorDO) {
            this.cariDataDO(this.nomorDO);
        }
       
    },
    watch: {
       
        nomorDO: function(newVal) {
            if (newVal && !/^\d+$/.test(newVal)) {
                this.pesanError = 'Hanya masukkan angka.';
                this.hasilTracking = null;
            } else {
                this.pesanError = ''; 
                if (newVal) {
                    this.cariDataDO(newVal);
                } else {
                    this.resetForm();
                }
            }
        },
        
        pesanError: function(newError) {
            if (newError) {
                console.error("Error:", newError);
            }
        }
    },
    methods: {
        
        cariDataDO(nomor) {
            this.isLoading = true;
            this.hasilTracking = null;
            this.pesanError = '';

           
            setTimeout(() => {
               
                const databaseDO = JSON.parse(localStorage.getItem('databaseDO')) || {};

                
                if (databaseDO[nomor]) {
                    
                    this.hasilTracking = databaseDO[nomor];
                    
                   
                    localStorage.removeItem('lastDONumber');

                } else {
                  
                    this.pesanError = 'Nomor DO tidak ditemukan.';
                }
                this.isLoading = false;
            }, 500); 
        },
        
        resetForm() {
            this.nomorDO = '';
            this.hasilTracking = null;
            this.pesanError = '';
            this.isLoading = false;
            
            localStorage.removeItem('lastDONumber');
        }
    }
});