package org.example.dal;

import org.example.data.LineStopEntity;
import org.example.data.StopId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineStopRepository extends PagingAndSortingRepository<LineStopEntity, StopId> { }
