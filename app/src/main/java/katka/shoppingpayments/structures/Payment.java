package katka.shoppingpayments.structures;

public class Payment {
    private Long id;
    private String price;
    private String shop;

    public Payment(String price, String shop) {
        this.price = price;
        this.shop = shop;
    }

    public Long getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

}

