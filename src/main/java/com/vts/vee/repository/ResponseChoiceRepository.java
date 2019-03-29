package com.vts.vee.repository;

import com.vts.vee.domain.ResponseChoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResponseChoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponseChoiceRepository extends JpaRepository<ResponseChoice, Long> {

}
