package com.github.wgzhao.dbquery.repo;

import com.github.wgzhao.dbquery.entities.SignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignRepo extends JpaRepository<SignEntity, String> {
}
