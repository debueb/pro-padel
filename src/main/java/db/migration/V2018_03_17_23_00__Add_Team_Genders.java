/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.migration;

import de.appsolve.padelcampus.constants.Gender;
import org.apache.log4j.Logger;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dominik
 */
public class V2018_03_17_23_00__Add_Team_Genders implements SpringJdbcMigration {

    private static final Logger LOG = Logger.getLogger(V2018_03_17_23_00__Add_Team_Genders.class);

    private static final String TABLE_NAME_PARTICIPANT = "Participant";
    private static final String TABLE_NAME_PARTICIPANT_PARTICIPANT = "Participant_Participant";
    private static final String ID_FIELD_NAME = "ID";
    private static final String GENDER_FIELD_NAME = "gender";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Adding Team Genders");
        List<TeamObject> teams = jdbcTemplate.query("select `" + ID_FIELD_NAME + "` from `" + TABLE_NAME_PARTICIPANT + "` where `participantType` = 'Team'", new TeamObjectRowMapper<TeamObject>());
        for (TeamObject team : teams) {
            List<PlayerObject> players = jdbcTemplate.query("select " + GENDER_FIELD_NAME + " from " + TABLE_NAME_PARTICIPANT_PARTICIPANT + " pp inner join " + TABLE_NAME_PARTICIPANT + " p on pp.players_id = p.id where Team_id = " + team.getId() + ";", new PlayerObjectRowMapper<PlayerObject>());
            Gender gender = getGender(players);
            jdbcTemplate.update("update `" + TABLE_NAME_PARTICIPANT + "` set `" + GENDER_FIELD_NAME + "` = '" + gender + "' where `" + ID_FIELD_NAME + "` = " + team.getId());
        }
        LOG.info("Done adding Team Genders");
    }

    private static class TeamObject {
        private Long id;
        private String UUID;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    private static class PlayerObject {
        private Gender gender;

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
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

    private static class PlayerObjectRowMapper<T> implements RowMapper<PlayerObject> {

        @Override
        public PlayerObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            PlayerObject o = new PlayerObject();
            o.setGender(Gender.valueOf(rs.getString(GENDER_FIELD_NAME)));
            return o;
        }
    }

    private Gender getGender(List<PlayerObject> players) {
        Gender teamGender = null;
        if (players != null) {
            for (PlayerObject player : players) {
                if (teamGender == null) {
                    teamGender = player.getGender();
                } else if (!teamGender.equals(player.getGender())) {
                    teamGender = Gender.mixed;
                }
            }
        }
        return teamGender;
    }
}
