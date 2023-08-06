package web.emp.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Dept {
	@Id
//	@Column(name = "DEPTNO")
    private Integer deptno;
//	@Column(name = "DNAME")
    private String dname;
//	@Column(name = "LOC")
    private String loc;
    @OneToMany(mappedBy = "dept")
    private List<Emp> emps;
//    @OneToMany
//    @JoinColumn(name = "DEPTNO",
//    	referencedColumnName= "DEPTNO")
//    private List<Emp> emps;
}
