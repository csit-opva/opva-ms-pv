package sg.gov.csit.opvamspv.officer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, String> {
    List<Officer> findByNameLike(String name);
    List<Officer> findByIsAdmin(boolean isAdmin);

    @Query("select o.role from Officer o where o.isAdmin= :isAdmin")
    List<Role> findByCustomIsAdmin(@Param("isAdmin") boolean isAdmin);

    @Query("select o.role from Officer o where o.isAdmin= :isAdmin")
    Set<Role> findByIsAdminUniqueRole(@Param("isAdmin") boolean isAdmin);

}
