package com.vts.vee.repository;

import com.vts.vee.domain.Survey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Survey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
