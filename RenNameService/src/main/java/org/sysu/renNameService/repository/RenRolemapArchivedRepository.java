package org.sysu.renNameService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sysu.renNameService.entity.RenRolemapArchivedEntity;

import javax.transaction.Transactional;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : Repository
 */
@Transactional
public interface RenRolemapArchivedRepository extends JpaRepository<RenRolemapArchivedEntity, String> {
}
