package com.octoriz.cwcci.model;

public class Member {
    private long id;
    private String name;
    private String membershipId;


    public Member(int id, String name, String membershipId) {
        this.id = id;
        this.name = name;
        this.membershipId = membershipId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

}
