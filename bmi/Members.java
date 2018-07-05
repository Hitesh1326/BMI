package com.example.shindehitesh.bmi;

/**
 * Created by Hitesh on 02-07-2018.
 */
//Storing data of members in firebase.
public class Members {
    int a;
    String p, n, r;

    public Members(int a, String p, String n, String r ) {
        this.a = a;
        this.p = p;
        this.n = n;
        this.r = r;



    }




    @Override
    public String toString() {
        return "Members "+ "Name= " + n + ", Age= " + a + ", Phone= "+ p + ",Gender= " + r ;


    }




    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }



    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
