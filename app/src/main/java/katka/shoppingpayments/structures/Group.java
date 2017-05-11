package katka.shoppingpayments.structures;

import java.util.ArrayList;

public class Group {
    private String name;
    private ArrayList<Member> members;

    public Group() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return name;
    }
}
