package sg.gov.csit.opvamspv.accesscontrollist;

import org.hibernate.annotations.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

@Entity
public class Acl{

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private String dept;

    public Acl(Integer id, Role role, String dept) {
        this.id = id;
        this.role = role;
        this.dept = dept;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
