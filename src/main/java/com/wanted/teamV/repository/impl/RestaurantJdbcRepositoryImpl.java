package com.wanted.teamV.repository.impl;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

public class RestaurantJdbcRepositoryImpl implements RestaurantJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    @Override
    public void bulkInsert(List<Restaurant> restaurants) {
        List<List<Restaurant>> partitions = partition(restaurants);
        for (List<Restaurant> partition : partitions) {
            insert(partition);
        }
    }

    private void insert(List<Restaurant> restaurants) {
        String sql = "INSERT IGNORE INTO restaurant (" +
                "name, sigun, type, roadname_address, lot_address, zip_code, lat, lon, average_rating, bsn_state_nm, " +
                "sigun_cd, license_de, clsbiz_de, locplc_ar, grad_faclt_div_nm, male_enflpsn_cnt, year, " +
                "multi_use_bizestbl_yn, grad_div_nm, tot_faclt_scale, female_enflpsn_cnt, " +
                "circumfr_div_nm, sanittn_indutype_nm, tot_emply_cnt" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ")";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Restaurant restaurant = restaurants.get(i);
                String licenseDe = restaurant.getLicenseDe() == null ? null : restaurant.getLicenseDe().format(formatter);
                String clsbizDe = restaurant.getClsbizDe() == null ? null : restaurant.getClsbizDe().format(formatter);

                ps.setString(1, restaurant.getName());
                ps.setString(2, restaurant.getSigun());
                ps.setString(3, restaurant.getType());
                ps.setString(4, restaurant.getRoadnameAddress());
                ps.setString(5, restaurant.getLotAddress());
                ps.setString(6, restaurant.getZipCode());
                ps.setDouble(7, restaurant.getLat());
                ps.setDouble(8, restaurant.getLon());
                ps.setDouble(9, restaurant.getAverageRating());
                ps.setString(10, restaurant.getBsnStateNm());
                ps.setString(11, restaurant.getSigunCd());
                ps.setString(12, licenseDe);
                ps.setString(13, clsbizDe);

                if (restaurant.getLocplcAr() == null) {
                    ps.setString(14, null);
                } else {
                    ps.setDouble(14, restaurant.getLocplcAr());
                }

                ps.setString(15, restaurant.getGradFacltDivNm());

                if (restaurant.getMaleEnflpsnCnt() == null) {
                    ps.setString(16, null);
                } else {
                    ps.setInt(16, restaurant.getMaleEnflpsnCnt());
                }

                if (restaurant.getYear() == null) {
                    ps.setString(17, null);
                } else {
                    ps.setInt(17, restaurant.getYear());
                }

                if (restaurant.getMultiUseBizestblYn() == null) {
                    ps.setString(18, null);
                } else {
                    ps.setBoolean(18, restaurant.getMultiUseBizestblYn());
                }

                ps.setString(19, restaurant.getGradDivNm());

                if (restaurant.getTotFacltScale() == null) {
                    ps.setString(20, null);
                } else {
                    ps.setDouble(20, restaurant.getTotFacltScale());
                }

                if (restaurant.getFemaleEnflpsnCnt() == null) {
                    ps.setString(21, null);
                } else {
                    ps.setInt(21, restaurant.getFemaleEnflpsnCnt());
                }

                ps.setString(22, restaurant.getCircumfrDivNm());
                ps.setString(23, restaurant.getSanittnIndutypeNm());

                if (restaurant.getTotEmplyCnt() == null) {
                    ps.setString(24, null);
                } else {
                    ps.setInt(24, restaurant.getTotEmplyCnt());
                }
            }

            @Override
            public int getBatchSize() {
                return restaurants.size();
            }
        });
    }

    private List<List<Restaurant>> partition(List<Restaurant> restaurants) {
        List<List<Restaurant>> partitions = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i += DEFAULT_CHUNK_SIZE) {
            partitions.add(restaurants.subList(i, Integer.min(i + DEFAULT_CHUNK_SIZE, restaurants.size())));
        }
        return partitions;
    }


}
