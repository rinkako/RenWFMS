package org.sysu.renNameService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sysu.renNameService.entity.RenRuntimerecordEntity;

import javax.transaction.Transactional;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : Repository
 */
@Transactional
public interface RenRuntimerecordRepository extends JpaRepository<RenRuntimerecordEntity, String> {
}
