package com.example.kd.ata;

/**
 * Created by kd on 30/12/17.
 */

public class ATA {

        private int id;
        private String email;
        private String mobile_no;
        private String password;
        private String imsi;
        private String imei;
        private String alt_no;


        public ATA()
        {
            this.id=id;
            this.email=email;
            this.password=password;
            this.imsi=imsi;
            this.imei=imei;
            this.alt_no=alt_no;
            this.mobile_no=mobile_no;
        }



    public void setId(int id) {
            this.id = id;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public void setImsi(String imsi) {
            this.imsi = imsi;
        }
        public void setImei(String imei) {
            this.imei = imei;
         }
        public void setAlt_no(String alt_no) {
            this.alt_no = alt_no;
        }
        public int getId() {
            return id;
        }
        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
        public String getImsi() {
            return imsi;
        }
        public String getImei() {
            return imei;
        }
        public String getAlt_no() {
            return alt_no;
        }
        public String  getmobile_no(String string) {return mobile_no;}

    }




