package com.example.greenleaf;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private String description;
    private double price;
    private int imageResource;

    // Constructor
    public Product(String name, String description, double price, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
    }

    // Getter methods
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageResource() { return imageResource; }

    // Parcelable implementation
    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageResource = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(imageResource);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}

