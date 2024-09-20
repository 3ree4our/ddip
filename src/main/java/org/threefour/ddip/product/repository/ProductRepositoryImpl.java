package org.threefour.ddip.product.repository;

import org.springframework.stereotype.Repository;
import org.threefour.ddip.product.domain.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ProductRepositoryImpl  {
    @PersistenceContext
    private EntityManager em;

    public List<Product> selectBySearch(String type, String search) {
        // 1. CriteriaBuilder 인스턴스를 생성합니다.
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 2. CriteriaQuery를 생성합니다.
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        // 3. Root를 설정합니다.
        Root<Product> root = cq.from(Product.class);

        // 4. 검색 조건을 설정합니다.
        Predicate predicate = cb.conjunction(); // 기본적으로 True를 반환하는 조건을 생성합니다.

        if (type != null && search != null) {
            // 5. 'type'에 해당하는 속성을 찾고 검색 조건을 생성합니다.
            Path<String> field = root.get(type);
            predicate = cb.like(field, "%" + search + "%"); // 부분 일치를 위한 LIKE 조건
        }

        // 6. 조건을 쿼리에 추가합니다.
        cq.where(predicate);

        // 7. 쿼리를 실행하고 결과를 반환합니다.
        return em.createQuery(cq).getResultList();
    }


}


