package com.vts.vee.repository;

import com.vts.vee.domain.Respondent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Respondent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RespondentRepository extends JpaRepository<Respondent, Long> {

}
