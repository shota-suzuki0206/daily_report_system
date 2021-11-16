package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.LikeButtonConverter;
import actions.views.LikeButtonView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.LikeButton;

public class LikeButtonService extends ServiceBase {

    /**
     * 指定した従業員がいいねした日報データを、指定されたページ数の一覧画面に表示する分取得しReportViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<LikeButtonView> getMinePerPage(EmployeeView employee, int page) {

        List<LikeButton> likeButtons = em.createNamedQuery(JpaConst.Q_LIK_GET_ALL_MINE, LikeButton.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return LikeButtonConverter.toViewList(likeButtons);
    }

    /**
     * 指定した従業員がいいねした日報データの件数を取得し、返却する
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_LIK_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * いいねされた日報の登録内容を元にデータ１件作成し、いいねテーブルに登録する
     * @param lbv いいねの登録内容
     */
    public void create(LikeButtonView lbv) {
        LocalDateTime ldt = LocalDateTime.now();
        lbv.setCreatedAt(ldt);
        lbv.setUpdatedAt(ldt);
        createInternal(lbv);
    }

    /**
     * いいねデータを1件登録する
     * @param lbv いいねデータ
     */
    private void createInternal(LikeButtonView lbv) {

        em.getTransaction().begin();
        em.persist(LikeButtonConverter.toModel(lbv));
        em.getTransaction().commit();

    }



    public Boolean getLikeFlag(EmployeeView employee, ReportView report) {
        //従業員id、日報idを条件にデータを取得
       double count = em.createNamedQuery(JpaConst.Q_LIK_COUNT_BY_EMPID_AND_REPID, Long.class)
                    .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                    .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(report))
                    .getSingleResult();
        if(count == 1){
           return true;
        }else{
           return false;
        }
    }

    public void destroy(ReportView rv,EmployeeView ev) {
      //従業員id、日報idを条件にデータを取得
        LikeButton lb = em.createNamedQuery(JpaConst.Q_LIK_GET_BY_EMPID_AND_REPID, LikeButton.class)
                     .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(ev))
                     .setParameter(JpaConst.JPQL_PARM_REPORT, ReportConverter.toModel(rv))
                     .getSingleResult();
        em.getTransaction().begin();
        em.remove(lb);       // データ削除
        em.getTransaction().commit();
        em.close();

    }
}
