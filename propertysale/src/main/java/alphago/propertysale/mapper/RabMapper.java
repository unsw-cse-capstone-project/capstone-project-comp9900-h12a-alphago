package alphago.propertysale.mapper;

import alphago.propertysale.entity.POJO.Rab;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * @Description: Data access layer for Rab table in database
 */
public interface RabMapper extends BaseMapper<Rab> {

    @Select("SELECT * FROM rab r , auction a WHERE r.uid = #{uid} AND r.aid = a.aid AND (a.status = 'R' OR a.status = 'A')")
    List<Rab> getRunningAuctions(long uid);
    @Select("SELECT * FROM rab r , auction a WHERE r.uid = #{uid} AND r.aid = a.aid AND (a.status = 'S' OR a.status = 'F')")
    List<Rab> getPastAuctions(long uid);
}
