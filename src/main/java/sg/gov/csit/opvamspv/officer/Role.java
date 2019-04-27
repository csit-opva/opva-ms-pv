package sg.gov.csit.opvamspv.officer;

public enum Role {
    Admin("System Admin"),
    AO("Approving Officer"),
    CO("Checking Officer"),
    SO("Supporting Officer"),
    CSA("Customer Support Assistance");

    private String roleDesc;

    Role(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String value() {
        return this.roleDesc;
    }
}
