package net.eclissi.lucasop.ioboss.database;

/**
 * Created by lucasop on 06/02/2017.
 */
public class DatabaseModel {
    private long dbID;
    private String stato;
    private String zonaID;
    private String name;
    private String address;
    private String coordinate;
    private String tipo;
    private int rag;
    private int img;


    public long getdbID() {return dbID;}
    public void setdbID(long dbID) {
        this.dbID = dbID;
    }
    /**************/
    public String getStato() {
        return stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }
    /**************/
    public String getzonaID() {
        return zonaID;
    }
    public void setZonaID(String zonaID) {
        this.zonaID = zonaID;
    }
    /**************/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**************/
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    /**************/
    public String getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
    /**************/
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**************/
    public int getRag() {
        return rag;
    }
    public void setRag(int rag) {this.rag = rag;}
    /**************/
    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }


}
