package CinemaManager;

public class Cinema {
    String name, id, address, cineArea;
    public Cinema() {
        this.name = "";
        this.id = "";
        this.address = "";
        this.cineArea = "";
    }
    public Cinema(String name, String id, String address, String cineArea) {
        this.cineArea = cineArea;
        this.name = name;
        this.id = id;
        this.address = address;
    }
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
}
