package sg.gov.csit.opvamspv.paymentvoucher;

public class PVHeader {
    private String financialYear;
    private String departmentRepresentation; // Not sure
    private String stationCode;
    private String timeStamp;
    private String version;

    public PVHeader(String headerStr) {
        String[] headers = headerStr.split("-");
        this.financialYear = headers[0];
        this.departmentRepresentation = headers[1];
        this.stationCode = headers[2];
        this.timeStamp = headers[3];
        this.version = headers[4];
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public String getDepartmentRepresentation() {
        return departmentRepresentation;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return this.financialYear + this.departmentRepresentation + this.stationCode + this.timeStamp + this.version;
    }
}
