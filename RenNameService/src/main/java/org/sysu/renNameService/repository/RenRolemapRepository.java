package org.sysu.renNameService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sysu.renNameService.entity.RenRolemapEntity;

import javax.transaction.Transactional;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : Repository
 */
@Transactional
public interface RenRolemapRepository extends JpaRepository<RenRolemapEntity, String> {
}
