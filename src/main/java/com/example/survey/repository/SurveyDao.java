package com.example.survey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.survey.entity.Survey;

@Repository
public interface SurveyDao extends JpaRepository<Survey, Integer> {

	@Modifying(clearAutomatically = true)
	@Query(value = "update survey.survey set name=?2, description=?3, published=?4, start_date=?5, end_date=?6, questions=?7"
			+ " where no=?1 and published=false or no=?1 and start_date>CURRENT_DATE", nativeQuery = true)
	public int update(int no, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, String questions);

	public void deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(List<Integer> no1, List<Integer> no2,
			LocalDate now);

	@Query(value = "select * from survey.survey where name like %?1% and start_date>=?2 and end_date<=?3 and published=true limit 50,?4", nativeQuery = true)
	public List<Survey> frontSearch(String surveyName, LocalDate startDate, LocalDate endDate, int index);

	@Query(value = "select * from survey.survey where name like %?1% and start_date>=?2 and end_date<=?3 and author=?4 limit 50,?5", nativeQuery = true)
	public List<Survey> backSearch(String surveyName, LocalDate startDate, LocalDate endDate, String author, int index);

	// 即將結束 : 結束時間 > now，挑選值最小的四個
	public List<Survey> findTop4ByEndDateGreaterThanOrderByEndDateAsc(LocalDate now);
	// 最近開始 : 開始時間 < now，挑選值最大的四個
	public List<Survey> findTop4ByStartDateLessThanOrderByStartDateDesc(LocalDate now);
	// 即將開始 : 開始時間 > now，挑選值最小的四個
	public List<Survey> findTop4ByStartDateGreaterThanOrderByStartDateAsc(LocalDate now);

	
}
