package detection.dao;

import detection_interface.FlowInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowInfoDao {

    void insert(FlowInfo flowInfo);
}
