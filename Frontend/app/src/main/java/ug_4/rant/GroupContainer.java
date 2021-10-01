package ug_4.rant;

public class GroupContainer {
    private String groupName, groupInfo;
    private int imageResource;
    private boolean isSelected;
    private int groupID;


    public GroupContainer(int imageResource, String groupName, String groupInfo, int groupID) {
        this.groupName = groupName;
        this.imageResource = imageResource;
        this.groupInfo = groupInfo;
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public int getImageResource() {
        return this.imageResource;
    }

    public void setSelected(boolean val) {
        this.isSelected = val;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}

