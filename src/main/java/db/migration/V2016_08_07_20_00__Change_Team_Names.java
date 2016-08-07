/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dominik
 */
public class V2016_08_07_20_00__Change_Team_Names implements SpringJdbcMigration{
    
    private static final Logger LOG = Logger.getLogger(V2016_08_07_20_00__Change_Team_Names.class);
    
    private static final String TABLE_NAME                  = "Participant";
    private static final String ID_FIELD_NAME               = "ID";
    private static final String NAME_FIELD_NAME             = "name";
    
    private static final String TABLE_NAME_2                = "Participant_Participant";
    private static final String PLAYERS_ID_FIELD_NAME       = "players_id";
    private static final String LASTNAME_FIELD_NAME         = "lastName";
    private static final String PARTICIPANTS_ID_FIELD_NAME  = "Participant_id";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Updating Team names");
        List<TeamObject> data = jdbcTemplate.query("select `" + ID_FIELD_NAME + "` from `"+TABLE_NAME+"` where `participantType` = 'Team'", new TeamObjectRowMapper<TeamObject>());
        for (TeamObject entry: data){
            //get team members
            List<MemberObject> members = jdbcTemplate.query("select `"+PLAYERS_ID_FIELD_NAME+"`, `"+LASTNAME_FIELD_NAME+"` from `"+TABLE_NAME_2+"` inner join `"+TABLE_NAME+"` where `"+PARTICIPANTS_ID_FIELD_NAME+"` = "+entry.getId()+" and "+TABLE_NAME+".participantType = 'Player' and "+TABLE_NAME+".id = "+PLAYERS_ID_FIELD_NAME+";", new MemberObjectRowMapper<MemberObject>());
            Collections.sort(members);
            StringBuilder teamName = new StringBuilder();
            for (int i=0; i<members.size(); i++){
                if (i!=0){
                    teamName.append(" / ");
                }
                MemberObject member = members.get(i);
                teamName.append(member.getLastName());
            }
            jdbcTemplate.update("update `"+TABLE_NAME+"` set `"+NAME_FIELD_NAME+"` = '"+teamName.toString()+"' where `"+ID_FIELD_NAME+"` = "+entry.getId());
        }
        LOG.info("Done updating Team names");
    }
    
    
    private static class TeamObject{
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
    
    private static class MemberObject implements Comparable<MemberObject>{
        private Long players_id;
        private String lastName;

        public Long getPlayers_id() {
            return players_id;
        }

        public void setPlayers_id(Long players_id) {
            this.players_id = players_id;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public int compareTo(MemberObject o) {
            return getLastName().compareToIgnoreCase(o.getLastName());
        }
    }
    
    private static class TeamObjectRowMapper<T> implements RowMapper<TeamObject>{

        @Override
        public TeamObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            TeamObject o = new TeamObject();
            o.setId(rs.getLong(ID_FIELD_NAME));
            return o;
        }
    }
    
    
    private static class MemberObjectRowMapper<T> implements RowMapper<MemberObject>{

        @Override
        public MemberObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            MemberObject o = new MemberObject();
            o.setPlayers_id(rs.getLong(PLAYERS_ID_FIELD_NAME));
            o.setLastName(rs.getString(LASTNAME_FIELD_NAME));
            return o;
        }
    }
}