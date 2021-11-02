package zoom.meeting.domain.repositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import org.springframework.jdbc.datasource.DataSourceUtils;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource;

    @Override
    public List<String> allLoginId() {

        String sql = "select * from memberRepository";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<String> allLoginId = new ArrayList<>();
            while (rs.next()) {
                allLoginId.add(rs.getString("loginId"));
            }
            return allLoginId;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }


    @Override
    public List<String> allNickName() {

        String sql = "select * from memberRepository";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<String> allNickName = new ArrayList<>();
            while (rs.next()) {
                allNickName.add(rs.getString("nickName"));
            }
            return allNickName;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }


    @Override
    public Member save(Member member) {

        String sql = "insert into memberRepository (loginId,password,name,nickName) values(?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1,member.getLoginId());
            pstmt.setString(2,member.getPassword());
            pstmt.setString(3,member.getName());
            pstmt.setString(4,member.getNickName());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                member.setManageSeq(rs.getLong(1));
            } else{
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {

        String sql = "select * from memberRepository where loginId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,loginId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member(
                        rs.getString("loginId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickName"));
                member.setManageSeq(rs.getLong("manageSeq"));
                return Optional.of(member);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public List<Member> findAll() {

        String sql = "select * from memberRepository";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member member = new Member(
                        rs.getString("loginId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickName"));
                member.setManageSeq(rs.getLong("manageSeq"));
                members.add(member);
            }
            return members;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }


    @Override
    public Member updateByLoginId(String loginId, Member updatedMember) {


        String sql = "update loginId=?,password=?,name=?,nickName=? where loginId=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updatedMember.getLoginId());
            pstmt.setString(2, updatedMember.getPassword());
            pstmt.setString(3, updatedMember.getName());
            pstmt.setString(4, updatedMember.getNickName());
            pstmt.setString(5, loginId);

            rs = pstmt.executeQuery();

            return updatedMember;

        } catch (Exception e) {
            log.error("{} : id의 update 실패",updatedMember.getLoginId());
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public void removeByLoginId(String loginId) {


        String sql = "delete from memberRepository where loginid=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginId);

            rs = pstmt.executeQuery();

        } catch (Exception e) {
            log.error("{} : id의 사용자 delete 실패",loginId);
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Member> findByManageSeq(Long manageSeq) {

        String sql = "select * from memberRepository where manageSeq = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,manageSeq);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member(
                        rs.getString("loginId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("nickName"));
                member.setManageSeq(rs.getLong("manageSeq"));
                return Optional.of(member);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            close(conn,pstmt,rs);
        }
    }


    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) {
                pstmt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                close(conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

}
