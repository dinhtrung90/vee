package com.vts.vee.repository;

import com.vts.vee.domain.QuestionOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the QuestionOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionOrderRepository extends JpaRepository<QuestionOrder, Long> {

}
