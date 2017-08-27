/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.migration;

import de.appsolve.padelcampus.constants.ImageCategory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dominik
 */
public class V2017_08_27_14_00__Import_Images_Into_Db implements SpringJdbcMigration {

    private static final Logger LOG = Logger.getLogger(V2017_08_27_14_00__Import_Images_Into_Db.class);

    private static final String TABLE_NAME = "Image";
    private static final String FIELD_ID = "id";
    private static final String FIELD_FILE_PATH = "filePath";
    private static final String FIELD_CONTENT_LENGTH = "contentLength";
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_CATEGORY = "category";


    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LOG.info("Importing images into db");
        List<ImageObject> images = jdbcTemplate.query("select `" + FIELD_ID + "`, `" + FIELD_FILE_PATH + "` from `" + TABLE_NAME + "`", new ImageObjectRowMapper<>());
        for (ImageObject entry : images) {
            try {
                File file = new File(entry.getFilePath());
                byte[] data = IOUtils.toByteArray(new FileInputStream(file));
                if (data != null) {
                    ImageCategory category = getCategory(entry.getFilePath());
                    jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(
                                String.format("update `%s` set `%s` = ?, `%s` = ?, `%s` = ? where `%s` = ?", TABLE_NAME, FIELD_CONTENT_LENGTH, FIELD_CONTENT, FIELD_CATEGORY, FIELD_ID),
                                new String[]{FIELD_CONTENT});
                        ps.setLong(1, 0L + data.length);
                        ps.setBytes(2, data);
                        ps.setString(3, category.name());
                        ps.setLong(4, entry.getId());
                        return ps;
                    });
                }
            } catch (Exception e) {
                LOG.error(String.format("Unable to import file %s: %s", entry.getFilePath(), e.getMessage()));
            }
        }
        LOG.info("Done importing images into db");
    }

    private ImageCategory getCategory(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return ImageCategory.cmsImage;
        }
        if (filePath.contains("profilePictures")) {
            return ImageCategory.profilePicture;
        }
        if (filePath.contains("staffImages")) {
            return ImageCategory.staffPicture;
        }
        if (filePath.contains("companyLogo")) {
            return ImageCategory.companyIcon;
        }
        if (filePath.contains("touchIcon")) {
            return ImageCategory.touchIcon;
        }
        return ImageCategory.cmsImage;
    }


    private static class ImageObject {
        private Long id;
        private String filePath;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    private static class ImageObjectRowMapper<T> implements RowMapper<ImageObject> {

        @Override
        public ImageObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImageObject o = new ImageObject();
            o.setId(rs.getLong(FIELD_ID));
            o.setFilePath(rs.getString(FIELD_FILE_PATH));
            return o;
        }
    }
}
