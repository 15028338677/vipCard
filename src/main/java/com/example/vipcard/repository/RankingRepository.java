package com.example.vipcard.repository;

import com.example.vipcard.model.Ranking;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

@Transactional
public interface RankingRepository extends JpaRepository<Ranking,Integer> {

    // store查询所有用户的排队记录 按时间排序
    @Query("select ranking from Ranking ranking where ranking.store.storeOpenid = :storeOpenid order by ranking.rankingTime")
//    @Query(nativeQuery = true,value = "SELECT * FROM ranking where storeOpenid = 'oHy4O5A5cA3WAdC9YTJo8qMQiIUo' ORDER BY rankingTime")
    public Collection<Ranking> findAllByStoreOpenid(String storeOpenid);

    //user查询自己的排队进度
//    @Query("select ranking from Ranking ranking where ranking.user.userOpenid = :userOpenid")
    @Query(nativeQuery = true,value = "" +
            "SELECT c.rankingNum " +
            "from " +
            "(Select userOpenid,storeOpenid,rankingTime,(@rowNum \\:= @rowNum+1) as rankingNum " +
            "From ranking, " +
            "(Select (@rowNum \\:= 0) ) b " +
            "Order by ranking.rankingTime) c " +
            "where c.userOpenid = ?1 and c.storeOpenid= ?2 ;")
    public Integer findByUserOpenid(String userOpenid,String storeOpenid);

    // 用户申请排队
    @Modifying
    @Query(nativeQuery = true,
    value = "INSERT INTO ranking (ranking.userOpenid, ranking.storeOpenid,ranking.rankingTime) VALUES (?1 , ?2 , ?3 )")
    public int addRanking(String userOpenid, String storeOpenid, String  date);

    // 获取排队总人数
}