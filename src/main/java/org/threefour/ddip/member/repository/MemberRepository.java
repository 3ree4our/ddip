package org.threefour.ddip.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threefour.ddip.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
