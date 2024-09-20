package org.threefour.ddip.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.member.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Boolean existsByEmail(String email);
  Member findByEmail(String email);
  long countBy();
  @Query(value =
          "SELECT "+
                  "  count(*) " +
                  "FROM member " +
                  "GROUP BY created_at "
          , nativeQuery = true
  )
  String[] findgroupByMember();

  @Query("SELECT m FROM Member m WHERE m.deleteYn = false")
  List<Member> findActiveMembers();

  @Modifying
  @Transactional
  @Query("update Member m SET m.deleteYn = 1 where m.id = :mid")
  void delete_yn(@Param("mid") Long memberId);
}