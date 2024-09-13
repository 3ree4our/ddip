package org.threefour.ddip.product.domain;

public class RegisterProductRequest {
    private String name;
    private int price;
    private String title;
    private String content;

    public RegisterProductRequest(String name, int price, String title, String content) {
        this.name = name;
        this.price = price;
        this.title = title;
        this.content = content;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    String getTitle() {
        return title;
    }

    String getContent() {
        return content;
    }
}
