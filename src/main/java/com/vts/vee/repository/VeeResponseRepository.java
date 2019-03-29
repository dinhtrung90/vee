package com.vts.vee.repository;

import com.vts.vee.domain.VeeResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VeeResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VeeResponseRepository extends JpaRepository<VeeResponse, Long> {

}
