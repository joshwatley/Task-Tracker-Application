package com.example.goaltrackerapplication;

public class GoalAllocationModel {
    private String userid;
    private int goalid;

    public GoalAllocationModel(String userid, int goalid) {
        this.userid = userid;
        this.goalid = goalid;
    }

    @Override
    public String toString() {
        return "GoalAllocationModel{" +
                "userid='" + userid + '\'' +
                ", goalid=" + goalid +
                '}';
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getGoalid() {
        return goalid;
    }

    public void setGoalid(int goalid) {
        this.goalid = goalid;
    }
}
