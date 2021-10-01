package ug_4.rant;

public class MemberContainer {
    private String memberEmail, memberRole;
    private int memberID;


    public MemberContainer( String memberEmail, String memberRole, int memberID) {
        this.memberEmail = memberEmail;
        this.memberRole = memberRole;
        this.memberID = memberID;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getMemberRole() {
        return memberRole;
    }


    public int getMemberID() {
        return memberID;
    }

}

