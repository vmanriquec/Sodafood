package com.firesoda.sodafood.modelo;

public class Pedido {


private int idpedido;
    private int idcliente;
    private int idmesa;
    private Double totalpedido;
    private String estadopedido ;
    private String fechapedido;
    private int idusuario;
    private int idalmacen;
    private String idfacebook ;


    public Pedido(int idcliente, int idmesa, Double totalpedido, String estadopedido, String fechapedido, int idusuario, int idalmacen, String idfacebook )
    {
        super();
        this.idcliente=idcliente;
        this.idmesa=idmesa;
        this.totalpedido=totalpedido;
        this.estadopedido=estadopedido;
        this.fechapedido=fechapedido;
        this.idusuario=idusuario;
        this.idalmacen=idalmacen;
        this.idfacebook=idfacebook;
    }




public int getIdalmacen(){return  idalmacen;}
    public void setIdalmacen(int idalmacen){this.idalmacen=idalmacen;}

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getIdmesa() {
        return idmesa;
    }

    public void setIdmesa(int idmesa) {
        this.idmesa = idmesa;
    }

    public Double getTotalpedido() {
        return totalpedido;
    }

    public void setTotalpedido(Double totalpedido) {
        this.totalpedido = totalpedido;
    }

    public String getEstadopedido() {
        return estadopedido;
    }

    public void setEstadopedido(String estadopedido) {
        this.estadopedido = estadopedido;
    }

    public String getFechapedido() {
        return fechapedido;
    }

    public void setFechapedido(String fechapedido) {
        this.fechapedido = fechapedido;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }
    public String getIdfacebook() {
        return idfacebook;
    }

    public void setIdfacebook(String idusuario) {
        this.idfacebook = idfacebook;
    }


}
