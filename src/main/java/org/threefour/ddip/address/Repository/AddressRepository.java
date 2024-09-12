package org.threefour.ddip.address.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.address.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
