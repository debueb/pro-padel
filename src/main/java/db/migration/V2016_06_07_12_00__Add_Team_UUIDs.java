/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dominik
 */
public class V2016_06_07_12_00__Add_Team_UUIDs implements SpringJdbcMigration{
    
    private static final Logger LOG = Logger.getLogger(V2016_06_07_12_00__Add_Team_UUIDs.class);
    
    private static final String TABLE_NAME          = "Participant";
    private static final String ID_FIELD_NAME       = "ID";
    private static final String UUID_FIELD_NAME     = "UUID";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Updating Team UUIDs");
        List<TeamObject> data = jdbcTemplate.query("select `" + ID_FIELD_NAME + "`, `" + UUID_FIELD_NAME + "` from `"+TABLE_NAME+"` where `participantType` = 'Team'", new TeamObjectRowMapper<TeamObject>());
        for (TeamObject entry: data){
            if (entry.getUUID()==null){
                jdbcTemplate.update("update `"+TABLE_NAME+"` set `"+UUID_FIELD_NAME+"` = '"+UUID.randomUUID().toString()+"' where `"+ID_FIELD_NAME+"` = "+entry.getId());
            }
        }
        LOG.info("Done updating Team UUIDs");
    }
    
    
    private static class TeamObject{
        private Long id;
        private String UUID;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUUID() {
            return UUID;
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }
    }
    
    private static class TeamObjectRowMapper<T> implements RowMapper<TeamObject>{

        @Override
        public TeamObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            TeamObject o = new TeamObject();
            o.setId(rs.getLong(ID_FIELD_NAME));
            o.setUUID(rs.getString(UUID_FIELD_NAME));
            return o;
        }
    }
}
