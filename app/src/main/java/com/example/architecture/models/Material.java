package com.example.architecture.models;

public class Material {
    private String id;           // Unique identifier

    private String name;
    private String description;
    private String type;
    private int quantity;
    private double price;

    public Material(String id, String name, String description, String type, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public Material() {
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
