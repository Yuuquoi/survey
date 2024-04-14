package com.example.survey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.survey.entity.Survey;

@Repository
public interface SurveyDao extends JpaRepository<Survey, Integer> {
	
	public List<Survey> findByAuthorContainingAndNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublished(String author, String name, LocalDate startDate, LocalDate endDate, boolean published);
	
	public void deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(int no1, int no2, LocalDate startDate);

	public void deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(List<Integer> nos, List<Integer> nos2,
			LocalDate now);
	
	public boolean existsByNoAndPublishedFalseOrNoAndStartDateAfter(int no1, int no2, LocalDate now);

}
