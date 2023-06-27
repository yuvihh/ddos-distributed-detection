package detection.dao;

import detection.entity.Attack;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttackDao {

    void insert(Attack attack);
}
