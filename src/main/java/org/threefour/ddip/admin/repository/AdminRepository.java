package org.threefour.ddip.admin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.admin.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin ,Long> {
}
