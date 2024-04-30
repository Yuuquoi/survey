package com.example.survey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.survey.entity.Reply;
import com.example.survey.entity.ReplyId;

@Repository
public interface ReplyDao extends JpaRepository<Reply, ReplyId>{

	public List<Reply> findBySurveyNo(int surveyNo);
}
