/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.migration;

import org.apache.log4j.Logger;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author dominik
 */
public class V2017_06_15_20_00__Change_Team_Names_With_Firstname implements SpringJdbcMigration {

    private static final Logger LOG = Logger.getLogger(V2017_06_15_20_00__Change_Team_Names_With_Firstname.class);

    private static final String TABLE_NAME = "Participant";
    private static final String ID_FIELD_NAME = "ID";
    private static final String NAME_FIELD_NAME = "name";

    private static final String TABLE_NAME_2 = "Participant_Participant";
    private static final String PLAYERS_ID_FIELD_NAME = "players_id";
    private static final String LASTNAME_FIELD_NAME = "lastName";
    private static final String FIRSTNAME_FIELD_NAME = "firstName";
    private static final String PARTICIPANTS_ID_FIELD_NAME = "Team_id";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Updating Team names");
        List<TeamObject> data = jdbcTemplate.query("select `" + ID_FIELD_NAME + "` from `" + TABLE_NAME + "` where `participantType` = 'Team'", new TeamObjectRowMapper<>());
        for (TeamObject entry : data) {
            //get team members
            List<MemberObject> members = jdbcTemplate.query("select `" + PLAYERS_ID_FIELD_NAME + "`, `" + FIRSTNAME_FIELD_NAME + "`, `" + LASTNAME_FIELD_NAME + "` from `" + TABLE_NAME_2 + "` inner join `" + TABLE_NAME + "` where `" + PARTICIPANTS_ID_FIELD_NAME + "` = " + entry.getId() + " and " + TABLE_NAME + ".participantType = 'Player' and " + TABLE_NAME + ".id = " + PLAYERS_ID_FIELD_NAME + ";", new MemberObjectRowMapper<>());
            Collections.sort(members);
            StringBuilder teamName = new StringBuilder();
            for (int i = 0; i < members.size(); i++) {
                if (i != 0) {
                    teamName.append(" / ");
                }
                MemberObject member = members.get(i);
                teamName.append(member.getFirstName()).append(" ").append(member.getLastName());
            }
            jdbcTemplate.update("update `" + TABLE_NAME + "` set `" + NAME_FIELD_NAME + "` = '" + teamName.toString() + "' where `" + ID_FIELD_NAME + "` = " + entry.getId());
        }
        LOG.info("Done updating Team names");
    }


    private static class TeamObject {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    private static class MemberObject implements Comparable<MemberObject> {
        private Long players_id;
        private String firstName;
        private String lastName;

        public Long getPlayers_id() {
            return players_id;
        }

        public void setPlayers_id(Long players_id) {
            this.players_id = players_id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
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

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.players_id);
            hash = 29 * hash + Objects.hashCode(this.lastName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MemberObject other = (MemberObject) obj;
            if (!Objects.equals(this.firstName, other.firstName)) {
                return false;
            }
            if (!Objects.equals(this.lastName, other.lastName)) {
                return false;
            }
            if (!Objects.equals(this.players_id, other.players_id)) {
                return false;
            }
            return true;
        }
    }

    private static class TeamObjectRowMapper<T> implements RowMapper<TeamObject> {

        @Override
        public TeamObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            TeamObject o = new TeamObject();
            o.setId(rs.getLong(ID_FIELD_NAME));
            return o;
        }
    }


    private static class MemberObjectRowMapper<T> implements RowMapper<MemberObject> {

        @Override
        public MemberObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            MemberObject o = new MemberObject();
            o.setPlayers_id(rs.getLong(PLAYERS_ID_FIELD_NAME));
            o.setFirstName(rs.getString(FIRSTNAME_FIELD_NAME));
            o.setLastName(rs.getString(LASTNAME_FIELD_NAME));
            return o;
        }
    }
}
