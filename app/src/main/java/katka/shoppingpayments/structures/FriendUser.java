package katka.shoppingpayments.structures;


public class FriendUser {
    private String nickname;

    public FriendUser(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return nickname;
    }
}
